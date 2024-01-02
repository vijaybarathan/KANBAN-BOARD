import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PROFILE } from '../Models/profile';
import { TokenServiceService } from './token-service.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient,private token:TokenServiceService) { }

  url:string="http://localhost:8087/api/v2/register";

  updateUrl:string="http://localhost:8087/api/v2/user/update";

  saveUser(form:any):Observable<any>{
    return this.http.post(this.url,form)
  }

  updateUser(formData: any): Observable<any>{
    const tok =this.token.getHeaders();
    const options = { headers: tok };
    return this.http.put(this.updateUrl, formData,options);
  }
}
