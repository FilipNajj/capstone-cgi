import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarComponent } from '@app/components/atoms/snack-bar/snack-bar.component';
import { AccountManagementService } from '@app/services/account-management.service';
import { ProductsService } from '@app/services/products.service';
import { RouterService } from '@app/services/router.service';
import { Item } from '../../transfer/transfer.component';

@Component({
  selector: 'app-add-account',
  templateUrl: './add-account.component.html',
  styleUrls: ['./add-account.component.scss']
})
export class AddAccountComponent {
  productList: Item[] = [];
  public addAccountForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    protected productsService: ProductsService,
    private accountManagementService: AccountManagementService,
    private routerService: RouterService,
    private _snackBar: MatSnackBar
  ) {
    for (let i = 0; i < this.productsService.productsAxle.length; i += 1) {
      this.productList.push({ label: this.productsService.productsAxle[i].accountType, value: this.productsService.productsAxle[i] })
    }
    this.createAddAccountForm();
  }

  private createAddAccountForm(): void {
    this.addAccountForm = this.formBuilder.group({
      accounts: new FormControl([], Validators.required)
    })
  }

  submit(): void {
    for (let account of this.addAccountForm.controls['accounts']?.value) {
      this.accountManagementService.addAccount(account).subscribe((res) => {
        if (res) {
          this._snackBar.open('Success', 'Close', {
            duration: 5 * 1000,
          })
          this.routerService.routeToAccounts();
        } else {
          this._snackBar.open('Not added', 'Close', {
            duration: 5 * 1000,
          })
        }
      })
    }
  }

}
