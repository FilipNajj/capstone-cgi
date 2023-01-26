import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// material
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCardModule } from '@angular/material/card';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatListModule } from '@angular/material/list';


// components
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
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
import { ClientHeaderComponent } from './components/client/client-header/client-header.component';
import { ClientMenuComponent } from './components/client/client-menu/client-menu.component';
import { AdminHeaderComponent } from './components/admin/admin-header/admin-header.component';
import { ClientsComponent } from './components/admin/clients/clients.component';
import { CcHeaderComponent } from './components/credit-card-compagny/cc-header/cc-header.component';
import { ProductsComponent } from './components/credit-card-compagny/products/products.component';
import { AboutusComponent } from './components/aboutus/aboutus.component';



@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    ClientComponent,
    AdminComponent,
    CreditCardCompagnyComponent,
    RegisterComponent,
    RegisterClientComponent,
    RegisterAdminComponent,
    RegisterCCComponent,
    AccountsComponent,
    ClientHeaderComponent,
    ClientMenuComponent,
    AdminHeaderComponent,
    ClientsComponent,
    CcHeaderComponent,
    ProductsComponent,
    AboutusComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatListModule,
    MatButtonModule,
    MatSortModule,
    MatExpansionModule,
    MatCardModule,
    MatDialogModule,
    MatPaginatorModule,
    MatGridListModule,
    HttpClientModule,
    FormsModule,
    MatTableModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
