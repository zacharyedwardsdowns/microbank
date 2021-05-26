import { Injectable } from '@angular/core';
import { ComponentType } from '@angular/cdk/portal';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ModalTemplate } from 'src/app/model/modal-template.model';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  public dialogReference: MatDialogRef<any, ModalTemplate>;
  public readonly defaultHeight: string = '30rem';
  public readonly defaultWidth: string = '25rem';

  constructor(public dialog: MatDialog) {}

  openModal(
    component: ComponentType<any>,
    template: ModalTemplate
  ): MatDialogRef<any, ModalTemplate> {
    if (!template.width) template.width = this.defaultWidth;
    if (!template.height) template.height = this.defaultHeight;
    this.dialogReference = this.dialog.open(component, template);
    return this.dialogReference;
  }

  closeModal(): void {
    if (this.dialogReference) {
      this.dialogReference.close();
      this.dialogReference = null;
    }
  }
}
