import { Component, OnInit } from '@angular/core';
import { bankProduct } from '@app/interfaces/bankProduct';
import { BankproductService } from '@app/services/bankproduct.service';

@Component({
  selector: 'app-products-information',
  templateUrl: './products-information.component.html',
  styleUrls: ['./products-information.component.scss']
})
export class ProductsInformationComponent implements OnInit {

  bankProductList: bankProduct[];

  constructor(
    private bankProductService: BankproductService
  ) { }
  ngOnInit(): void {
    this.bankProductService.getAllBankProducts()
    .subscribe({
      next:(data: bankProduct[]) => {
        this.bankProductList = data;
      },
      error:(err:any) =>{
        this.bankProductList = [];
      }
    })
  }
}