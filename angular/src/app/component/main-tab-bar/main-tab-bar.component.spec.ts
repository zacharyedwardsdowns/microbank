import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MainTabBarComponent } from './main-tab-bar.component';

describe('MainTabBarComponent', () => {
  let component: MainTabBarComponent;
  let fixture: ComponentFixture<MainTabBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MainTabBarComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainTabBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
