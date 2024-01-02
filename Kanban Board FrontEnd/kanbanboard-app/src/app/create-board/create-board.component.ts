import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { BoardService } from '../services/board.service';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { MatFormField } from '@angular/material/form-field';
import { AuthService } from '../services/auth.service';
import { StageService } from '../services/stage.service';
import { RouteServiceService } from '../services/route-service.service';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-create-board',
  templateUrl: './create-board.component.html',
  styleUrls: ['./create-board.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS,
    useValue: { showError: true },
  }]
})
export class CreateBoardComponent {

  constructor(private fb: FormBuilder, private notification:NotificationService, private routeService: RouteServiceService, private stageService: StageService, private board: BoardService, private authService: AuthService) { }

  firstFormGroup = this.fb.group({
    boardId: [0,],
    boardName: ['', Validators.required],
    boardDescription: ['', Validators.required],
    boardMembers: [[],],
    boardCreatedOn: [new Date().toLocaleDateString()],
    boardOwnerName: [''],
  });
  secondFormGroup = this.fb.group({

    stageName1: [{ value: 'Todo', disabled: true }, Validators.required],
    stageName2: [{ value: 'In Progress', disabled: true }, Validators.required],
    stageName3: [{ value: 'Completed', disabled: true }, Validators.required],
    stageName4: [{ value: '', disabled: true }, Validators.required],
    stageName5: [{ value: '', disabled: true }, Validators.required]
  });

  email: string[] = [];
  userName: string = '';

  ngOnInit(): void {
    this.board.getUsers().subscribe({
      next: data => {
        this.email = data;
      }
    })
    this.authService.getUserDetails().subscribe({
      next: data => {
        console.log(data.userName);
        this.userName = data.userName;
      },
      error: err => {
        console.log(err);
      }
    })
  }
@Output() dueTasks:EventEmitter<number>=new EventEmitter<number>();
  addBoxes(value: any) {
    if (value.value == 3) {
      this.secondFormGroup.controls["stageName1"].enable();
      this.secondFormGroup.controls["stageName2"].enable();
      this.secondFormGroup.controls["stageName3"].enable();
      this.secondFormGroup.controls["stageName4"].disable();
      this.secondFormGroup.controls["stageName5"].disable();
    }
    if (value.value == 4) {
      this.secondFormGroup.controls["stageName1"].enable();
      this.secondFormGroup.controls["stageName2"].enable();
      this.secondFormGroup.controls["stageName3"].enable();
      this.secondFormGroup.controls["stageName4"].enable();
      this.secondFormGroup.controls["stageName5"].disable();
    }
    if (value.value == 5) {
      this.secondFormGroup.controls["stageName1"].enable();
      this.secondFormGroup.controls["stageName2"].enable();
      this.secondFormGroup.controls["stageName3"].enable();
      this.secondFormGroup.controls["stageName4"].enable();
      this.secondFormGroup.controls["stageName5"].enable();
    }
  }

  msgi:number=0;
  add() {
    this.firstFormGroup.value.boardOwnerName = this.userName;
    this.firstFormGroup.value.boardId = Math.floor(Math.random() * 10000);
    this.board.saveBoard(this.firstFormGroup.value).subscribe({
      next: data => {
        this.stageService.saveStages({ stageId: 1, stageName: this.secondFormGroup.value.stageName1,kanbanTaskList:[] }, data.boardId).subscribe({
          next: data1 => {
            this.stageService.saveStages({ stageId: 2, stageName: this.secondFormGroup.value.stageName2 ,kanbanTaskList:[]}, data.boardId).subscribe({
              next: data1 => {
                this.stageService.saveStages({ stageId: 3, stageName: this.secondFormGroup.value.stageName3,kanbanTaskList:[] }, data.boardId).subscribe({
                  next: data1 => {
                    if (this.secondFormGroup.value.stageName4 != null) {
                      this.stageService.saveStages({ stageId: 4, stageName: this.secondFormGroup.value.stageName4,kanbanTaskList:[] }, data.boardId).subscribe({
                        next: data1 => {
                          if (this.secondFormGroup.value.stageName5 != null) {
                            this.stageService.saveStages({ stageId: 5, stageName: this.secondFormGroup.value.stageName5,kanbanTaskList:[] }, data.boardId).subscribe({
                              next: data1 => {
                                this.routeService.navigateToBoard(data.boardId);
                                this.msgi=Math.floor(Math.random() * 1000);
                                let msg={
                                  msgId:this.msgi,
                                  message:"New Board is saved"
                                }
                                this.notification.save(msg).subscribe({
                                  next:data=>{
                                    console.log(data);
                                    
                                  }
                                })
                              }
                            })
                          }
                          else if (this.secondFormGroup.value.stageName5 == null) {
                            this.routeService.navigateToBoard(data.boardId);
                            this.msgi=Math.floor(Math.random() * 1000);
                            let msg={
                              msgId:this.msgi,
                              message:"New Board is saved"
                            }
                            this.notification.save(msg).subscribe({
                              next:data=>{
                                console.log(data);
                                
                              }
                            })
                          }
                        }
                      })
                    }
                    else if (this.secondFormGroup.value.stageName4 == null) {
                      this.routeService.navigateToBoard(data.boardId);
                      this.msgi=Math.floor(Math.random() * 1000);
                      let msg={
                        msgId:this.msgi,
                        message:"New Board is saved"
                      }
                      this.notification.save(msg).subscribe({
                        next:data=>{
                          console.log(data);
                          this.dueTasks.emit(1);
                        }
                      })
                    }
                  }
                })
              }
            })
          }
        })

      }
    })
  }
}
