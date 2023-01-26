import { Component } from '@angular/core';
import { AccountManagementService } from '@app/services/account-management.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent {

  constructor(
    protected accountManagementService: AccountManagementService
  ) { 
    this.accountManagementService.getAllAccountForUser();
  }

}
