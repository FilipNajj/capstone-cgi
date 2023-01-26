import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { ClientComponent } from './components/client/client.component';
import { AdminComponent } from './components/admin/admin.component';
import { CreditCardCompagnyComponent } from './components/credit-card-compagny/credit-card-compagny.component';
import { ProductsListComponent } from "@app/components/credit-card-compagny/products-list/products-list.component";
import { RegisterComponent } from './components/register/register.component';
import { RegisterClientComponent } from './components/register/register-client/register-client.component';
import { RegisterAdminComponent } from './components/register/register-admin/register-admin.component';
import { RegisterCCComponent } from './components/register/register-cc/register-cc.component';
import { AccountsComponent } from './components/client/accounts/accounts.component';
import { ClientsComponent } from './components/admin/clients/clients.component';
import { ProductsComponent } from './components/products/products.component';
import { AboutusComponent } from './components/aboutus/aboutus.component';
import {AddProductComponent} from "@app/components/credit-card-compagny/add-product/add-product.component";
import { ETransferComponent } from './components/client/e-transfer/e-transfer.component';
import { PayBillComponent } from './components/client/pay-bill/pay-bill.component';
import { TransferComponent } from './components/client/transfer/transfer.component';
import { DepositChequeComponent } from './components/client/deposit-cheque/deposit-cheque.component';
import { ContactsComponent } from './components/client/contacts/contacts.component';
import { BudgetComponent } from './components/client/budget/budget.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CcListComponent } from './components/admin/cc-list/cc-list.component';
import { InstitutionsComponent } from './components/admin/institutions/institutions.component';
import { ChequeValidationListComponent } from './components/admin/cheque-validation-list/cheque-validation-list.component';
import { ContactUsComponent } from './components/contact-us/contact-us.component';
import { AccountViewComponent } from './components/client/accounts/account-view/account-view.component';
import { ProductsInformationComponent } from './components/products-information/products-information.component';
import { CccFormComponent } from './components/admin/cc-list/ccc-form/ccc-form.component';
import { ClientFormComponent } from './components/admin/clients/client-form/client-form.component';
import { EditContactComponent } from './components/client/contacts/editcontact/editcontact.component';
import { AddAccountComponent } from './components/client/accounts/add-account/add-account.component';
import { AddCreditCardComponent } from './components/client/accounts/add-credit-card/add-credit-card.component';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'products',
    children: [
        {
          path: 'axle',
          component: ProductsInformationComponent,
        },
        {
          path: 'credit-cards',
          component: ProductsComponent,
        }
    ]
  },

  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    children: [
      { path: '', component: RegisterComponent },
      {
        path: 'client',
        component: RegisterClientComponent
      },
      {
        path: 'admin',
        component: RegisterAdminComponent
      },
      {
        path: 'creditCardCompany',
        component: RegisterCCComponent
      },
    ]
  },
  {
    path: 'client',
    component: ClientComponent,
    children: [
      { path: 'accounts', component: AccountsComponent },
      { path: 'addAccount', component: AddAccountComponent },
      { path: 'addCreditCard', component: AddCreditCardComponent },
      { path: 'viewAccount', component: AccountViewComponent },
      { path: 'e-transfer', component: ETransferComponent },
      { path: 'pay-bill', component: PayBillComponent },
      { path: 'transfer', component: TransferComponent },
      { path: 'deposit-cheque', component: DepositChequeComponent },
      {
        path: 'contacts',
        children: [
          { path: '', component: ContactsComponent },
          { path: ':contactId/edit', component: EditContactComponent },

        ],
      },
      { path: 'profile', component: ProfileComponent },
      { path: 'budget-app', component: BudgetComponent },
      { path: '', redirectTo: 'accounts', pathMatch: 'full' },
      { path: '**', redirectTo: 'accounts', pathMatch: 'full' },
    ]
  },
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: 'clients', component: ClientsComponent },
      { path: 'client/view', component: ClientFormComponent },
      { path: 'creditCardCompany', component: CcListComponent },
      { path: 'creditCardCompany/view', component: CccFormComponent },
      { path: 'institutions', component: InstitutionsComponent },
      { path: 'cheque-validation', component: ChequeValidationListComponent },
      { path: '', redirectTo: 'clients', pathMatch: 'full' },
      { path: '**', redirectTo: 'clients', pathMatch: 'full' },
    ]
  },
  {
    path: 'creditCardCompany',
    component: CreditCardCompagnyComponent,
    children: [
      { path: 'products', component: ProductsListComponent },
      { path: 'add-product', component: AddProductComponent, pathMatch: 'full' },
      { path: '**', redirectTo: 'products', pathMatch: 'full' },
    ]
  },
  { path: 'aboutus', component: AboutusComponent },
  { path: 'contactus', component: ContactUsComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home', pathMatch: 'full' },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
