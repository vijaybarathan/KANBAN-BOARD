import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RegistrationComponent } from './registration/registration.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavigationComponent } from './navigation/navigation.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { LoginComponent } from './login/login.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatCardModule} from '@angular/material/card';
import { HttpClientModule } from '@angular/common/http';
import { NavigationHomeComponent } from './navigation-home/navigation-home.component';
import {MatMenuModule} from '@angular/material/menu';
import { CreateBoardComponent } from './create-board/create-board.component';
import { NavigationBoardComponent } from './navigation-board/navigation-board.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import {MatStepperModule} from '@angular/material/stepper';
import {MatSelectModule} from '@angular/material/select';
import { BoardComponent } from './board/board.component';
import {MatDialogModule} from '@angular/material/dialog';
import { EditBoardComponent } from './edit-board/edit-board.component';
import { AddTaskComponent } from './add-task/add-task.component';
import {MatRadioModule} from '@angular/material/radio';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { EditTaskComponent } from './edit-task/edit-task.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {MatBadgeModule} from '@angular/material/badge';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LandingViewComponent } from './landing-view/landing-view.component';
import { FooterComponent } from './footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    RegistrationComponent,
    NavigationComponent,
    LoginComponent,
    NavigationHomeComponent,
    CreateBoardComponent,
    NavigationBoardComponent,
    EditProfileComponent,
    BoardComponent,
    EditBoardComponent,
    AddTaskComponent,
    EditTaskComponent,
    PageNotFoundComponent,
    LandingViewComponent,
    FooterComponent
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatToolbarModule,
    DragDropModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatSnackBarModule,
    HttpClientModule,
    MatMenuModule,
    MatStepperModule,
    MatSelectModule,
    MatDialogModule,
    MatRadioModule,
   MatDatepickerModule,
   MatNativeDateModule,
   MatButtonToggleModule,
   DragDropModule,
   MatBadgeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
