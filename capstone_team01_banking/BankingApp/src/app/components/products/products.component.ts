import { Component } from '@angular/core';
import { ProductCCService } from '@app/services/product-cc.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent  {

  constructor(
    protected productCCService: ProductCCService
  ) { 
    this.productCCService.getAllCCProduct();
    console.log(this.productCCService.creditCardProducts)
  }

}                                                   