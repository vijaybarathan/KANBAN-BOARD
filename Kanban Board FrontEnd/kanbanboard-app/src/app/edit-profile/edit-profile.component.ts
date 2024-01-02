import { FormBuilder, Validators } from '@angular/forms';
import { RegistrationService } from '../services/registration.service';
import { RouteServiceService } from '../services/route-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent {
  constructor(
    private router: Router,
    private fb: FormBuilder,
    private registerService: RegistrationService,
    private routeService: RouteServiceService,
    private _snackBar: MatSnackBar
  ) {}

  editForm = this.fb.group({
    userName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+$/)]],
    password: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&].{7,}$')]]
  });

  updateProfile() {
    this.registerService.updateUser(this.editForm.value).subscribe({
      next: data => {
        console.log('Server Response:', data);
        this._snackBar.open(
          'Congrats!! Your Profile Updated Successfully!!',
          'success',
          {
            duration: 5000,
            panelClass: ['mat-toolbar', 'mat-primary']
          }
        );
        this.routeService.navigateToLoginView();
        this.editForm.reset();
      },
      error: err => {
        console.log('Error:', err);
        this._snackBar.open(
          'Failed to update user !! Please Try Again Later',
          'Failure',
          {
            duration: 3000,
            panelClass: ['mat-toolbar', 'mat-warn']
          }
        );
      }
    });
  }  

  goToHome() {
    this.router.navigate(['/home']); 
  }

  close(){
    
  }
}
