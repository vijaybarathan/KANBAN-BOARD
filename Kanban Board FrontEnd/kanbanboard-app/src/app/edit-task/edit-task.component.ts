import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BoardComponent } from '../board/board.component';
import { FormBuilder } from '@angular/forms';
import { TaskService } from '../services/task.service';
import { TASKS } from '../Models/Task';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrls: ['./edit-task.component.css']
})
export class EditTaskComponent {
  constructor(public dialogRef: MatDialogRef<BoardComponent>,private notification:NotificationService, @Inject(MAT_DIALOG_DATA) public data: BoardComponent, private fb: FormBuilder, private taskService: TaskService) { }

  task: any = {}
  ngOnInit(): void {
    console.log(this.data);
    this.taskService.getTask(this.data.boardId, this.data.stageId, this.data.taskId).subscribe({
      next: data => {
        this.task = data
        this.taskform = {
          taskId: this.task.taskId,
          taskTitle: this.task.taskTitle,
          taskDescription: this.task.taskDescription,
          taskPriority: this.task.taskPriority,
          startDate: this.task.startDate.substring(0, 10),
          taskDeadline: this.task.taskDeadline.substring(0, 10),
          taskAssignee: this.task.taskAssignee,
          taskType: this.task.taskType
        }

      }
    })
  }

  taskform: TASKS = {
    taskTitle: "",
    taskDescription: "",
    taskPriority: "",
    startDate: new Date(),
    taskDeadline: new Date(),
    taskAssignee: "",
    taskId: 0,
    taskType: ''
  }

  savedTasks: any = {}

  updateOnClose: boolean = true; // Default to true to allow form submission
  msgi: number = 0;
  onSubmit(editTaskForm: any) {
    if (this.updateOnClose) {
      console.log(editTaskForm.value);
      this.taskService.editTask(editTaskForm.value, this.data.boardId, this.data.stageId, this.data.taskId).subscribe({
        next: data2 => {
          console.log(data2);
          this.savedTasks = data2;
          this.msgi = Math.floor(Math.random() * 1000);
          let msg = {
            msgId: this.msgi,
            message: "Task is updated"
          }
          this.notification.save(msg).subscribe({
            next: data => {
              console.log(data);

            }
          })
          this.dialogRef.close({ data: this.savedTasks })

        }
      });
    }

  }
  close() {
    this.updateOnClose = false; // Set the flag to prevent form submission
    this.dialogRef.close(); // Close the dialog box
  }
}
