import { Injectable } from '@angular/core';
import { USER } from '../Models/User';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { BOARDS } from '../Models/Board';
import { TokenServiceService } from './token-service.service';

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  constructor(private http:HttpClient,private token:TokenServiceService) { }

  getUsers():Observable<string[]>{
    return this.http.get<string[]>("http://localhost:8083/api/v1/getuseremail")
  }

  getBoards():Observable<BOARDS[]>{
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.get<BOARDS[]>("http://localhost:8087/api/v2/user/kanbanBoards",options)
  }

  deleteBoard(id:any):Observable<USER>{
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.delete<USER>(`http://localhost:8087/api/v2/user/kanbanBoard/${id}`,options)
  
  }

  saveBoard(form:any){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.post<BOARDS>(`http://localhost:8087/api/v2/user/kanbanBoard`,form,options)
  
  }

  getBoard(id:any):Observable<BOARDS>{
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.get<BOARDS>(`http://localhost:8087/api/v2/user/kanbanBoards/${id}`,options)
  }

  updateBoard(form:any){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.put<BOARDS>(`http://localhost:8087/api/v2/user/kanbanBoard`,form,options)
  
  }
}
