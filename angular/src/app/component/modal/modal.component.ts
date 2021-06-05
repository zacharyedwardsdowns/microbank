import { ComponentType } from '@angular/cdk/portal';
import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ModalTemplate } from 'src/app/model/modal-template.model';
import { ModalService } from 'src/app/service/modal.service';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
})
export class ModalComponent {
  private readonly loginModal = '(modal:login-modal)';
  private component: ComponentType<any>;
  private template: ModalTemplate;
  private originalUrl: string;

  constructor(
    private router: Router,
    private location: Location,
    private modalService: ModalService
  ) {
    let modalUrl: string;

    if (router.url.endsWith(this.loginModal)) {
      this.originalUrl = router.url.replace(this.loginModal, '');
      modalUrl = 'login';

      this.component = LoginComponent;
      this.template = {
        panelClass: 'modal-dialog-container',
      } as ModalTemplate;
    }

    if (modalUrl) {
      location.replaceState(modalUrl);
    }

    if ((this.component, this.template)) {
      this.modalService
        .openModal(this.component, this.template)
        .afterClosed()
        .subscribe(() => {
          this.navigateToPreviousUrl();
        });
    } else {
      this.navigateToPreviousUrl();
    }
  }

  private navigateToPreviousUrl() {
    if (!this.modalService.returnToPrevious) {
      this.modalService.returnToPrevious = true;
    } else if (this.originalUrl) {
      this.router.navigateByUrl(this.originalUrl);
    } else {
      this.router.navigateByUrl('home');
    }
  }
}
