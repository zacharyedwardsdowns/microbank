import { Location } from '@angular/common';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ModalService } from 'src/app/service/modal.service';
import { ModalComponent } from './modal.component';

describe('ModalComponent', () => {
  let fixture: ComponentFixture<ModalComponent>;
  let component: ModalComponent;
  let mockModalService: any;

  beforeEach(async () => {
    mockModalService = jasmine.createSpyObj(['openModal']);

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ModalComponent],
      providers: [
        Location,
        { provide: ModalService, useValue: mockModalService },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
