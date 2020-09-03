import { TestBed } from '@angular/core/testing';
import { ModalService } from './modal.service';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { LoginComponent } from '../component/login/login.component';
import { ModalTemplate } from '../model/modal-template.model';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegisterComponent } from '../component/register/register.component';

describe('ModalService', () => {
  let template: ModalTemplate = { panelClass: 'modal-dialog-container' };
  let service: ModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatDialogModule, BrowserAnimationsModule],
    });
    service = TestBed.inject(ModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should ignore closing a non-existent modal', () => {
    expect(service.dialogReference).toBeUndefined();
    service.closeModal();
    expect(service.dialogReference).toBeUndefined();
  });

  it('should open a modal', () => {
    service.openModal(LoginComponent, template);
    expect(service.dialogReference).toBeTruthy();
  });

  it('should open a modal with given height and width', () => {
    template.height = '20rem';
    template.width = '35rem';
    service.openModal(LoginComponent, template);
    expect(service.dialogReference).toBeTruthy();
  });

  it('should close the modal', () => {
    service.openModal(RegisterComponent, template);
    expect(service.dialogReference).toBeTruthy();
    service.closeModal();
    expect(service.dialogReference).toBeNull();
  });
});
