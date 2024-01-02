import { Component, Input, inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AsyncPipe, NgIf } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { RouteServiceService } from '../services/route-service.service';
import { TokenServiceService } from '../services/token-service.service';
import { BoardService } from '../services/board.service';
import { BOARDS } from '../Models/Board';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-navigation-board',
  templateUrl: './navigation-board.component.html',
  styleUrls: ['./navigation-board.component.css']
})
export class NavigationBoardComponent {
  private breakpointObserver = inject(BreakpointObserver);

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private tokenService: TokenServiceService,private notification:NotificationService, private board: BoardService, private authService: AuthService, private route: RouteServiceService) { }

  hidden = false;
  Notifications:any=[]
  userName: String = '';
  Boards: BOARDS[] = [];
  dueTasks: number = 0;
  date: Date = new Date()
  ngOnInit(): void {
    this.authService.getUserDetails().subscribe({
      next: data => {
        console.log(data.userName);
        let num = 0
        this.userName = data.userName;
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
          next:data=>{
            console.log(data);
            this.Notifications=data
            
          }
        })
      },
      error: err => {
        console.log(err);
      }
    })
    this.board.getBoards().subscribe({
      next: data => {
        this.Boards = data;
      }
    })
  }
  
  editprofile() {
    this.route.navigateToEditProfileView();
  }

  logout() {
    this.authService.logout();
    this.route.navigateToLoginView();
  }

  navigateTo(boardId: number) {
    this.route.navigateToBoard(boardId)
  }

  home() {
    this.route.navigateToNavigationView();
  }

  toggleBadgeVisibility() {
    this.hidden = true;
  }
}
