import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AccountManagementService } from '@app/services/account-management.service';
import { ClientService } from '@app/services/client.service';
import { ContactService } from '@app/services/contact.service';
import { InstitutionService } from '@app/services/institution.service';
import { Item } from '../transfer/transfer.component';


@Component({
  selector: 'app-e-transfer',
  templateUrl: './e-transfer.component.html',
  styleUrls: ['./e-transfer.component.scss']
})
export class ETransferComponent {

  public eTransferForm: FormGroup;
  accounts: Item[] = []
  contacts: Item[] = []

  constructor(
    private formBuilder: FormBuilder,
    private accountManagementService: AccountManagementService,
    private clientService: ClientService,
    protected institutionService: InstitutionService,
    private contactService: ContactService
  ) {
    for (let account of this.accountManagementService.accounts) {
      this.accounts.push({
        label: `${account.accountType} : ${account.accountNumber}`,
        value: account.accountNumber,
        medium: account.accountType
      })
    }
    this.contactService.getContacts().subscribe({
      next: (contacts) => {
        for (let contact of contacts) {
          this.contacts.push({
            label: `${contact.recipientName}`,
            value: contact.emailAddress
          })
        }
      }
    });
    this.createETransferForm();
  }

  private createETransferForm(): void {
    this.eTransferForm = this.formBuilder.group({
      fromAccountNumber: new FormControl("", Validators.required),
      emailRecipient: new FormControl("", Validators.required),
      amount: new FormControl("", [Validators.required]),
      transactionType: new FormControl("ETRANSFER")
    })
  }

  findAccountMedium(account: number): string {
    let medium = this.accounts.find(e => e.value === account)
    return medium?.medium || "";
  }

  submit(): void {
    const data = this.eTransferForm.value;
    data.amount = data.amount * -1;
    data.fromAccountNumber = parseInt(data.fromAccountNumber, 10);
    data.transactionMedium = this.findAccountMedium(data.fromAccountNumber)
    this.accountManagementService.makeTransaction(data);
    this.eTransferForm.reset();
  }


}
