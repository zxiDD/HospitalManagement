import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { menuItems } from '../../../menu.config';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent implements OnInit {
  role: string = '';
  menuItems: any[] = [];

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.role = this.auth.getRoles()[0];
    this.menuItems = menuItems[this.role] || [];
  }
}
