import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RouteServiceService {

  constructor(private router: Router) {}
 
  navigateToNavigationView(){
    this.router.navigate(['/home']);
  }

  navigateToLoginView(){
    this.router.navigate(['/login'])
  }

  navigateToCreateBoard(){
    this.router.navigate(['/createboard'])
  }

  navigateToEditProfileView(){
    this.router.navigate(['/editprofile'])
  }

  navigateToBoard(id:number){
    this.router.navigate([`/board/${id}`])
  }

  navigateToHome(){
    this.router.navigate(['/home'])
  }

  navigateToLandingView(){
    this.router.navigate(['/landingview'])
  }


}
