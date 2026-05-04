import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule,RouterModule,CommonModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  username = '';
  password = '';
  patientName = '';
  phoneNo = '';
  address = '';
  ssn: number | null = null;
  insuranceId: number | null = null;

  message = '';
  showPassword = false;

  constructor(private auth: AuthService, private router: Router) {}

  onRegister() {

    const data = {
      username: this.username,
      password: this.password,
      patientName: this.patientName,
      phoneNo: this.phoneNo,
      address: this.address,
      ssn: this.ssn,
      insuranceId: this.insuranceId
    };

    this.auth.register(data).subscribe({
      next: () => {
        this.message = 'Registration successful!';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: () => {
        this.message = 'Registration failed';
      }
    });
  }
}