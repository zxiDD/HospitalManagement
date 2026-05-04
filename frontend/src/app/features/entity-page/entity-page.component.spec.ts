import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EntityPageComponent } from './entity-page.component';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { of } from 'rxjs';

describe('EntityPageComponent', () => {
  let component: EntityPageComponent;
  let fixture: ComponentFixture<EntityPageComponent>;
  let mockApiService: any;
  let mockAuthService: any;
  let mockToastService: any;

  beforeEach(async () => {
    mockApiService = {
      get: () => of([]),
      post: () => of({}),
      put: () => of({}),
      delete: () => of({})
    };
    mockAuthService = {
      hasAnyRole: () => true,
      isAdmin: () => true,
      isPatient: () => false,
      userId: () => 1
    };
    mockToastService = {
      success: () => {},
      error: () => {},
      info: () => {}
    };

    await TestBed.configureTestingModule({
      imports: [EntityPageComponent, RouterTestingModule, HttpClientTestingModule, FormsModule, ReactiveFormsModule],
      providers: [
        { provide: ApiService, useValue: mockApiService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EntityPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
