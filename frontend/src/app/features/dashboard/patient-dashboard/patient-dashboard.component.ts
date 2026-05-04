import { Component, Input, OnInit, signal, computed } from '@angular/core';
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { ApiService } from '../../../core/services/api.service';
import { PatientDashboard } from '../../../core/models/api.models';

@Component({
  selector: 'app-patient-dashboard',
  standalone: true,
  imports: [DatePipe, NgFor, NgIf],
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.css']
})
export class PatientDashboardComponent implements OnInit {
  @Input() ssn = '';
  readonly dashboard = signal<PatientDashboard | null>(null);

  readonly greeting = computed(() => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning,';
    if (hour < 17) return 'Good afternoon,';
    return 'Good evening,';
  });

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.api.get<PatientDashboard>(`/patients/${this.ssn}/dashboard`).subscribe((dashboard) => this.dashboard.set(dashboard));
  }

  nameInitials(name: string): string {
    const parts = name.split(/\s+/);
    return parts.length >= 2
      ? (parts[0][0] + parts[1][0]).toUpperCase()
      : name.substring(0, 2).toUpperCase();
  }
}
