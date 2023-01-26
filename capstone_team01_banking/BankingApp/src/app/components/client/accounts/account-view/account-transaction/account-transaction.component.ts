import { Component } from '@angular/core';
import { Transaction } from '@app/enum/transaction';
import { AccountManagementService } from '@app/services/account-management.service';

@Component({
  selector: 'app-account-transaction',
  templateUrl: './account-transaction.component.html',
  styleUrls: ['./account-transaction.component.scss']
})
export class AccountTransactionComponent {

  transactions: Transaction[];

  constructor(
    private accountManagementService: AccountManagementService,
  ) {
    if (!this.accountManagementService.isCreditCard) {
      this.accountManagementService.getAccountTransactions().subscribe((data) => {
        this.transactions = data.transactionHistory[this.accountManagementService.currentAccount.accountNumber.toString()];
      })
    }
  }

  isAdd(transaction: Transaction): boolean {
    if (!this.accountManagementService.isCreditCard) {
      return this.accountManagementService.currentAccount.accountNumber === transaction.toAccountNumber;
    } else {
      return this.accountManagementService.currentCreditCard.creditAccountNumber === transaction.toAccountNumber;
    }
  }

  getAmount(transaction: Transaction): number {
    if (transaction.amount < 0) {
      return transaction.amount * -1;
    }
    return transaction.amount;
  }

}
