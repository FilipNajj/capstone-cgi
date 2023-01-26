import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from '../app-routing.module';
import { AppComponent } from '../app.component';
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
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBarModule } from '@angular/material/snack-bar';


// components
import { HeaderComponent } from '../components/header/header.component';
import { FooterComponent } from '../components/footer/footer.component';
import { HomeComponent } from '../components/home/home.component';
import { LoginComponent } from '../components/login/login.component';
import { ClientComponent } from '../components/client/client.component';
import { AdminComponent } from '../components/admin/admin.component';
import { CreditCardCompagnyComponent } from '../components/credit-card-compagny/credit-card-compagny.component';
import { RegisterComponent } from '../components/register/register.component';
import { RegisterClientComponent } from '../components/register/register-client/register-client.component';
import { RegisterAdminComponent } from '../components/register/register-admin/register-admin.component';
import { RegisterCCComponent } from '../components/register/register-cc/register-cc.component';
import { AccountsComponent } from '../components/client/accounts/accounts.component';
import { ClientHeaderComponent } from '../components/client/client-header/client-header.component';
import { AdminHeaderComponent } from '../components/admin/admin-header/admin-header.component';
import { ClientsComponent } from '../components/admin/clients/clients.component';
import { CcHeaderComponent } from '../components/credit-card-compagny/cc-header/cc-header.component';
import { ProductsComponent } from '../components/products/products.component';
import { AboutusComponent } from '../components/aboutus/aboutus.component';
import { AddProductComponent } from "@app/components/credit-card-compagny/add-product/add-product.component";
import { ProductsListComponent } from "@app/components/credit-card-compagny/products-list/products-list.component";
import { ETransferComponent } from '../components/client/e-transfer/e-transfer.component';
import { PayBillComponent } from '../components/client/pay-bill/pay-bill.component';
import { TransferComponent } from '../components/client/transfer/transfer.component';
import { DepositChequeComponent } from '../components/client/deposit-cheque/deposit-cheque.component';
import { ContactsComponent } from '../components/client/contacts/contacts.component';
import { BudgetComponent } from '../components/client/budget/budget.component';
import { ProfileComponent } from '../components/profile/profile.component';
import { CcListComponent } from '../components/admin/cc-list/cc-list.component';
import { InstitutionsComponent } from '../components/admin/institutions/institutions.component';
import { ChequeValidationListComponent } from '../components/admin/cheque-validation-list/cheque-validation-list.component';
import { AccountOverviewComponent } from '../components/client/accounts/account-overview/account-overview.component';
import { CreditCardOverviewComponent } from '../components/client/accounts/credit-card-overview/credit-card-overview.component';
import { AccountViewComponent } from '../components/client/accounts/account-view/account-view.component';
import { AccountDetailComponent } from '../components/client/accounts/account-view/account-detail/account-detail.component';
import { AccountTransactionComponent } from '../components/client/accounts/account-view/account-transaction/account-transaction.component';
import { InstitutionFormComponent } from '../components/admin/institutions/institution-form/institution-form.component';
import { ContactUsComponent } from '../components/contact-us/contact-us.component';
import { ProductsInformationComponent } from '../components/products-information/products-information.component';
import { ProductCCComponent } from '../components/products/product-cc/product-cc.component';
import { CccFormComponent } from '../components/admin/cc-list/ccc-form/ccc-form.component';
import { ClientFormComponent } from '../components/admin/clients/client-form/client-form.component';
import { AddContactComponent } from '../components/client/contacts/addcontact/addcontact.component';
import { EditContactComponent } from '../components/client/contacts/editcontact/editcontact.component';
import { AddAccountComponent } from '../components/client/accounts/add-account/add-account.component';
import { AddCreditCardComponent } from '../components/client/accounts/add-credit-card/add-credit-card.component';
import { SnackBarComponent } from '../components/atoms/snack-bar/snack-bar.component';



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
    AdminHeaderComponent,
    ClientsComponent,
    CcHeaderComponent,
    ProductsComponent,
    AboutusComponent,
    AddProductComponent,
    ETransferComponent,
    PayBillComponent,
    TransferComponent,
    DepositChequeComponent,
    ContactsComponent,
    BudgetComponent,
    ProfileComponent,
    CcListComponent,
    InstitutionsComponent,
    ChequeValidationListComponent,
    AccountOverviewComponent,
    CreditCardOverviewComponent,
    AccountViewComponent,
    AccountDetailComponent,
    AccountTransactionComponent,
    InstitutionFormComponent,
    ContactUsComponent,
    ProductsInformationComponent,
    CccFormComponent,
    ClientFormComponent,
    ProductCCComponent,
    AddAccountComponent,
    AddCreditCardComponent,
    SnackBarComponent,
    AddContactComponent,
    EditContactComponent,
    ProductsListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatFormFieldModule,
    MatSidenavModule,
    MatInputModule,
    MatSelectModule,
    MatListModule,
    MatButtonModule,
    MatCheckboxModule,
    MatSortModule,
    MatTabsModule,
    MatExpansionModule,
    MatCardModule,
    MatDialogModule,
    MatPaginatorModule,
    MatGridListModule,
    MatSnackBarModule,
    HttpClientModule,
    FormsModule,
    MatTableModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
