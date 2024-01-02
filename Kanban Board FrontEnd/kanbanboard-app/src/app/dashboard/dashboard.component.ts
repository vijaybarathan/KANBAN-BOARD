import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { TokenServiceService } from '../services/token-service.service';
import { BOARDS } from '../Models/Board';
import { RouteServiceService } from '../services/route-service.service';
import { BoardService } from '../services/board.service';
import { MatDialog } from '@angular/material/dialog';
import { EditBoardComponent } from '../edit-board/edit-board.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  showBoard: boolean = false;

  constructor( private authService: AuthService,private router:Router,private tokenService: TokenServiceService, public dialog: MatDialog, private board: BoardService, private route: RouteServiceService) { }

  userName: string = '';

  @Output() notifications: EventEmitter<number> = new EventEmitter<number>();

  boardList: BOARDS[] = []
  ngOnInit(): void {
   
    this.authService.getUserDetails().subscribe({
      next: data => {
        this.boardList = data.kanbanBoardList;
        this.userName = data.userName.toUpperCase();
        this.showBoard = this.boardList.length > 0;
      },
      error: err => {
        console.log(err);
      }
    })
  }

  deleteBoard(id: any) {
    if (confirm('Are you sure, you want to delete board?') == true) {
      this.board.deleteBoard(id).subscribe({
        next: data => {
          console.log(data.kanbanBoardList);
          for (let i = 0; i < this.boardList.length; i++) {
            if (this.boardList[i].boardId == id) {
              this.boardList.splice(i, 1)
              this.showBoard = this.boardList.length > 0;
            }
          }
        }
      })
    }
  }

  editBoard(id: number) {
    const dialog1 = this.dialog.open(EditBoardComponent, {
      disableClose:true,
      data: {
        boardId: id
      }
    });
  
    dialog1.componentInstance.boardUpdated.subscribe((updatedData: any) => {
      // Update the boardList with the updated data emitted from the EditBoardComponent
      this.boardList = updatedData.data.kanbanBoardList;
    });
  
    dialog1.afterClosed().subscribe();
  }
  
  createboard() {
    this.route.navigateToCreateBoard();
  }

  viewBoard(id: number) {
    this.route.navigateToBoard(id);
  }
}
