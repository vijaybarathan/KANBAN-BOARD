import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenServiceService } from './token-service.service';
import { USER } from '../Models/User';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private tokenService: TokenServiceService) {   
    this.isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  }

  // Flag to track the login status
  isLoggedIn: boolean = false;

  url:string ="http://localhost:8083/api/v1/login"

  login(form: any): Observable<any> {
    // Assuming login success; set isLoggedIn to true and store in localStorage
    this.isLoggedIn = true;
    localStorage.setItem('isLoggedIn', 'true');
    return this.http.post(this.url, form);
  }

  getUserDetails():Observable<any>{
    const tok =this.tokenService.getHeaders();
    const options = { headers: tok };
    return this.http.get<USER>("http://localhost:8087/api/v2/user/getuser",options);
  }

  logout() {
    // Clear login status and remove from localStorage
    this.isLoggedIn = false;
    localStorage.removeItem('isLoggedIn');
    this.tokenService.clearToken();
  }
}
