import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { CreateBoardComponent } from './create-board/create-board.component';
import { RegistrationComponent } from './registration/registration.component'; // Import your Registration component

@Injectable({
  providedIn: 'root'
})
export class CanDeactivateGuard implements CanDeactivate<EditProfileComponent | CreateBoardComponent | RegistrationComponent> {
  canDeactivate(
    component: EditProfileComponent | CreateBoardComponent | RegistrationComponent
  ): boolean {
    if (component instanceof EditProfileComponent && component.editForm.dirty) {
      return confirm('Are you sure you want to leave this page? Your changes will be lost.');
    } else if (component instanceof RegistrationComponent && component.registerForm.dirty) {
      return confirm('Are you sure you want to leave this page? Your changes will be lost.');
    }
    return true;
  }
}
