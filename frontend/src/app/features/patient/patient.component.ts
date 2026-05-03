import { Component } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './patient.component.html'
})
export class PatientComponent {

  patients: any[] = [];
  searchText = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getPatients().subscribe(res => {
      this.patients = res;
    });
  }

  filteredPatients() {
    return this.patients.filter(p =>
      p.name.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }
}