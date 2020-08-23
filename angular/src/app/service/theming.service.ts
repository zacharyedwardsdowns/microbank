import { Injectable, ApplicationRef, Inject } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class ThemingService {
  private themes = ['dark-theme', 'light-theme']; // <- list all themes in this array
  public theme = new BehaviorSubject('light-theme'); // <- initial theme

  constructor(
    private ref: ApplicationRef,
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
    window.matchMedia('(prefers-color-scheme: dark)').addListener((e) => {
      const turnOn = e.matches;
      this.theme.next(turnOn ? 'dark-theme' : 'light-theme');

      // Trigger UI refresh, set color variables, trigger another UI refresh.
      this.ref.tick();
      this.setVariablesAndRefresh();
    });
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
      '--text-color',
      computedStyle.getPropertyValue('--theme-text-color')
    );
  }
}
