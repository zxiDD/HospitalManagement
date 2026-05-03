import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patient-report',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './patient-report.component.html',
  styleUrl: './patient-report.component.css'
})
export class PatientReportComponent {
  totalPatients = 500;
  newPatients = 25;
  activePatients = 120;

}
