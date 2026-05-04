import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { LayoutComponent } from './layout.component';
import { AuthService } from '../core/services/auth.service';

describe('LayoutComponent', () => {
  let component: LayoutComponent;
  let fixture: ComponentFixture<LayoutComponent>;
  let mockAuthService: any;

  beforeEach(async () => {
    mockAuthService = {
      userName: () => 'Test User',
      isAdmin: () => true,
      userId: () => 1,
      hasAnyRole: () => true,
      logout: () => {}
    };

    await TestBed.configureTestingModule({
      imports: [LayoutComponent, RouterTestingModule],
      providers: [{ provide: AuthService, useValue: mockAuthService }]
    }).compileComponents();

    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
