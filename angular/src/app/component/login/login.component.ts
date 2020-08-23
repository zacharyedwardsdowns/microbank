import { Component, OnInit } from '@angular/core';
import { ModalService } from 'src/app/service/modal.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  constructor(private modelService: ModalService) {}

  ngOnInit(): void {}

  close() {
    this.modelService.closeModal();
  }
}
