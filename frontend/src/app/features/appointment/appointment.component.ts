import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './appointment.component.html',
  styleUrl: './appointment.component.css'
})
export class AppointmentComponent implements OnInit {
  appointments: any[] = [];
  searchText: string = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getAppointments().subscribe(res => {
      this.appointments = res;
    });
  }

  filteredAppointments() {
    return this.appointments.filter(a =>
      (a.patient?.toLowerCase().includes(this.searchText.toLowerCase()) ||
       a.doctor?.toLowerCase().includes(this.searchText.toLowerCase()))
    );
  }

}
