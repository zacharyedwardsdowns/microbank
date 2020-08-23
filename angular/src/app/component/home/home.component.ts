import { Component, Inject, AfterViewInit } from '@angular/core';
import { ModalTemplate } from 'src/app/model/modal-template.model';
import { ModalService } from 'src/app/service/modal.service';
import { LoginComponent } from '../login/login.component';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements AfterViewInit {
  public tabs: string[] = ['Home', 'About'];
  private lastSelectedTab: HTMLElement;

  constructor(
    private route: Router,
    private modalService: ModalService,
    @Inject(DOCUMENT) private document: Document
  ) {}

  ngAfterViewInit(): void {
    const element = this.document.getElementById('HomeTab');
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
    this.route.navigate(['/register']);
  }

  selectTab(element: HTMLElement): void {
    if (element.tagName !== 'BUTTON') element = element.parentElement;
    this.lastSelectedTab.style.borderBottom = '';
    element.style.borderBottom = `0.2rem solid`;
    this.lastSelectedTab = element;
  }
}
