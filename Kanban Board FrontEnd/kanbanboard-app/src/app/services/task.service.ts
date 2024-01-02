import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { USER } from '../Models/User';
import { TokenServiceService } from './token-service.service';
import { TASKS } from '../Models/Task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private token:TokenServiceService,private http:HttpClient) { }

  saveTask(task:any,boardId:number,stageId:number):Observable<USER>{
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.post<USER>(`http://localhost:8087/api/v2/user/task/savetask/${boardId}/${stageId}`,task,options)
  
  }

  moveTask(task:any,boardId:number,stageId:number){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.post<USER>(`http://localhost:8087/api/v2/user/task/updatedraganddrop/${boardId}/${stageId}`,task,options)
  
  }

  deleteTask(boardId:number,stageId:number,taskId:number){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.delete<USER>(`http://localhost:8087/api/v2/user/task/deletetask/${boardId}/${stageId}/${taskId}`,options)
  
  }

  getTask(boardId:number,stageId:number,taskId:number){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.get<TASKS>(`http://localhost:8087/api/v2/user/task/gettask/${boardId}/${stageId}/${taskId}`,options)
  
  }

  editTask(task:any,boardId:number,stageId:number,taskId:number){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.put<USER>(`http://localhost:8087/api/v2/user/task/updatetask/${boardId}/${stageId}/${taskId}`,task,options)
  
  }

  
}
