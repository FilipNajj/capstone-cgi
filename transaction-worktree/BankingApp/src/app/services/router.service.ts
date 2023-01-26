import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Roles } from '@app/enum/roles';

@Injectable({
  providedIn: 'root'
})
export class RouterService {

  constructor(
    private router: Router
  ) { }

  routeToHome(): void {
    this.router.navigate(['home'])
  }

  routeToClientView(): void {
    this.router.navigate(['client'])
  }
  routeToAdminView(): void {
    this.router.navigate(['admin'])
  }
  routeToCCCView(): void {
    this.router.navigate(['creditCardCompagny'])
  }

  routeToUserMainPage(role: Roles): void {
    switch (role) {
      case Roles.ADMIN:
        this.routeToAdminView()
        break;
      case Roles.CLIENT:
        this.routeToClientView()
        break;
      case Roles.CCC:
        this.routeToCCCView()
        break;
    
      default:
        break;
    }
  }
}
