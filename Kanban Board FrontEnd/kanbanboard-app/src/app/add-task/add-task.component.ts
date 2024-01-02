import { Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BoardComponent } from '../board/board.component';
import { TaskService } from '../services/task.service';
import { BoardService } from '../services/board.service';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-add-task',
  templateUrl: './add-task.component.html',
  styleUrls: ['./add-task.component.css'],
})
export class AddTaskComponent {

  startTaskDate: string = '';

  start: Date = new Date();
  due: Date | null = null;

  constructor(public dialogRef: MatDialogRef<BoardComponent>, private notification: NotificationService, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: BoardComponent, private taskService: TaskService, private board: BoardService) { }
  taskdetails = this.fb.group({
    taskId: [0],
    taskTitle: [''],
    taskDescription: [''],
    taskAssignee: [''],
    startDate: [''],
    taskDeadline: [''],
    taskPriority: [''],
    taskType: ['']
  })

  email: string[] = [];

  ngOnInit(): void {
    this.board.getUsers().subscribe({
      next: data => {
        this.email = data;
      }
    });
    // Set a default value for taskPriority
    this.taskdetails.patchValue({
      taskPriority: 'MEDIUM'
    });

    this.due = new Date();

    this.taskdetails.get('startDate')?.valueChanges.subscribe((value) => {
      // Update due date when start date changes
      this.due = value as unknown as Date;
    });
  }

  get taskTitle() { return this.taskdetails.get("name"); }

  get taskDescription() { return this.taskdetails.get("description"); }

  get taskPriority() { return this.taskdetails.get("priority"); }

  get startDate() { return this.taskdetails.get("startDate"); }

  get taskDeadline() { return this.taskdetails.get("taskDeadline"); }

  get taskassigneeEmail() { return this.taskdetails.get("assigneeEmail"); }

  savedTasks: any = {}

  updateOnClose: boolean = true; // Default to true to allow form submission
  msgi:number=0;
  addTask() {
    if (this.updateOnClose) {
      this.taskdetails.value.taskId = Math.floor(Math.random() * 10000);
      this.taskService.saveTask(this.taskdetails.value, this.data.boardId, this.data.stageId).subscribe({
        next: data => {
          this.savedTasks = data;
          this.dialogRef.close({ data: this.savedTasks });
          this.msgi = Math.floor(Math.random() * 1000);
          let msg = {
            msgId: this.msgi,
            message: "Task is saved successfully"
          }
          this.notification.save(msg).subscribe({
            next: data => {
              console.log(data);
            }
          })
        },
        error: err => {
          alert('Cannot add more tasks to this user.');
        }
      });
    }
  }

  close() {
    this.updateOnClose = false; // Set the flag to prevent form submission
    this.dialogRef.close(); // Close the dialog box
  }
}
