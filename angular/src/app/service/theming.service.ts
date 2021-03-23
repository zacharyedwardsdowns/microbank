import { Injectable, ApplicationRef, Inject } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class ThemingService {
  private themes = ['dark-theme', 'light-theme']; // <- list all themes in this array
  public theme = new BehaviorSubject('dark-theme'); // <- initial theme
  public mql: MediaQueryList;

  constructor(
    public ref: ApplicationRef,
    @Inject(DOCUMENT) private document: Document
  ) {
    const darkModeOn =
      window.matchMedia &&
      window.matchMedia('(prefers-color-scheme: dark)').matches;

    // If dark mode is enabled then directly switch to the dark-theme
    if (darkModeOn) {
      this.theme.next('dark-theme');
    }

    // Watch for changes of the preference
    this.mql = window.matchMedia('(prefers-color-scheme: dark)');
  }

  public setVariablesAndRefresh(): void {
    this.setGlobalColorVariables();
    this.ref.tick();
  }

  private setGlobalColorVariables(): void {
    let style: CSSStyleDeclaration = this.document.documentElement.style;
    let computedStyle = getComputedStyle(
      this.document.getElementById('colors')
    );

    style.setProperty(
      '--primary-default',
      computedStyle.getPropertyValue('--theme-primary-default')
    );
    style.setProperty(
      '--accent-default',
      computedStyle.getPropertyValue('--theme-accent-default')
    );
    style.setProperty(
      '--accent-lighter',
      computedStyle.getPropertyValue('--theme-accent-lighter')
    );
    style.setProperty(
      '--accent-darker',
      computedStyle.getPropertyValue('--theme-accent-darker')
    );
    style.setProperty(
      '--warn-default',
      computedStyle.getPropertyValue('--theme-warn-default')
    );
    style.setProperty(
      '--contrast-color',
      computedStyle.getPropertyValue('--theme-contrast-color')
    );
  }
}
