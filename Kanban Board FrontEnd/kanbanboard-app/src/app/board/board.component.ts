import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BoardService } from '../services/board.service';
import { BOARDS } from '../Models/Board';
import { MatDialog } from '@angular/material/dialog';
import { AddTaskComponent } from '../add-task/add-task.component';
import { TASKS } from '../Models/Task';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { TaskService } from '../services/task.service';
import { EditTaskComponent } from '../edit-task/edit-task.component';
import { RouteServiceService } from '../services/route-service.service';
import { EditBoardComponent } from '../edit-board/edit-board.component';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent {

  constructor( private authService: AuthService,private router:Router,private activatedRoute: ActivatedRoute, private route: RouteServiceService, private taskService: TaskService, private board: BoardService, private matDialog: MatDialog) { }

  Board: any = {};

  startDate: string = ''
  userName: string = '';
  showBoard: boolean = false;

  boardList: BOARDS[] = []

  ngOnInit(): void {
    
    this.activatedRoute.paramMap.subscribe(data => {
      let id = data.get('id') ?? 0;
      console.log(id);
      this.board.getBoard(id).subscribe({
        next: data => {
          console.log(data);
          this.Board = data;
        }
      })
    })
  }

  deleteBoard(id: any) {
    if (confirm('Are you sure, you want to delete this board?') == true) {
      this.board.deleteBoard(id).subscribe({
        next: data => {
          console.log(data.kanbanBoardList);
          this.route.navigateToHome();
        }
      })
    }
  }

  boardAftermoving: any = {}

  moveTask(task: any, id: number) {
    const assignee = task.taskAssignee;

    // Checking if the task is moving to the "In Progress" stage
    if (id === 2) {
      // Fetch the "In Progress" stage from this.Board.kanbanStageList
      const inProgressStage = this.Board.kanbanStageList.find((stage: { stageId: any; }) => stage.stageId === 2);

      // Check if the number of tasks assigned to the same person in "In Progress" is less than 2
      if (inProgressStage && inProgressStage.kanbanTaskList.filter((t: { taskAssignee: any; }) => t.taskAssignee === assignee).length >= 2) {

        alert('More tasks cannot be added to this user while they already have a limited number of tasks in progress.');
        return;
      }
    }

    this.taskService.moveTask(task, this.Board.boardId, id).subscribe({
      next: (data) => {
        this.boardAftermoving = data;
        for (let i = 0; i < this.boardAftermoving.kanbanBoardList.length; i++) {
          if (this.Board.boardId == this.boardAftermoving.kanbanBoardList[i].boardId) {
            this.Board = this.boardAftermoving.kanbanBoardList[i];
          }
        }
      },
      error: (err) => {
        console.log(err);
        alert('More tasks cannot be added to this member while they already have a limited number of tasks in progress.');
      },
    });
  }


  deleteTask(taskId: number, stage: number) {
    if (confirm('Are you sure, you want to delete this task?') == true)
      this.taskService.deleteTask(this.Board.boardId, stage, taskId).subscribe({
        next: data => {
          this.boardAftermoving = data;
          for (let i = 0; i < this.boardAftermoving.kanbanBoardList.length; i++) {
            if (this.Board.boardId == this.boardAftermoving.kanbanBoardList[i].boardId) {
              this.Board = this.boardAftermoving.kanbanBoardList[i];
            }
          }
        },
        error: err => {
          console.log(err);

        }
      })

  }

  taskList1: TASKS[] = []

  stageId: number = 0;
  boardId: number = 0;
  addTask(id: number) {
    this.stageId = id;
    this.boardId = this.Board.boardId;
    const dialog = this.matDialog.open(AddTaskComponent, {
      disableClose:true,
      data: {
        boardId: this.boardId,
        stageId: this.stageId
      }
    })

    dialog.afterClosed().subscribe({
      next: data => {
        console.log(data.data.kanbanBoardList[1]);
        for (let i = 0; i < data.data.kanbanBoardList.length; i++) {
          if (this.boardId == data.data.kanbanBoardList[i].boardId) {
            console.log(data.data.kanbanBoardList[i]);

            this.Board = data.data.kanbanBoardList[i];
            console.log(this.Board);

          }
        }
      }
    })
  }

  taskId: number = 0

  editTask(taskId: number, stageId: number) {
    this.stageId = stageId;
    this.taskId = taskId;
    this.boardId = this.Board.boardId;
    const dialog = this.matDialog.open(EditTaskComponent, {
      disableClose:true,
      data: {
        boardId: this.boardId,
        taskId: this.taskId,
        stageId: this.stageId
      }
    })

    dialog.afterClosed().subscribe({
      next: data => {
        console.log(data.data.kanbanBoardList[1]);
        for (let i = 0; i < data.data.kanbanBoardList.length; i++) {
          if (this.boardId == data.data.kanbanBoardList[i].boardId) {
            console.log(data.data.kanbanBoardList[i]);

            this.Board = data.data.kanbanBoardList[i];
            console.log(this.Board);

          }
        }
      }
    })
  }

  onTaskDrop(event: CdkDragDrop<TASKS[]>, stageId: number) {
    const assignee = event.item.data.taskAssignee;
    const inProgressStageId = 2;

    // Checking if the task is being dropped into the "In Progress" stage
    if (stageId === 2) {
      // Fetch the "In Progress" stage from this.Board.kanbanStageList
      const inProgressStage = this.Board.kanbanStageList.find((stage: { stageId: any; }) => stage.stageId === inProgressStageId);

      // Check if the number of tasks assigned to the same person in "In Progress" is less than 2
      if (inProgressStage && inProgressStage.kanbanTaskList.filter((t: { taskAssignee: any; }) => t.taskAssignee === assignee).length >= 2) {
        // Show an alert or handle the error condition here
        alert('More tasks cannot be added to this member while they already have a limited number of tasks in progress.');
        return; // Prevent dropping the task
      }
    }

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );

      this.taskService.moveTask(event.item.data, this.Board.boardId, stageId).subscribe({
        next: (data) => {
          this.boardAftermoving = data;
          for (let i = 0; i < this.boardAftermoving.kanbanBoardList.length; i++) {
            if (this.Board.boardId == this.boardAftermoving.kanbanBoardList[i].boardId) {
              this.Board = this.boardAftermoving.kanbanBoardList[i];
            }
          }
        }
      });
    }
  }

  editBoard(id: number) {
    const dialog1 = this.matDialog.open(EditBoardComponent, {
      disableClose: true,
      data: {
        boardId: id
      }
    });
  
    // Subscription to the emitted event
    dialog1.afterClosed().subscribe((result: any) => {
      if (result && result.data) {
        // Update the Board.boardName upon successful data emission
        this.Board = result.data.kanbanBoardList.find((board: any) => board.boardId === this.Board.boardId);
      }
    });
  }

}