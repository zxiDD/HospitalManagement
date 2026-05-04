import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { PatientDashboardComponent } from './patient-dashboard.component';
import { ApiService } from '../../../core/services/api.service';
import { of } from 'rxjs';

describe('PatientDashboardComponent', () => {
  let component: PatientDashboardComponent;
  let fixture: ComponentFixture<PatientDashboardComponent>;
  let mockApiService: any;

  beforeEach(async () => {
    mockApiService = {
      get: () => of({
        patient: { name: 'Test Patient', ssn: 123 },
        appointments: [],
        medications: []
      })
    };

    await TestBed.configureTestingModule({
      imports: [PatientDashboardComponent, HttpClientTestingModule],
      providers: [{ provide: ApiService, useValue: mockApiService }]
    }).compileComponents();

    fixture = TestBed.createComponent(PatientDashboardComponent);
    component = fixture.componentInstance;
    component.ssn = '123';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
