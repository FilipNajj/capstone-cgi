import { User } from '@app/interfaces/user';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '@app/services/authentication.service';
import { RouterService } from '@app/services/router.service';
import { Roles } from '@app/enum/roles';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  currentUser: User | undefined;
  isAuthenticated: boolean = false;


  constructor(
    protected authService: AuthenticationService,
    private routerService: RouterService
  ) { }

  ngOnInit(): void {
    this.authService.authenticate().subscribe(user => {
      if (user) {
        this.isAuthenticated = true;
        this.currentUser = user;
        console.log(user);
      }
    });
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }

  goToDashboard(): void {
    const roles = this.authService.currentUser?.roles;
    const isClient = roles?.find(role => role === 'client') !== undefined;
    const isAdmin = roles?.find(role => role === 'admin') !== undefined;
    const isCreditCard = roles?.find(role => role === 'creditCardCompany') !== undefined;

    if (this.authService.currentUser && isClient) {
      this.routerService.routeToUserMainPage(Roles.CLIENT);
    }
    if (this.authService.currentUser && isAdmin) {
      this.authService.isRedirectedToLayout = true;
      this.routerService.routeToUserMainPage(Roles.ADMIN);
    }
    if (this.authService.currentUser && isCreditCard) {
      this.authService.isRedirectedToLayout = true;
      this.routerService.routeToUserMainPage(Roles.CCC);
    }
  }

}
