import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getPatients(): Observable<any> {
    return this.http.get(`${this.baseUrl}/patients`);
  }

  getDoctors(): Observable<any> {
    return this.http.get(`${this.baseUrl}/physicians`);
  }

  getAppointments(): Observable<any> {
    return this.http.get(`${this.baseUrl}/appointments`);
  }
  getRooms() : Observable<any> {
  return this.http.get(`${this.baseUrl}/rooms`);
}
}
