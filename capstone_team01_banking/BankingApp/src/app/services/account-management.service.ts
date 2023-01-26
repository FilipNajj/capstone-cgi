import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AccountType } from '@app/enum/accountType';
import { Status } from '@app/enum/status';
import { Transaction } from '@app/enum/transaction';
import { Account } from '@app/interfaces/account';
import { CreditCard } from '@app/interfaces/creditCard';
import { Observable, Subject, tap } from 'rxjs';
import { ClientService } from './client.service';
import { RouterService } from './router.service';

const URL_ACCOUNT_MANAGEMENT = 'http://localhost:9005/api/v1/accounts';
const URL_TRANSACTION = 'http://localhost:9008/api/v1/transaction';


@Injectable({
  providedIn: 'root'
})
export class AccountManagementService {

  accounts: Account[] = []
  accountsSub: Subject<Account[]> = new Subject<Account[]>();

  creditCards: CreditCard[] = []

  transactions: Transaction[] = [];

  currentAccount: Account;

  currentCreditCard: CreditCard;

  isCreditCard: boolean = false;
  private headers: HttpHeaders;

  constructor(
    private httpClient: HttpClient,
    private clientService: ClientService,
    private _snackBar: MatSnackBar,
    private routerService: RouterService
  ) {
    this.headers = new HttpHeaders({});
    if (this.accounts.length > 0) {
      this.currentAccount = this.accounts[0];
    }
    this.accountsSub.subscribe((value) => {
      this.accounts = value;
    })
  }

  addAccount(account: Account): Observable<Account> {
    account.profileId = this.clientService.clientId;
    return this.httpClient.post<any>(URL_ACCOUNT_MANAGEMENT, account).pipe(
      tap((newAccount) => {
        this.accounts.push(account);
      })
    );
  }

  getAllAccounts(): void {
    this.httpClient.get<Account[]>(URL_ACCOUNT_MANAGEMENT).subscribe((accounts) => {
      this.accounts = accounts;
    })
  }

  getAllAccountForUser(): void {
    const id = this.clientService.clientId;
    this.httpClient.get<Account[]>(`${URL_ACCOUNT_MANAGEMENT}/accounts-profile/${id}`, {
      headers: this.headers
    }).subscribe((accounts) => {
      this.accounts = accounts;
    })
  }

  getAccountId(id: number): void {
    this.httpClient.get<Account>(`${URL_ACCOUNT_MANAGEMENT}/${id}`).subscribe((account) => {
      this.currentAccount = account;
    })
  }

  putAccountId(account: Account): Observable<Account> {
    return this.httpClient.put<Account>(`${URL_ACCOUNT_MANAGEMENT}/${account.accountNumber}`, account).pipe(
      tap((newAccount) => {
        const index = this.accounts.findIndex(v => v.accountNumber === account.accountNumber);
        this.accounts.splice(index, 1);
        this.accounts.push(account);
      })
    );
  }

  deleteAccountId(id: number): void {
    this.httpClient.delete<Account>(`${URL_ACCOUNT_MANAGEMENT}/${id}`).subscribe((account) => {
      const index = this.accounts.findIndex(v => v.accountNumber === id);
      this.accounts.splice(index, 1);
      this.currentAccount = account;
    })
  }

  getAccountTransactions(): Observable<any> {
    return this.httpClient.get<any>(`${URL_TRANSACTION}/account/${this.currentAccount.accountNumber}/${this.clientService.clientId}`);
  }

  makeTransaction(data: any): void {
    this.httpClient.post<any>(`${URL_TRANSACTION}/${this.clientService.clientId}`, data).subscribe((res) => {
      this._snackBar.open('Success', 'Close', {
        duration: 5 * 1000,
      })
      this.routerService.routeToAccounts();
    },
      (err) => {
        if (err.status === 409) {
          this._snackBar.open(err.error.message, 'Close', {
            duration: 5 * 1000,
          })
          this.routerService.routeToAccounts();
        }
      });
  }
}
