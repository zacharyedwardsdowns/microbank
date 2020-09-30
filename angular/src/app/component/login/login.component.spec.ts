import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ModalService } from 'src/app/service/modal.service';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let mockModalService: any;

  beforeEach(async(() => {
    mockModalService = jasmine.createSpyObj(['closeModal']);
    TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        BrowserAnimationsModule,
      ],
      declarations: [LoginComponent],
      providers: [{ provide: ModalService, useValue: mockModalService }],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call modal service to close modal', () => {
    component.close();
    expect(mockModalService.closeModal).toHaveBeenCalled();
  });
});
