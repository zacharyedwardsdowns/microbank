import { ModalTemplate } from './modal-template.model';
import { ComponentType } from '@angular/cdk/portal';

export class ModalRouteParams {
  component: ComponentType<any>;
  template: ModalTemplate;
}
