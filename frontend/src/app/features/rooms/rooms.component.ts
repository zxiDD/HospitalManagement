import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})
export class RoomsComponent {
    rooms = [
    { roomNo: 101, floor: 1, type: 'ICU', status: 'Occupied' },
    { roomNo: 102, floor: 1, type: 'General', status: 'Available' },
    { roomNo: 201, floor: 2, type: 'Private', status: 'Available' }
  ];

}
