import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { AccountManagementService } from '@app/services/account-management.service';
import { ClientService } from '@app/services/client.service';

export interface Item {
  label: string;
  value: any;
  medium?: string;
}

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent implements OnInit {

  public transferForm: FormGroup;

  accounts: Item[] = [
    {
      label: "", value: "", medium: ""
    }
  ]
  accountsFrom: Item[] = []
  accountsTo: Item[] = []

  constructor(
    private formBuilder: FormBuilder,
    private accountManagementService: AccountManagementService,
    private clientService: ClientService
  ) {
    for (let account of this.accountManagementService.accounts) {
      this.accounts.push({
        label: `${account.accountType} : ${account.accountNumber}`,
        value: account.accountNumber,
        medium: account.accountType
      })
    }
    this.accountsTo = Object.assign([], this.accounts);
    this.accountsFrom = Object.assign([], this.accounts);
    this.createTransferForm();
  }

  ngOnInit(): void {
  }

  findAccountMedium(account: number): string {
    let medium = this.accounts.find(e => e.value === account)
    return medium?.medium || "";
  }

  private createTransferForm(): void {
    this.transferForm = this.formBuilder.group({
      fromAccountNumber: new FormControl("", Validators.required),
      toAccountNumber: new FormControl("", Validators.required),
      amount: new FormControl("", [Validators.required]),
      transactionType: new FormControl("TRANSFER"),
      transactionMedium: new FormControl("", []),
    }, { validators: this.checkAccounts })
  }

  submit(): void {
    const data = this.transferForm.value;
    data.amount = data.amount * -1;
    data.fromAccountNumber = parseInt(data.fromAccountNumber, 10);
    data.toAccountNumber = parseInt(data.toAccountNumber, 10);
    data.transactionMedium = this.findAccountMedium(data.fromAccountNumber)
    this.accountManagementService.makeTransaction(data);
    this.transferForm.reset();
  }

  checkAccounts: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    const clone = Object.assign([], this.accounts);
    let index = clone.findIndex((v: { label: string; value: string; }) => {
      return parseInt(v.value) === parseInt(group.get('toAccountNumber')?.value);
    });

    if (index !== -1 && group.get('toAccountNumber')?.value !== '') {
      clone.splice(index, 1);
    }
    this.accountsFrom = Object.assign([], clone);

    const cloneTo = Object.assign([], this.accounts);
    index = cloneTo.findIndex((v: Item) => {
      return parseInt(v.value) === parseInt(group.get('fromAccountNumber')?.value);
    });

    if (index !== -1 && group.get('fromAccountNumber')?.value !== '') {
      cloneTo.splice(index, 1);
    }
    this.accountsTo = Object.assign([], cloneTo);
    return null;
  }

}
