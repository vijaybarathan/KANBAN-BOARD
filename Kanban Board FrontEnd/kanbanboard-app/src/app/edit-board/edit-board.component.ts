import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { BOARDS } from '../Models/Board';
import { BoardComponent } from '../board/board.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { BoardService } from '../services/board.service';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-edit-board',
  templateUrl: './edit-board.component.html',
  styleUrls: ['./edit-board.component.css']
})
export class EditBoardComponent {

  constructor(public dialogRef: MatDialogRef<DashboardComponent>, private notification: NotificationService, @Inject(MAT_DIALOG_DATA) public data: BoardComponent, private fb: FormBuilder, private boardService: BoardService) { }

  @Output() boardUpdated: EventEmitter<any> = new EventEmitter<any>();

  email: string[] = []
  board: any = {}
  ngOnInit(): void {
    console.log(this.data);
    this.boardService.getBoard(this.data.boardId).subscribe({
      next: data => {
        this.board = data
        this.boardForm = {
          boardId: this.board.boardId,
          boardName: this.board.boardName,
          boardDescription: this.board.boardDescription,
          boardMembers: this.board.boardMembers,
          boardCreatedOn: this.board.boardCreatedOn,
          kanbanStageList: this.board.kanbanStageList,
          boardOwnerName: this.board.boardOwnerName
        }

      }
    })

    this.boardService.getUsers().subscribe({
      next: data1 => {

        this.email = data1;
        console.log(this.email);

      }
    })
  }

  boardForm: BOARDS = {
    boardId: 0,
    boardName: '',
    boardOwnerName: '',
    boardDescription: '',
    boardCreatedOn: '',
    boardMembers: [],
    kanbanStageList: []
  }

  savedBoards: any = {}

  updateOnClose: boolean = true; // Default to true to allow form submission

  msgi:number=0;

  onSubmit(form: any) {
    if (this.updateOnClose) {
      // Update the board and get the updated list
      this.boardService.updateBoard(this.boardForm).subscribe({
        next: data => {
          this.savedBoards = data;
          // Emit the updated board list data to the parent component
          this.boardUpdated.emit({ data: this.savedBoards });
          this.msgi = Math.floor(Math.random() * 1000);
          let msg = {
            msgId: this.msgi,
            message: "Board is edited successfully.."
          }
          this.notification.save(msg).subscribe({
            next: data => {
              console.log(data);
            }
          })
          this.dialogRef.close({ data: this.savedBoards });
        }
      });
    }
  }

  close() {
    this.updateOnClose = false;
    this.dialogRef.close();
  }

}
