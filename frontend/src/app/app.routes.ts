import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then((m) => m.RegisterComponent),
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        canActivate: [roleGuard],
        data: { roles: ['ROLE_ADMIN'] },
        loadComponent: () =>
          import('./features/dashboard/admin-dashboard/admin-dashboard.component').then((m) => m.AdminDashboardComponent),
      },
      {
        path: 'patients/:ssn/dashboard',
        loadComponent: () =>
          import('./features/dashboard/patient-dashboard/patient-dashboard.component').then(
            (m) => m.PatientDashboardComponent,
          ),
      },
      {
        path: 'patient/book',
        canActivate: [roleGuard],
        data: { roles: ['ROLE_PATIENT'] },
        loadComponent: () => import('./features/dashboard/patient-booking/patient-booking.component').then((m) => m.PatientBookingComponent),
      },
      {
        path: ':entity',
        loadComponent: () => import('./features/entity-page/entity-page.component').then((m) => m.EntityPageComponent),
      },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
