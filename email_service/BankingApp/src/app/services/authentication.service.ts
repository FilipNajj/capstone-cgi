import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '@app/interfaces/user';
import { Roles } from '@app/enum/roles';
import { RouterService } from './router.service';

const URL = 'http://localhost:3000/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  currentUser: User;
  isAuthenticated: boolean = false;
  role: Roles;


  constructor(
    private httpClient: HttpClient,
    private routerService: RouterService
  ) {

  }

  setUser(user: User) {
    this.currentUser = user;
  }

  getUser(): User {
    return this.currentUser;
  }

  login(data: any): string {
    this.role = Roles.CLIENT;
    this.isAuthenticated = true;
    this.routerService.routeToUserMainPage(this.role);
    return 'successful';

    //TODO: backend auth
    // this.httpClient.post<any>(`${URL}/login`, data).subscribe({
    //   next: (res) => {
    //     this.role = res.role;
    //     this.isAuthenticated = true;
    //     this.routerService.routeToUserMainPage(this.role);
    //     return 'successful'
    //   },
    //   error: (error) => {
    //     if (error.status === 403) {
    //       return 'Unauthorized';
    //     } else {
    //       return error.message;
    //     }

    //   }
    // })
    // return 'Unauthorized';
  }

  logout(): void {
    this.isAuthenticated = false;
    this.routerService.routeToHome();
  }

  setBearerToken(token: any) {
    localStorage.setItem('bearerToken', token);
  }

  getBearerToken() {
    return localStorage.getItem('bearerToken');
  }
}
