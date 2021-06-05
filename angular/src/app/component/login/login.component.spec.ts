import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ModalService } from 'src/app/service/modal.service';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationService } from 'src/app/service/registration.service';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject } from 'rxjs';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  const mockRegistrationService: any = {
    initializeRegistrationTab: new BehaviorSubject<boolean>(false),
  };
  let component: LoginComponent;
  let mockModalService: any;
  let router: Router;

  beforeEach(
    waitForAsync(() => {
      mockModalService = jasmine.createSpyObj(['closeModal']);

      TestBed.configureTestingModule({
        imports: [
          RouterTestingModule,
          MatDialogModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          BrowserAnimationsModule,
        ],
        declarations: [LoginComponent],
        providers: [
          { provide: ModalService, useValue: mockModalService },
          { provide: RegistrationService, useValue: mockRegistrationService },
        ],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call modal service to close modal', () => {
    component.close();
    expect(mockModalService.closeModal).toHaveBeenCalled();
  });

  it('should navigate to registration page', () => {
    component.registrationPage();
    let registrationRoute = '/registration';
    spyOnProperty(router, 'url', 'get').and.returnValue(registrationRoute);
    fixture.detectChanges();
    expect(router.url).toEqual(registrationRoute);
    expect(mockModalService.closeModal).toHaveBeenCalled();
  });
});
