import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-medications',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './medications.component.html',
  styleUrl: './medications.component.css'
})
export class MedicationsComponent {
    medications = [
    { id: 'D001', name: 'Paracetamol', stock: 500, expiry: '2026-07-16' },
    { id: 'D002', name: 'Ibuprofen', stock: 300, expiry: '2026-09-10' }
  ];

}
