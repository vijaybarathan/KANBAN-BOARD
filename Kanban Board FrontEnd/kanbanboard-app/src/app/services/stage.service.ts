import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { USER } from '../Models/User';
import { TokenServiceService } from './token-service.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StageService {

  constructor(private token:TokenServiceService,private http:HttpClient) { }

  saveStages(stage:any,boardId:number):Observable<USER>{
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.post<USER>(`http://localhost:8087/api/v2/user/stage/addStages/${boardId}`,stage,options)
  
  }
}
