import { Component, Input } from '@angular/core';
import { CreditCard } from '@app/interfaces/creditCard';
import { AccountManagementService } from '@app/services/account-management.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-credit-card-overview',
  templateUrl: './credit-card-overview.component.html',
  styleUrls: ['./credit-card-overview.component.scss']
})
export class CreditCardOverviewComponent {

  @Input('creditCard') creditCard: CreditCard;

  constructor(
    private accountManagementService: AccountManagementService,
    private routerService: RouterService
  ) { }

  goToAccount(): void {
    this.accountManagementService.currentCreditCard = this.creditCard;
    this.accountManagementService.isCreditCard = true;
    this.routerService.routeToAccountView();
  }

}
