import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { ClientComponent } from './components/client/client.component';
import { AdminComponent } from './components/admin/admin.component';
import { CreditCardCompagnyComponent } from './components/credit-card-compagny/credit-card-compagny.component';
import { RegisterComponent } from './components/register/register.component';
import { RegisterClientComponent } from './components/register/register-client/register-client.component';
import { RegisterAdminComponent } from './components/register/register-admin/register-admin.component';
import { RegisterCCComponent } from './components/register/register-cc/register-cc.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { ClientsComponent } from './components/admin/clients/clients.component';
import { ProductsComponent } from './components/credit-card-compagny/products/products.component';
import { AboutusComponent } from './components/aboutus/aboutus.component';

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
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
        path: 'creditCardCompagny',
        component: RegisterCCComponent
      },
    ]
  },
  {
    path: 'client',
    component: ClientComponent,
    children: [
      { path: 'accounts', component: AccountsComponent },
      { path: '', redirectTo: 'accounts', pathMatch: 'full' },
      { path: '**', redirectTo: 'accounts', pathMatch: 'full' },
    ]
  },
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: 'clients', component: ClientsComponent },
      { path: '', redirectTo: 'clients', pathMatch: 'full' },
      { path: '**', redirectTo: 'clients', pathMatch: 'full' },
    ]
  },
  {
    path: 'creditCardCompagny',
    component: CreditCardCompagnyComponent,
    children: [
      { path: 'products', component: ProductsComponent },
      { path: '', redirectTo: 'products', pathMatch: 'full' },
      { path: '**', redirectTo: 'products', pathMatch: 'full' },
    ]
  },
  { path: 'aboutus', component: AboutusComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
