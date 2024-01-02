import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenServiceService {

  constructor() { }

  saveToken(data:any){
    return localStorage.setItem('token',data.token);
  }

  getToken(){
    return localStorage.getItem('token');
  }

  getHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  clearToken(){
    localStorage.removeItem('token');
  }
}
