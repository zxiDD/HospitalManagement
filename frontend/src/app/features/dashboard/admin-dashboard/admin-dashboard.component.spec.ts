import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AdminDashboardComponent } from './admin-dashboard.component';
import { ApiService } from '../../../core/services/api.service';
import { of } from 'rxjs';

describe('AdminDashboardComponent', () => {
  let component: AdminDashboardComponent;
  let fixture: ComponentFixture<AdminDashboardComponent>;
  let mockApiService: any;

  beforeEach(async () => {
    mockApiService = {
      get: (url: string) => {
        if (url === '/stats/counts') return of({});
        if (url === '/appointments') return of([]);
        return of(null);
      }
    };

    await TestBed.configureTestingModule({
      imports: [AdminDashboardComponent, RouterTestingModule, HttpClientTestingModule],
      providers: [{ provide: ApiService, useValue: mockApiService }]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
