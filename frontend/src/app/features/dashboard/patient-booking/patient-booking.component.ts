import { Component, OnInit, signal, effect, computed } from '@angular/core';
import { DatePipe, NgClass, NgFor, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

interface Physician {
  employeeId: number;
  name: string;
  position: string;
}

interface Appointment {
  starto: string;
  endo: string;
}

interface TimeSlot {
  time: Date;
  available: boolean;
  selected: boolean;
}

@Component({
  selector: 'app-patient-booking',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, ReactiveFormsModule, DatePipe],
  templateUrl: './patient-booking.component.html',
  styleUrls: ['./patient-booking.component.css']
})
export class PatientBookingComponent implements OnInit {
  readonly loading = signal(false);
  readonly fetchingSlots = signal(false);
  readonly physicians = signal<Physician[]>([]);
  readonly slots = signal<TimeSlot[]>([]);
  readonly selectedSlot = signal<TimeSlot | null>(null);
  
  readonly form = this.fb.group({
    physicianId: ['', Validators.required],
    date: ['', Validators.required],
  });

  showSlots(): boolean {
    return !!this.form.get('physicianId')?.value && !!this.form.get('date')?.value;
  }

  readonly hasAvailableSlots = computed(() => {
    return this.slots().some(s => s.available);
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly api: ApiService,
    private readonly auth: AuthService,
    private readonly toast: ToastService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.api.get<Physician[]>('/physicians').subscribe({
      next: (docs) => this.physicians.set(docs),
      error: () => this.toast.error('Failed to load physicians')
    });
  }

  minDate(): string {
    const now = new Date();
    return now.toISOString().split('T')[0];
  }

  fetchAvailability(): void {
    const phyId = this.form.get('physicianId')?.value;
    const dateStr = this.form.get('date')?.value;

    if (!phyId || !dateStr) {
      this.slots.set([]);
      this.selectedSlot.set(null);
      return;
    }

    this.fetchingSlots.set(true);
    this.selectedSlot.set(null);

    // Fetch existing appointments for the physician
    this.api.get<Appointment[]>(`/appointments/physician/${phyId}/appointments`).subscribe({
      next: (appointments) => {
        this.generateSlots(dateStr, appointments);
        this.fetchingSlots.set(false);
      },
      error: () => {
        this.toast.error('Failed to fetch availability');
        this.fetchingSlots.set(false);
      }
    });
  }

  private generateSlots(dateStr: string, existingAppointments: Appointment[]): void {
    const generatedSlots: TimeSlot[] = [];
    
    // Operating hours: 09:00 to 17:00
    const startHour = 9;
    const endHour = 17;
    const now = new Date();

    for (let hour = startHour; hour < endHour; hour++) {
      for (let minute of [0, 30]) {
        // Construct the slot time
        const slotTime = new Date(`${dateStr}T${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00`);
        const slotEndTime = new Date(slotTime.getTime() + 30 * 60000); // 30 mins later

        // If the slot is in the past (e.g. today at an earlier time), mark unavailable
        if (slotTime < now) {
          generatedSlots.push({ time: slotTime, available: false, selected: false });
          continue;
        }

        // Check against existing appointments
        let isBooked = false;
        for (const appt of existingAppointments) {
          const apptStart = new Date(appt.starto);
          const apptEnd = new Date(appt.endo);

          // Overlap condition: slotStart < apptEnd AND slotEnd > apptStart
          if (slotTime < apptEnd && slotEndTime > apptStart) {
            isBooked = true;
            break;
          }
        }

        generatedSlots.push({
          time: slotTime,
          available: !isBooked,
          selected: false
        });
      }
    }

    this.slots.set(generatedSlots);
  }

  selectSlot(slot: TimeSlot): void {
    if (!slot.available) return;

    // Deselect all
    const updated = this.slots().map(s => ({
      ...s,
      selected: s.time.getTime() === slot.time.getTime()
    }));

    this.slots.set(updated);
    this.selectedSlot.set(updated.find(s => s.selected) || null);
  }

  submit(): void {
    if (this.form.invalid || !this.selectedSlot()) {
      this.toast.info('Please select a doctor, a date, and an available time slot.');
      return;
    }

    const val = this.form.getRawValue();
    const slot = this.selectedSlot()!;
    
    // Ensure accurate UTC / Local string parsing
    const start = new Date(slot.time);
    const end = new Date(start.getTime() + 30 * 60000); // Add 30 mins

    const payload = {
      patientId: this.auth.userId(),
      physicianId: Number(val.physicianId),
      starto: start.toISOString(),
      endo: end.toISOString(),
      examinationRoom: 'TBD',
      nurseId: null
    };

    this.loading.set(true);
    this.api.post('/appointments', payload).subscribe({
      next: () => {
        this.toast.success('Appointment booked successfully!');
        this.router.navigate(['/appointments']);
      },
      error: (err) => {
        this.toast.error(err.message || 'Failed to book appointment or slot no longer available.');
        this.loading.set(false);
        this.fetchAvailability(); // Refresh slots
      }
    });
  }
}
