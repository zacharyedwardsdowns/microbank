import { Component, Inject, AfterViewInit, OnDestroy } from '@angular/core';
import { ModalTemplate } from 'src/app/model/modal-template.model';
import { Tab } from 'src/app/model/tab.model';
import { ModalService } from 'src/app/service/modal.service';
import { LoginComponent } from '../login/login.component';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RegistrationService } from 'src/app/service/registration.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss'],
})
export class LandingPageComponent implements AfterViewInit, OnDestroy {
  private registrationTabSub: Subscription;
  public lastSelectedTab: HTMLElement;
  public tabs: Tab[] = [
    { name: 'Home', route: 'home' },
    { name: 'About', route: 'about' },
    { name: 'Register', route: 'registration' },
  ];

  constructor(
    private router: Router,
    private modalService: ModalService,
    @Inject(DOCUMENT) private document: Document,
    private registrationService: RegistrationService
  ) {
    this.registrationTabSub = this.registrationService.initializeRegistrationTab.subscribe(
      (value) => {
        if (value === true) {
          this.registrationPage();
        }
      }
    );
  }

  ngAfterViewInit(): void {
    let element: HTMLElement;
    let registrationElement: HTMLElement = this.document.getElementById(
      'RegisterTab'
    );
    registrationElement.style.display = 'none';

    if (this.router.url === '/about') {
      element = this.document.getElementById('AboutTab');
    } else if (this.router.url === '/registration') {
      element = registrationElement;
      element.style.display = 'inline';
    } else {
      element = this.document.getElementById('HomeTab');
    }
    this.lastSelectedTab = element;
    this.selectTab(element, false);
  }

  ngOnDestroy(): void {
    this.registrationTabSub.unsubscribe();
  }

  loginModal(): void {
    const modalTemplate: ModalTemplate = {
      panelClass: 'modal-dialog-container',
    };
    this.modalService.openModal(LoginComponent, modalTemplate);
  }

  registrationPage(): void {
    let element = this.document.getElementById('RegisterTab');
    if (element.style.display === 'none' || element.style.display === '') {
      this.router.navigate(['/registration']);
      element.style.display = 'inline';
      this.selectTab(element);
    }
  }

  selectTab(element: HTMLElement, doNotSkip: boolean = true): void {
    if (
      doNotSkip &&
      this.lastSelectedTab.id === 'RegisterTab' &&
      element.id !== 'RegisterTab'
    ) {
      this.lastSelectedTab.style.display = 'none';
    }
    if (element.tagName !== 'BUTTON') element = element.parentElement;
    this.lastSelectedTab.style.borderBottom = '';
    element.style.borderBottom = '0.2rem solid';
    this.lastSelectedTab = element;
  }
}
