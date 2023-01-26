import { Component } from '@angular/core';
import { AccountType } from '@app/enum/accountType';
import { Account } from '@app/interfaces/account';
import { AccountManagementService } from '@app/services/account-management.service';

export interface Item {
  label: string;
  value: any;
  isMoney: boolean;
}

@Component({
  selector: 'app-account-detail',
  templateUrl: './account-detail.component.html',
  styleUrls: ['./account-detail.component.scss']
})
export class AccountDetailComponent {

  information: Item[];

  constructor(
    private accountManagementService: AccountManagementService,
  ) {
    if (!this.accountManagementService.isCreditCard) {
      this.createInformationArrayCard();
    } else {
      this.createInformationArrayCreditCard();
    }
  }

  private createInformationArrayCard(): void {
    this.information = [];
    this.information.push({
      label: 'Available balance',
      value: this.accountManagementService.currentAccount.balance,
      isMoney: true
    });
    this.information.push({
      label: 'Daily Purchasing Limit',
      value: this.accountManagementService.currentAccount.maxDailyPurchasingLimit,
      isMoney: true
    });
    this.information.push({
      label: 'Daily Withdraw Limit',
      value: this.accountManagementService.currentAccount.maxDailyWithdrawlLimit,
      isMoney: true
    });
    this.information.push({
      label: 'Interest rate',
      value: this.accountManagementService.currentAccount.interestRate,
      isMoney: false
    });
    this.information.push({
      label: 'Over draft',
      value: this.accountManagementService.currentAccount.overDraft,
      isMoney: true
    });
  }

  private createInformationArrayCreditCard(): void {
    this.information = [];
    this.information.push({
      label: 'Current balance',
      value: this.accountManagementService.currentCreditCard.balance,
      isMoney: true
    });
    this.information.push({
      label: 'Credit limit',
      value: this.accountManagementService.currentCreditCard.creditLimit,
      isMoney: true
    });
    this.information.push({
      label: 'Available credit',
      value: this.accountManagementService.currentCreditCard.creditLimit - this.accountManagementService.currentCreditCard.balance,
      isMoney: true
    });
    this.information.push({
      label: 'Purchase Interest Rate',
      value: this.accountManagementService.currentCreditCard.purchaseInterestRate,
      isMoney: false
    });
    this.information.push({
      label: 'Cash Advance Interest Rate',
      value: this.accountManagementService.currentCreditCard.cashAdvanceInterestRate,
      isMoney: false
    });
  }

}
