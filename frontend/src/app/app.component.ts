import { Component } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { ToastService } from './core/services/toast.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgFor, NgIf],
  template: `
    <router-outlet />
    <div class="toast-container">
      <div
        *ngFor="let t of toast.toasts()"
        class="toast"
        [class.success]="t.type === 'success'"
        [class.error]="t.type === 'error'"
        [class.info]="t.type === 'info'"
        (click)="toast.dismiss(t.id)"
      >
        <svg class="toast-icon" *ngIf="t.type === 'success'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 12l2 2 4-4" stroke="#059669"/><circle cx="12" cy="12" r="10" stroke="#059669"/></svg>
        <svg class="toast-icon" *ngIf="t.type === 'error'" viewBox="0 0 24 24" fill="none" stroke="#dc2626" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>
        <svg class="toast-icon" *ngIf="t.type === 'info'" viewBox="0 0 24 24" fill="none" stroke="#2563eb" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
        <span>{{ t.message }}</span>
      </div>
    </div>
  `,
})
export class AppComponent {
  constructor(readonly toast: ToastService) {}
}
