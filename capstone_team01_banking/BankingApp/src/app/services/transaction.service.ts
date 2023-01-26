import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Transaction } from "@app/interfaces/transaction";
import { BehaviorSubject, Observable, of, tap } from "rxjs";
import { ClientService } from "./client.service";


@Injectable({
    providedIn: 'root'
})
export class TransactionService {

    transactions: Array<Transaction> = []
    transactionsSubject: BehaviorSubject<Transaction[]> = new BehaviorSubject<Transaction[]>([]);
    currentClientId: number;

    constructor(private clientService: ClientService, private httpClient: HttpClient) {
        this.currentClientId = this.clientService.clientId;
        this.httpClient.get<Array<Transaction>>("http://localhost:9008/api/v1/transaction/" + this.currentClientId).subscribe(
            (transactions) => {
                this.transactions = transactions;
                this.transactionsSubject.next(this.transactions);
            }
        )
    }


    getTransactionByClientId(): BehaviorSubject<Array<Transaction>> {
        return this.transactionsSubject;
    }

}
