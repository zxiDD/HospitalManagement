import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent, NavbarComponent],
  template: `
    <div class="layout">

      <app-sidebar></app-sidebar>

      <div class="main">
        <app-navbar></app-navbar>

        <div class="content">
          <router-outlet></router-outlet>
        </div>
      </div>

    </div>
  `
})
export class MainLayoutComponent {}