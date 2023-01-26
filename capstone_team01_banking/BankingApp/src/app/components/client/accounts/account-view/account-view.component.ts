import { Component, OnInit } from '@angular/core';
import { Status } from '@app/enum/status';
import { Account } from '@app/interfaces/account';
import { AccountManagementService } from '@app/services/account-management.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-account-view',
  templateUrl: './account-view.component.html',
  styleUrls: ['./account-view.component.scss']
})
export class AccountViewComponent {

  account: any;
  accountNumber: number;

  constructor(
    private accountManagementService: AccountManagementService,
    private routerService: RouterService
  ) {
    if (this.accountManagementService.currentAccount === undefined && this.accountManagementService.currentCreditCard === undefined) {
      this.routerService.routeToAccounts();
    }
    if (this.accountManagementService.isCreditCard) {
      this.account = this.accountManagementService.currentCreditCard;
      this.accountNumber = this.account.creditAccountNumber;
    } else {
      this.account = this.accountManagementService.currentAccount;
      this.accountNumber = this.account.accountNumber;
    }
  }

  isGreen(): boolean {
    return this.account.accountStatus === Status.ACTIVE
  }

  isRed(): boolean {
    const status = [Status.CLOSED, Status.INACTIVE, Status.SUSPENDED];
    return status.includes(this.account.accountStatus);
  }

  isYellow(): boolean {
    return this.account.accountStatus === Status.PENDING;
  }

}
