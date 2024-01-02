import { Component, EventEmitter, Output } from '@angular/core';
import { RouteServiceService } from '../services/route-service.service';
import { AuthService } from '../services/auth.service';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TokenServiceService } from '../services/token-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  // EventEmitter to emit login status
  @Output() isLoggedIn: EventEmitter<boolean> = new EventEmitter<boolean>();

  showPassword: boolean = false;
  errMessage: boolean = false;

  constructor(private authService: AuthService, private tokenService: TokenServiceService, private routerService: RouteServiceService, private fb: FormBuilder, private snackbar: MatSnackBar) { }

  // Toggle password visibility
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  loginForm = this.fb.group({ email: ['', Validators.required], password: ['', Validators.required] })

  // Handle login button click
  handleLogin(): void {

    console.log(this.loginForm.value);

    // Call AuthService to perform login
    this.authService.login(this.loginForm.value).subscribe({
      next: data => {
        console.log(data);
        this.snackbar.open('You Have LoggedIn Successfully ', 'success', {
          duration: 5000,
          panelClass: ['mat-toolbar', 'mat-primary']
        })
        this.isLoggedIn.emit(true);
        this.authService.isLoggedIn=true;
        this.routerService.navigateToNavigationView();
        this.tokenService.saveToken(data);
        
      },
      error: err => {
        console.log(err);
        if (err.status == 404) {
          this.snackbar.open('Invalid credentials..!!!', 'Failure', {
            duration: 3000,
            panelClass: ['mat-toolbar', 'mat-warn']
          });
          this.errMessage = true;
        }
        else {
          this.snackbar.open('Network Error..!!!', 'Failure', {
            duration: 3000,
            panelClass: ['mat-toolbar', 'mat-warn']
          });
        }
      }
    })
  }
}
