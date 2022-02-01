import { Component, HostBinding, OnInit, AfterViewInit } from '@angular/core';
import { ThemingService } from 'src/app/service/theming.service';
import * as ifvisible from 'ifvisible';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, AfterViewInit {
  constructor(private themingService: ThemingService) {}
  @HostBinding('class') public cssClass: string;

  ngOnInit() {
    this.themingService.theme.subscribe((theme: string) => {
      this.cssClass = theme;
    });

    ifvisible.setIdleDuration(environment.idleTime);
  }

  ngAfterViewInit(): void {
    this.themingService.setVariablesAndRefresh();
  }
}
