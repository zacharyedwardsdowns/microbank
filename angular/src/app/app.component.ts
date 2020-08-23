import { Component, HostBinding, OnInit } from '@angular/core';
import { ThemingService } from 'src/app/service/theming.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  constructor(private themingService: ThemingService) {}
  @HostBinding('class') public cssClass: string;

  ngOnInit() {
    this.themingService.theme.subscribe((theme: string) => {
      this.cssClass = theme;
    });
  }
}
