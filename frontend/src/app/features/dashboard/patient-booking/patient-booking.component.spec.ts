import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { PatientBookingComponent } from './patient-booking.component';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { of } from 'rxjs';

describe('PatientBookingComponent', () => {
  let component: PatientBookingComponent;
  let fixture: ComponentFixture<PatientBookingComponent>;
  let mockApiService: any;
  let mockAuthService: any;
  let mockToastService: any;

  beforeEach(async () => {
    mockApiService = {
      get: () => of([]),
      post: () => of({})
    };
    mockAuthService = {
      userId: () => 1
    };
    mockToastService = {
      success: () => {},
      error: () => {},
      info: () => {}
    };

    await TestBed.configureTestingModule({
      imports: [PatientBookingComponent, ReactiveFormsModule, RouterTestingModule, HttpClientTestingModule],
      providers: [
        { provide: ApiService, useValue: mockApiService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PatientBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
