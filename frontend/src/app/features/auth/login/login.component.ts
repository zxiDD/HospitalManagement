// import { Component } from '@angular/core';
// import { FormsModule } from '@angular/forms';
// import { AuthService } from '../../../core/services/auth.service';
// import { Router, RouterLink, RouterModule } from '@angular/router';

// @Component({
//   selector: 'app-login',
//   standalone: true,
//   imports: [FormsModule,RouterModule],
//   templateUrl: './login.component.html'
// })
// export class LoginComponent {

//   username = '';
//   password = '';
//   errorMessage = '';

//   constructor(private auth: AuthService, private router: Router) {}

//   onLogin() {
//     const data = {
//       username: this.username,
//       password: this.password
//     };

//     this.auth.login(data).subscribe({
//       next: (res) => {
//         this.auth.saveToken(res.token);
//         this.router.navigate(['/dashboard']);
//       },
//       error: () => {
//         this.errorMessage = 'Invalid credentials';
//       }
//     });
//   }
// }

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule,CommonModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  username = '';
  password = '';
  errorMessage = '';
  showPassword = false; // 👁️ toggle

  constructor(private auth: AuthService, private router: Router) {}

  onLogin() {
    // basic validation
    if (!this.username || !this.password) {
      this.errorMessage = 'Please enter username and password';
      return;
    }

    const data = {
      username: this.username,
      password: this.password
    };

    this.auth.login(data).subscribe({
      next: (res: any) => {
        console.log('Login success:', res);

        // ⚠️ Handle different backend responses
        const token = res.token || res.jwt || res.accessToken;

        if (token) {
          this.auth.saveToken(token);
          this.router.navigate(['/dashboard']);
        } else {
          this.errorMessage = 'Token not received from server';
        }
      },
      error: (err) => {
        console.log('Login error:', err);

        if (err.status === 401) {
          this.errorMessage = 'Invalid username or password';
        } else if (err.status === 0) {
          this.errorMessage = 'Server not reachable';
        } else {
          this.errorMessage = 'Something went wrong';
        }
      }
    });
  }
}