import { Component, Inject, AfterViewInit, OnDestroy } from '@angular/core';
import { Tab } from 'src/app/model/tab.model';
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
    { name: 'Registration', route: 'registration' },
  ];

  constructor(
    private router: Router,
    @Inject(DOCUMENT) private document: Document,
    private registrationService: RegistrationService
  ) {
    this.registrationTabSub =
      this.registrationService.initializeRegistrationTab.subscribe((value) => {
        if (value === true) {
          this.registrationPage();
        }
      });
  }

  ngAfterViewInit(): void {
    let element: HTMLElement;
    let registrationElement: HTMLElement =
      this.document.getElementById('RegistrationTab');
    registrationElement.style.display = 'none';

    if (this.router.url === '/about') {
      element = this.document.getElementById('AboutTab');
    } else if (this.router.url === '/registration') {
      element = registrationElement;
      element.style.display = 'inline';
    } else {
      this.document.getElementById('HomeTab');
    }

    this.lastSelectedTab = element;
    this.selectTab(element, false);
  }

  ngOnDestroy(): void {
    this.registrationTabSub.unsubscribe();
  }

  loginModal(): void {
    this.router.navigate([{ outlets: { modal: 'login-modal' } }]);
  }

  registrationPage(): void {
    let element = this.document.getElementById('RegistrationTab');
    if (element.style.display === 'none' || element.style.display === '') {
      this.router.navigate(['/registration']);
      element.style.display = 'inline';
      this.selectTab(element);
    }
  }

  selectTab(element: HTMLElement, doNotSkip: boolean = true): void {
    if (this.lastSelectedTab) {
      if (
        doNotSkip &&
        this.lastSelectedTab.id === 'RegistrationTab' &&
        element.id !== 'RegistrationTab'
      ) {
        this.lastSelectedTab.style.display = 'none';
      }

      this.lastSelectedTab.style.borderBottom = '';
    }

    if (element) {
      if (element.tagName !== 'BUTTON') element = element.parentElement;
      element.style.borderBottom = '0.2rem solid';
      this.lastSelectedTab = element;
    }
  }
}
