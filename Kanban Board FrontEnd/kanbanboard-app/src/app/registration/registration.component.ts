import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RegistrationService } from '../services/registration.service';
import { RouteServiceService } from '../services/route-service.service';
import { EMAIL } from '../Models/email';
import { EmailService } from '../services/email.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})

export class RegistrationComponent {

  constructor(private fb: FormBuilder, private registerService: RegistrationService, private routeService:RouteServiceService, private _snackBar: MatSnackBar,private emailservice:EmailService) { }


  registerForm = this.fb.group({
    userName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
    email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9+_.-]+@[a-zA-Z.-]+$/)]],
    password: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{7,}$')]],
    passCode1: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{7,}$')]]
  })

  emailform=this.fb.group({
    recipient:[""],
    msgBody:[""],
    subject:["Registration confirmation"]
  })

  registerUser() {
    if (this.registerForm.value.password == this.registerForm.value.passCode1) {
      console.log('hi');
      this.emailform.value.recipient=this.registerForm.value.email;
      this.emailform.value.msgBody="Thank you "+this.registerForm.value.userName+" for registering in our KanbanTool.We are happy to work with you in upcoming projects."
      const emaill:EMAIL=this.emailform.value as EMAIL;
      this.registerService.saveUser(this.registerForm.value).subscribe({
        next: data => {
          this._snackBar.open('Congrats!!You have registered Successfully!!', 'success', {
            duration: 5000,
            panelClass: ['mat-toolbar', 'mat-primary']
          })
          this.emailservice.postemail(emaill).subscribe(data=>{
            console.log("Emailformdata: "+data);
          })
          this.routeService.navigateToLoginView();
          this.registerForm.reset();
        },
        error: err => {
          console.log(err);
          if (err.status == 409) {
            this._snackBar.open('User already registered..!!!', 'Failure', {
              duration: 3000,
              panelClass: ['mat-toolbar', 'mat-warn']
            });
          }
          else {
            this._snackBar.open('Failed to register user !! Please Try Again Later', 'Failure', {
              duration: 3000,
              panelClass: ['mat-toolbar', 'mat-warn']
            });
          }
        }
      })
    }
    else {
      if (!(document.getElementById('invalidTag'))) {
        let column = document.getElementById('tag')
        let warn = document.createElement('h4')
        warn.id = 'invalidTag'
        warn.style.color = 'red'
        warn.innerText = 'Password Mismatch'
        column?.appendChild(warn)
      }
    }
  }

}
