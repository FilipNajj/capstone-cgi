import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '@app/interfaces/user';
import { Roles } from '@app/enum/roles';
import { RouterService } from './router.service';

// const URL = 'http://localhost:3000/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

    isRedirectedToLayout : boolean = false;
    currentUser: User | undefined;
//   role: Roles;


  constructor(
    private httpClient: HttpClient,
    private routerService: RouterService
  ) {
    this.authenticate().subscribe( user =>{
        if (user) {
            this.currentUser = user;
        }
    }
    )
  }

  authenticate(): Observable<User> {
    return this.httpClient.get<User>('/api/v1/auth/user');
  }



//   setUser(user: User) {
//     this.currentUser = user;
//   }

//   getUser(): User {
//     return this.currentUser;
//   }

  login(): void {
    window.open('/oauth2/authorization/keycloak', '_self');
    // this.role = Roles.CCC;
    // this.isAuthenticated = true;
    // this.routerService.routeToUserMainPage(this.role);
    // return 'successful';

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

  logout(): Observable<any> {
    const formData: any = new FormData();
    this.isRedirectedToLayout = false;

    return this.httpClient.post<any>('/logout', formData);
    // this.isAuthenticated = false;
    // this.routerService.routeToHome();
  }

  setBearerToken(token: any) {
    localStorage.setItem('bearerToken', token);
  }

  getBearerToken() {
    return localStorage.getItem('bearerToken');
  }

  register(data: any): void {
    // endpoint user
    // endopoint auth
    delete data.confirmPassword;
    const authObject = (({ firstName, lastName, companyName, email, password, address, phoneNumber, role }) => ({ firstName, lastName, companyName, email, password, address, phoneNumber, role }))(data);
    console.log(authObject);
    this.httpClient.post<any>("/api/v1/auth/register", authObject).subscribe(
        (res) => this.routerService.routeToHome() ,
        // need to add some processing for if the registration didn't work
      (err) => this.routerService.routeToHome()
    );
    // TODO: backend

  }

}
