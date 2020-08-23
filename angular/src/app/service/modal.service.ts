import { Injectable } from '@angular/core';
import { ComponentType } from '@angular/cdk/portal';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ModalTemplate } from 'src/app/model/modal-template.model';

const defaultWidth: string = '25rem';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  public dialogReference: MatDialogRef<any, ModalTemplate>;

  constructor(public dialog: MatDialog) {}

  openModal(
    component: ComponentType<any>,
    template: ModalTemplate,
    width?: string,
    height?: string
  ): MatDialogRef<AnalyserNode, ModalTemplate> {
    if (!width) template.width = defaultWidth;
    this.dialogReference = this.dialog.open(component, template);
    return this.dialogReference;
  }

  closeModal(): void {
    if (this.dialogReference) {
      this.dialogReference.close();
    }
  }
}
