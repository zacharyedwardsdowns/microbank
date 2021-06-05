import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalService } from 'src/app/service/modal.service';
import { RegistrationService } from 'src/app/service/registration.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  public hide = true;

  constructor(
    private router: Router,
    private modelService: ModalService,
    private registrationService: RegistrationService
  ) {}

  ngOnInit(): void {}

  close() {
    this.modelService.closeModal();
  }

  registrationPage() {
    this.modelService.returnToPrevious = false;
    this.close();
    this.registrationService.initializeRegistrationTab.next(true);
    this.router.navigateByUrl('registration');
  }
}
