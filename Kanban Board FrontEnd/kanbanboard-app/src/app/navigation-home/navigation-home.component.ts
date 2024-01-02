import { Component, EventEmitter, Input, inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AsyncPipe, NgIf } from '@angular/common';
import { TokenServiceService } from '../services/token-service.service';
import { AuthService } from '../services/auth.service';
import { RouteServiceService } from '../services/route-service.service';
import { NotificationService } from '../services/notification.service';
import { Notification } from '../Models/Notification';

@Component({
  selector: 'app-navigation-home',
  templateUrl: './navigation-home.component.html',
  styleUrls: ['./navigation-home.component.css']
})
export class NavigationHomeComponent {
  private breakpointObserver = inject(BreakpointObserver);

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private tokenService: TokenServiceService, private notification: NotificationService, private authService: AuthService, private route: RouteServiceService) { }
  date: Date = new Date()
  userName: String = '';
  Notifications: any = []

  dueTasks: number = 0
  ngOnInit(): void {
    this.authService.getUserDetails().subscribe({
      next: data => {
        this.userName = data.userName;
        let num = 0

        console.log(data);
        for (let i = 0; i < data.kanbanBoardList.length; i++) {
          for (let j = 0; j < data.kanbanBoardList[i].kanbanStageList.length - 1; j++) {
            for (let k = 0; k < data.kanbanBoardList[i].kanbanStageList[j].kanbanTaskList.length; k++) {
              if ((parseInt(data.kanbanBoardList[i].kanbanStageList[j].kanbanTaskList[k].taskDeadline.substring(8, 10)) - parseInt(this.date.toString().substring(8, 10)) < 2) && (parseInt(data.kanbanBoardList[i].kanbanStageList[j].kanbanTaskList[k].taskDeadline.substring(8, 10)) - parseInt(this.date.toString().substring(8, 10)) >= 0)) {

                num++;
              }
            }
          }
        }
        this.dueTasks = num;

        this.notification.getNotification().subscribe({
          next: data => {
            console.log(data);
            this.Notifications = data
          }
        })
      },
      error: err => {
        console.log(err);
      }
    })
  }


  hidden = false;

  toggleBadgeVisibility() {
    this.hidden = true;
  }

  editprofile() {
    this.route.navigateToEditProfileView();
  }

  logout() {
    this.authService.logout();
    this.route.navigateToLandingView();
  }

  home() {
    this.route.navigateToNavigationView();
  }
}
