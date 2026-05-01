import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './contact.component.html'
})
export class ContactComponent {

  name = '';
  email = '';
  phone = '';
  message = '';

  successMessage = '';
  errorMessage = '';

  onSubmit() {

    if (!this.name || !this.email || !this.message) {
      this.errorMessage = 'Please fill all required fields';
      this.successMessage = '';
      return;
    }

    console.log('Form Data:', {
      name: this.name,
      email: this.email,
      phone: this.phone,
      message: this.message
    });

    this.successMessage = 'Message sent successfully!';
    this.errorMessage = '';

    // Reset form
    this.name = '';
    this.email = '';
    this.phone = '';
    this.message = '';
  }
}