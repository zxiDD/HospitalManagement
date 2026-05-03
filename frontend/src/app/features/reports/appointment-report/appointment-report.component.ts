import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-appointment-report',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './appointment-report.component.html',
  styleUrl: './appointment-report.component.css'
})
export class AppointmentReportComponent implements OnInit {
  totalAppointments = 125;
  todayAppointments = 12;
  cancelledAppointments = 2;

  ngOnInit(): void {}

}
