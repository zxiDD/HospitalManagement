import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private baseUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  getPatientDashboard(ssn: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/patients/${ssn}/dashboard`);
  }
}
