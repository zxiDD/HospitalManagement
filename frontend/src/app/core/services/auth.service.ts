import { computed, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, SignupRequest } from '../models/api.models';

const AUTH_KEY = 'hms.auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly session = signal<LoginResponse | null>(this.readSession());

  readonly currentUser = computed(() => this.session());
  readonly isAuthenticated = computed(() => Boolean(this.session()?.token));
  readonly roles = computed(() => this.session()?.roles ?? []);
  readonly userId = computed(() => this.session()?.userId ?? null);
  readonly userName = computed(() => this.session()?.userName ?? '');
  readonly isAdmin = computed(() => this.hasRole('ROLE_ADMIN'));
  readonly isPatient = computed(() => this.hasRole('ROLE_PATIENT'));

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiBaseUrl}/auth/login`, payload).pipe(
      tap((response) => {
        localStorage.setItem(AUTH_KEY, JSON.stringify(response));
        this.session.set(response);
      }),
    );
  }

  register(payload: SignupRequest): Observable<string> {
    return this.http.post(`${environment.apiBaseUrl}/auth/register`, payload, { responseType: 'text' });
  }

  token(): string | null {
    return this.session()?.token ?? null;
  }

  hasRole(role: string): boolean {
    return this.roles().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.hasRole(role));
  }

  redirectAfterLogin(): void {
    if (this.hasRole('ROLE_ADMIN')) {
      this.router.navigateByUrl('/dashboard');
      return;
    }

    const patientId = this.userId();
    this.router.navigateByUrl(patientId ? `/patients/${patientId}/dashboard` : '/appointments');
  }

  logout(): void {
    localStorage.removeItem(AUTH_KEY);
    this.session.set(null);
    this.router.navigateByUrl('/login');
  }

  private readSession(): LoginResponse | null {
    const raw = localStorage.getItem(AUTH_KEY);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as LoginResponse;
    } catch {
      localStorage.removeItem(AUTH_KEY);
      return null;
    }
  }
}
