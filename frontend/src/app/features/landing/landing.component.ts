import { Component, AfterViewInit, Inject, PLATFORM_ID } from '@angular/core';
import { RouterModule } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './landing.component.html'
})
export class LandingComponent implements AfterViewInit {

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  doctorsCount = 0;
  patientsCount = 0;
  appointmentsCount = 0;

  private counterStarted = false;

  ngAfterViewInit() {

    if (isPlatformBrowser(this.platformId)) {

      const statsSection = document.getElementById('stats');

      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting && !this.counterStarted) {
            this.counterStarted = true;
            this.startCounter();
          }
        });
      }, { threshold: 0.5 });

      if (statsSection) {
        observer.observe(statsSection);
      }
    }
  }

  startCounter() {
    const interval = setInterval(() => {
      if (this.doctorsCount < 100) this.doctorsCount++;
      if (this.patientsCount < 500) this.patientsCount += 5;
      if (this.appointmentsCount < 300) this.appointmentsCount += 3;

      if (
        this.doctorsCount >= 100 &&
        this.patientsCount >= 500 &&
        this.appointmentsCount >= 300
      ) {
        clearInterval(interval);
      }
    }, 20);
  }

  scrollToFeatures() {
    document.getElementById('features')?.scrollIntoView({ behavior: 'smooth' });
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}