import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Transaction } from '@app/interfaces/transaction';
import { TransactionService } from '@app/services/transaction.service';

@Component({
  selector: 'app-cheque-validation-list',
  templateUrl: './cheque-validation-list.component.html',
  styleUrls: ['./cheque-validation-list.component.scss']
})
export class ChequeValidationListComponent implements OnInit {

  displayedColumns = ["clientId", "tarnsactionId", "amount", "toAccountNumber", "adminId", "createdBy"];
  transactions = new MatTableDataSource<Transaction>();
  @ViewChild(MatTable) table: MatTable<Transaction>;


  constructor(private transactionService:TransactionService) { 
    this.transactionService.getTransactionByClientId().subscribe({
      next:(transactions)=>{
        this.transactions = new MatTableDataSource<Transaction>(transactions.filter(t=>t.transactionType === "DEPOSIT" && t.transactionMedium === "CHEQUE"))
      }
    })
  }

  ngOnInit(): void {
  }

}
