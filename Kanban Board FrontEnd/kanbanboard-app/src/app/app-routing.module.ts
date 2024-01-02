import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { NavigationComponent } from './navigation/navigation.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CreateBoardComponent } from './create-board/create-board.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { BoardComponent } from './board/board.component';
import { authGuard } from './services/auth.guard';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LandingViewComponent } from './landing-view/landing-view.component';
import { CanDeactivateGuard } from './deactive.guard';


const routes: Routes = [
  { path: "", redirectTo: "landingview", pathMatch: 'full' },
  { path: "login", component: LoginComponent },
  { path: "register", component: RegistrationComponent, canDeactivate: [CanDeactivateGuard] },
  { path: "home", component: DashboardComponent, canActivate: [authGuard] },
  { path: "createboard", component: CreateBoardComponent, canActivate: [authGuard] },
  { path: "editprofile", component: EditProfileComponent, canActivate: [authGuard], canDeactivate: [CanDeactivateGuard] },
  { path: "board/:id", component: BoardComponent, canActivate: [authGuard] },
  { path: "landingview", component: LandingViewComponent },

  // Wildcard route for handling 404 errors
  { path: "**", component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
