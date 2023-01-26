import { Component, Input, OnInit } from '@angular/core';
import { Account } from '@app/interfaces/account';
import { AccountManagementService } from '@app/services/account-management.service';
import { RouterService } from '@app/services/router.service';
import { getUpperCaseWord } from '@app/utils/text';

@Component({
  selector: 'app-account-overview',
  templateUrl: './account-overview.component.html',
  styleUrls: ['./account-overview.component.scss']
})
export class AccountOverviewComponent {

  @Input('account') account: Account;

  constructor(
    private accountManagementService: AccountManagementService,
    private routerService: RouterService
  ) { }

  goToAccount(): void {
    this.accountManagementService.isCreditCard = false;
    this.accountManagementService.currentAccount = this.account;
    this.routerService.routeToAccountView();
  }

}
