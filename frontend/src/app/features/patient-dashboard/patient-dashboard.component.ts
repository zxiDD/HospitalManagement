import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { PatientService } from '../../core/services/patient.service';

@Component({
  selector: 'app-patient-dashboard',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css',
})
export class PatientDashboardComponent implements OnInit {
  dashboardData: any;
  patient: any;
  appointments: any[] = [];
  stayDetails: any;
  medications: any[] = [];
  activities: any[] = [];

  constructor(
    private patientService: PatientService,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    const ssn = Number(this.auth.getUserId());

    if (ssn) {
      this.loadDashboard(ssn);
    }
  }

  loadDashboard(ssn: number) {
    this.patientService.getPatientDashboard(ssn).subscribe({
      next: (data: any) => {
        this.dashboardData = data;
        this.patient = data.patient;
        this.appointments = data.appointments || [];
        this.stayDetails = data.stay;
        this.medications = data.medications || [];
        this.activities = data.activities || [];
      },
      error: (err: any) => {
        console.error('Dashboard loading failed', err);
      },
    });
  }

  logout() {
    this.auth.logout();
  }
}
