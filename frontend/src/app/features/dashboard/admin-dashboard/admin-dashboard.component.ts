import { Component, OnInit, signal } from '@angular/core';
import { NgFor, NgIf, DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { forkJoin } from 'rxjs';
import { ApiService } from '../../../core/services/api.service';
import { Appointment } from '../../../core/models/api.models';

interface StatCard {
  label: string;
  value: number;
  hint: string;
  color: string;
  icon: SafeHtml;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [DecimalPipe, DatePipe, NgFor, NgIf, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  readonly stats = signal<StatCard[]>([]);
  readonly recentAppointments = signal<Appointment[]>([]);

  constructor(private readonly api: ApiService, private readonly sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    forkJoin({
      counts: this.api.get<Record<string, number>>('/stats/counts'),
      appointments: this.api.get<Appointment[]>('/appointments'),
    }).subscribe(({ counts, appointments }) => {
      this.stats.set([
        { label: 'Active Patients', value: counts['patients'] ?? 0, hint: 'Registered in system', color: 'blue', icon: this.svg('M17 20h5v-2a4 4 0 00-3-3.87M9 20H4v-2a4 4 0 013-3.87m6 5.87v-2a4 4 0 00-3-3.87m3 5.87v-2a4 4 0 013-3.87M16 7a4 4 0 11-8 0 4 4 0 018 0z') },
        { label: 'Medical Doctors', value: counts['physicians'] ?? 0, hint: 'Active clinical staff', color: 'teal', icon: this.svg('M4.26 10.147a60 60 0 00-.491 6.347A48.6 48.6 0 0112 20.904a48.6 48.6 0 018.232-4.41 60.5 60.5 0 00-.491-6.347') },
        { label: 'Nursing Staff', value: counts['nurses'] ?? 0, hint: 'Care & prep teams', color: 'green', icon: this.svg('M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z') },
        { label: 'Total Appointments', value: counts['appointments'] ?? 0, hint: 'Scheduled & past', color: 'amber', icon: this.svg('M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75') },
        { label: 'Hospital Departments', value: counts['departments'] ?? 0, hint: 'Operational units', color: 'blue', icon: this.svg('M3.75 21h16.5M4.5 3h15M5.25 3v18m13.5-18v18M9 6.75h1.5m-1.5 3h1.5m-1.5 3h1.5m3-6H15m-1.5 3H15m-1.5 3H15M9 21v-3.375c0-.621.504-1.125 1.125-1.125h3.75c.621 0 1.125.504 1.125 1.125V21') },
        { label: 'Room Inventory', value: counts['rooms'] ?? 0, hint: 'Across all blocks', color: 'teal', icon: this.svg('M2.25 12l8.954-8.955a1.126 1.126 0 011.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75') },
      ]);

      // Show last 5 appointments sorted by date
      const sorted = [...appointments]
        .sort((a, b) => new Date(b.starto).getTime() - new Date(a.starto).getTime())
        .slice(0, 5);
      this.recentAppointments.set(sorted);
    });
  }

  private svg(d: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(
      `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="22" height="22"><path stroke-linecap="round" stroke-linejoin="round" d="${d}"/></svg>`
    );
  }
}
