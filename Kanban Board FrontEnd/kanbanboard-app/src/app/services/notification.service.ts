import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenServiceService } from './token-service.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http:HttpClient,private token:TokenServiceService) { }

  save(data:any){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    
    
    return this.http.post<Notification>('http://localhost:8085/api/v3/kanban/savemessage',data,options)
  
  }

  getNotification(){
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    
    
    return this.http.get<Notification>('http://localhost:8085/api/v3/kanban/getemailmsg',options)
  
  }
}
