import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LandingComponent } from './features/landing/landing.component';
import { ContactComponent } from './features/contact/contact.component';
import { PatientDashboardComponent } from './features/patient-dashboard/patient-dashboard.component';

export const routes: Routes = [
    { path: '', component: LandingComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'contact', component: ContactComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'patient-dashboard', component: PatientDashboardComponent }
];
