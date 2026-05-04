import { Component } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-physician',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './physician.component.html',
  styleUrl: './physician.component.css'
})
export class PhysicianComponent {
  doctors: any[] = [];
  searchText: string = '';   // ✅ ADD THIS

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getDoctors().subscribe(res => {
      this.doctors = res;
    });
  }

  // ✅ ADD THIS FUNCTION
  filteredDoctors() {
    return this.doctors.filter(d =>
      d.name?.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }

}
