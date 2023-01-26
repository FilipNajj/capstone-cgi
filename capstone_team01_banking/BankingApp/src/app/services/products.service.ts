import { Injectable } from '@angular/core';
import { AccountType } from '@app/enum/accountType';
import { Status } from '@app/enum/status';
import { Product } from '@app/interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  product1: Product = {
    balance: 2000,
    maxDailyPurchasingLimit: 100,
    maxDailyWithdrawlLimit: 100,
    interestRate: 0.22,
    overDraft: 500,
    accountStatus: "ACTIVE",
    accountType: AccountType.CHECKING,
  };

  product2: Product = {
    balance: 10000,
    maxDailyPurchasingLimit: 100,
    maxDailyWithdrawlLimit: 100,
    interestRate: 0.20,
    overDraft: 500,
    accountStatus: "ACTIVE",
    accountType: AccountType.SAVINGS
  };

  productsAxle: Product[] = [
    this.product1, this.product2
  ]

  currentProductAccount: Product;
  constructor() { }
}
