import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LandingPageComponent } from './landing-page.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ModalService } from 'src/app/service/modal.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('LandingPageComponent', () => {
  let fixture: ComponentFixture<LandingPageComponent>;
  let component: LandingPageComponent;
  let mockModalService: any;
  let router: Router;

  beforeEach(async(() => {
    mockModalService = jasmine.createSpyObj(['openModal']);
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, MatDialogModule, BrowserAnimationsModule],
      declarations: [LandingPageComponent],
      providers: [{ provide: ModalService, useValue: mockModalService }],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LandingPageComponent);
    component = fixture.componentInstance;
    router = TestBed.get(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should select tab home', () => {
    const route = `/${component.tabs[0].route}`;
    spyOnProperty(router, 'url', 'get').and.returnValue(route);
    fixture.detectChanges();
    expect(router.url).toEqual(route);
  });

  it('should select tab about', () => {
    const route = `/${component.tabs[1].route}`;
    spyOnProperty(router, 'url', 'get').and.returnValue(route);
    fixture.detectChanges();
    expect(router.url).toEqual(route);
  });

  it('should open the login modal', () => {
    component.loginModal();
    expect(mockModalService.openModal).toHaveBeenCalled();
  });

  it('should open the register modal', () => {
    component.registerModal();
    expect(mockModalService.openModal).toHaveBeenCalled();
  });

  it('should select the parent component if not of type button', () => {
    fixture.detectChanges();
    const child: HTMLElement = document.getElementById(
      `${component.tabs[1].name}Tab`
    ).firstChild as HTMLElement;
    component.selectTab(child);
  });
});
