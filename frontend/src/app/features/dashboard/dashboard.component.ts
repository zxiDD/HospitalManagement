import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  patientsCount = 0;
  doctorCount = 0;
  appointmentCount = 0;
  roomCount = 0;

  constructor(private api: ApiService) {}

  ngOnInit(): void {

    this.api.getPatients().subscribe((res: string | any[]) => {
      this.patientsCount = res.length;
    });

    this.api.getDoctors().subscribe((res: string | any[]) => {
      this.doctorCount = res.length;
    });

    this.api.getAppointments().subscribe((res: string | any[]) => {
      this.appointmentCount = res.length;
    });
    this.api.getRooms().subscribe((res: string | any[]) => {
      this.roomCount = res.length;
    });

  }
}