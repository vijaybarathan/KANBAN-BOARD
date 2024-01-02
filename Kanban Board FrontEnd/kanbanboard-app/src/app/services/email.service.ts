import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMAIL } from '../Models/email';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private HTTP: HttpClient) { }
  url:string="http://localhost:8082/api/v4/sendMail";

  postemail(form:EMAIL):Observable<EMAIL>
  {
    return this.HTTP.post<EMAIL>(this.url,form);
  }
}