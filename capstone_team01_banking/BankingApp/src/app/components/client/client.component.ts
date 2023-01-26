import { Component, ViewChild } from '@angular/core';
import { AccountManagementService } from '@app/services/account-management.service';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent {

  @ViewChild('sidenav') private sidenav: any;
  opened: boolean = true;
  menu = [
    {
      label: 'E-transfer',
      link: '/client/e-transfer'
    },
    {
      label: 'Pay bill',
      link: '/client/pay-bill'
    },
    {
      label: 'Transfer between my account',
      link: '/client/transfer'
    },
    {
      label: 'Deposit cheque',
      link: '/client/deposit-cheque',
      isDisabled: true
    },
    {
      label: 'Contacts',
      link: '/client/contacts'
    }
  ]

  constructor(
    protected accountManagementService: AccountManagementService
  ) {
    this.accountManagementService.getAllAccountForUser();
  }

  toggle(): void {
    this.sidenav.toggle();
  }
}
