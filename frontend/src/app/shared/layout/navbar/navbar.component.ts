import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

isOpen = false;
  auth: any;

toggleDropdown() {
  console.log("clicked"); // 👈 check click working or not
  this.isOpen = !this.isOpen;
}

logout() {
  this.auth.logout();
}

}
