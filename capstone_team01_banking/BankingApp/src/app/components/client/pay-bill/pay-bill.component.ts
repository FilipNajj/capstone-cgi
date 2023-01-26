import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AccountManagementService } from '@app/services/account-management.service';
import { ClientService } from '@app/services/client.service';
import { InstitutionService } from '@app/services/institution.service';
import { Item } from '../transfer/transfer.component';

@Component({
  selector: 'app-pay-bill',
  templateUrl: './pay-bill.component.html',
  styleUrls: ['./pay-bill.component.scss']
})
export class PayBillComponent {

  public payBillForm: FormGroup;
  accounts: Item[] = []

  institutions: Item[] = [
    {
      label: "", value: ""
    }
  ]

  constructor(
    private formBuilder: FormBuilder,
    private accountManagementService: AccountManagementService,
    private clientService: ClientService,
    protected institutionService: InstitutionService
  ) {
    this.institutionService.fecthInstitutions();
    for (let account of this.accountManagementService.accounts) {
      this.accounts.push({
        label: `${account.accountType} : ${account.accountNumber}`,
        value: account.accountNumber,
        medium: account.accountType
      })
    }
    this.institutions = [];
    this.institutionService.getAllInstitutions().subscribe({
      next: (data) => {
        for (let institution of data) {
          if(this.institutions.filter(e => e.value ===  institution.institutionId).length == 0)
          this.institutions.push({
            label: `${institution.institutionId} : ${institution.institutionName}`,
            value: institution.institutionId
          })
        }
      }
    })
    this.createPayBillForm();
  }

  private createPayBillForm(): void {
    this.payBillForm = this.formBuilder.group({
      fromAccountNumber: new FormControl((this.accounts[0].value.toString()), Validators.required),
      institutionId: new FormControl("", Validators.required),
      amount: new FormControl("", [Validators.required]),
      transactionType: new FormControl("PAYMENT", [])
    })
  }

  findAccountMedium(account: number): string {
    let medium = this.accounts.find(e => e.value === account)
    return medium?.medium || "";
  }

  submit(): void {
    const data = this.payBillForm.value;
    data.amount = data.amount * -1;
    data.fromAccountNumber = parseInt(data.fromAccountNumber, 10);
    data.institutionId = parseInt(data.institutionId, 10);
    data.transactionMedium = this.findAccountMedium(data.fromAccountNumber)
    this.accountManagementService.makeTransaction(data);
    this.payBillForm.reset();
  }

}
