import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data);
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  // ✅ ADD THIS
  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  // ✅ ADD THIS
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // ✅ ADD THIS
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // ✅ ADD THIS
  logout() {
    localStorage.removeItem('token');
  }
}