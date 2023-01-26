import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Roles } from '@app/enum/roles';
import { CreditCardProduct } from "@app/interfaces/ccc-models/CreditCardProduct";

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

  routeToUserMainPage(role: Roles): void {
    console.log(role)
    this.router.navigate([`${role}`]);
  }

  routeToRegister(role: any): void {
    this.router.navigate(['register', `${role}`]);
  }

  routeToAccountView(): void {
    this.router.navigate(['client', 'viewAccount'])
  }

  routeToCCView(): void {
    this.router.navigate(['client', 'viewCreditCard'])
  }

  routeToAccounts(): void {
    this.router.navigate(['client']);
  }

  routeToInstitutions(): void {
    this.router.navigate(['admin', 'institutions']);
  }

  routeToProducts(): void {
    this.router.navigate(['products-information', 'viewProducts'])
  }

  routeToCCCForm(): void {
    this.router.navigate(['admin', 'creditCardCompany', 'view']);
  }

  routeToCCCList(): void {
    this.router.navigate(['admin', 'creditCardCompany']);
  }

  routeToClientList(): void {
    this.router.navigate(['admin', 'client']);
  }

  routeToClientForm(): void {
    this.router.navigate(['admin', 'client', 'view']);
  }
  routeToContacts(): void {
    this.router.navigate(["client", "contacts"]);
  }
  routeToEditContact(recipientId: number): void {
    this.router.navigate(["client", "contacts", recipientId, "edit"]);
  }

  goToCCCDashboard() {
    this.router.navigate(['creditCardCompany']);
  }

  gotoEditCCCProduct(product: CreditCardProduct) {
    this.router.navigate(['creditCardCompany', 'add-product'], {
      state: { subjectProduct: product, mode: true }
    });


  }
}
