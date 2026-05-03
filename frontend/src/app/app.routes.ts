import { Routes } from '@angular/router';
import { MainLayoutComponent } from './shared/layout/main-layout/main-layout.component';

import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LandingComponent } from './features/landing/landing.component';

import { DashboardComponent } from './features/dashboard/dashboard.component';
import { PatientComponent } from './features/patient/patient.component';
import { PhysicianComponent } from './features/physician/physician.component';
import { AppointmentComponent } from './features/appointment/appointment.component';

// ✅ ADD THESE IMPORTS
import { RoomsComponent } from './features/rooms/rooms.component';
import { MedicationsComponent } from './features/medications/medications.component';
import { AppointmentReportComponent } from './features/reports/appointment-report/appointment-report.component';
import { PatientReportComponent } from './features/reports/patient-report/patient-report.component';
import { PatientDashboardComponent } from './features/patient-dashboard/patient-dashboard.component';

export const routes: Routes = [

  // 🌐 PUBLIC
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // 🔐 ADMIN LAYOUT
  {
    path: '',
    component: MainLayoutComponent,
    children: [

      { path: 'dashboard', component: DashboardComponent },
      { path: 'patients', component: PatientComponent },
      { path: 'doctors', component: PhysicianComponent },
      { path: 'appointments', component: AppointmentComponent },

      // ✅ NEW ROUTES
      { path: 'rooms', component: RoomsComponent },
      { path: 'medications', component: MedicationsComponent },
      { path: 'appointment-report', component: AppointmentReportComponent },
      { path: 'patient-report', component: PatientReportComponent },
      { path: 'patient-dashboard', component: PatientDashboardComponent }


    ]
  },

  // ❗ fallback
  { path: '**', redirectTo: 'dashboard' }

];
