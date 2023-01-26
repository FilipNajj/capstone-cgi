import { AuthenticationService } from '@app/services/authentication.service';
import { RouterService } from './../../services/router.service';
import { Component, OnInit } from '@angular/core';
import { Roles } from '@app/enum/roles';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    isClient: boolean = false;
    isAdmin: boolean = false;
    isCreditCard: boolean = false;

    menu: any[] = [
        {
            label: 'Check out the products',
            link: '/products/axle',
            picture: 'shopping_cart'
        },
        {
            label: 'Check out the credit cards',
            link: '/products/credit-cards',
            picture: 'credit_card'
        },
        {
            label: 'Register',
            link: '/register',
            picture: 'how_to_reg'
        },
    ]

    constructor(protected authService: AuthenticationService, private routerService: RouterService) { }

    ngOnInit(): void {
        this.authService.authenticate().subscribe(user => {
            if (user) {
                const roles = user.roles;
                this.isClient = roles.find(role => role === 'client') !== undefined;
                this.isAdmin = roles.find(role => role === 'admin') !== undefined;
                this.isCreditCard = roles.find(role => role === 'creditCardCompany') !== undefined;

                if (user && this.isClient && !this.authService.isRedirectedToLayout) {
                    this.authService.isRedirectedToLayout = true;
                    this.routerService.routeToUserMainPage(Roles.CLIENT);
                }
                if (user && this.isAdmin && !this.authService.isRedirectedToLayout) {
                    this.authService.isRedirectedToLayout = true;
                    this.routerService.routeToUserMainPage(Roles.ADMIN);
                }
                if (user && this.isCreditCard && !this.authService.isRedirectedToLayout) {
                    this.authService.isRedirectedToLayout = true;
                    this.routerService.routeToUserMainPage(Roles.CCC);
                }
            }
        });
    }


}
