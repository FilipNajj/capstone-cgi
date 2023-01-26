import { Component, Input } from '@angular/core';
import { CreditCardProduct } from '@app/interfaces/creditCardProduct';
import { AccountType } from '@app/enum/accountType';

@Component({
  selector: 'app-product-cc',
  templateUrl: './product-cc.component.html',
  styleUrls: ['./product-cc.component.scss']
})
export class ProductCCComponent {

  @Input('creditCardProduct') creditCardProduct: CreditCardProduct;

  constructor(
  ) {
  }

  getImageUrl(): string {
    switch (this.creditCardProduct.type.toLowerCase()) {
      case AccountType.AMEX.toLowerCase():
        return "/assets/AMX.png";
      case AccountType.VISA.toLowerCase():
        console.log("here")
        return "/assets/TV.jpg";
      case AccountType.MASTERCARD.toLowerCase():
        return "/assets/MCC.jpg";
      default:
        return "/assets/TV.jpg";
    }
  }

}


