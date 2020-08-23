import { Component, Inject, AfterViewInit } from '@angular/core';
import { ModalTemplate } from 'src/app/model/modal-template.model';
import { Tab } from 'src/app/model/tab.model';
import { ModalService } from 'src/app/service/modal.service';
import { LoginComponent } from '../login/login.component';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';
import { RegisterComponent } from '../register/register.component';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss'],
})
export class LandingPageComponent implements AfterViewInit {
  private lastSelectedTab: HTMLElement;
  public tabs: Tab[] = [
    { name: 'Home', route: 'home' },
    { name: 'About', route: 'about' },
  ];

  constructor(
    private router: Router,
    private modalService: ModalService,
    @Inject(DOCUMENT) private document: Document
  ) {}

  ngAfterViewInit(): void {
    let element: HTMLElement;
    if (this.router.url === '/about') {
      element = this.document.getElementById('AboutTab');
    } else {
      element = this.document.getElementById('HomeTab');
    }
    this.lastSelectedTab = element;
    this.selectTab(element);
  }

  loginModal(): void {
    const modalTemplate: ModalTemplate = {
      panelClass: 'modal-dialog-container',
    };
    this.modalService.openModal(LoginComponent, modalTemplate);
  }

  registerModal(): void {
    const modalTemplate: ModalTemplate = {
      panelClass: 'modal-dialog-container',
    };
    this.modalService.openModal(RegisterComponent, modalTemplate);
  }

  selectTab(element: HTMLElement): void {
    if (element.tagName !== 'BUTTON') element = element.parentElement;
    this.lastSelectedTab.style.borderBottom = '';
    element.style.borderBottom = '0.2rem solid';
    this.lastSelectedTab = element;
  }
}
