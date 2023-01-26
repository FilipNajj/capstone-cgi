import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AccountType } from '@app/enum/accountType';
import { CreditCardProduct } from '@app/interfaces/creditCardProduct';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ProductCCService {
  currentCreditCard: CreditCardProduct; 
  creditCardProducts: CreditCardProduct[] = [ ];
  private product_host = 'http://localhost:9003/api/v1/company/product';

  constructor(private httpClient: HttpClient) {}

  public getAllProductsForCompanyId(companyId: any): void {
    this.httpClient
      .get<CreditCardProduct[]>(`${this.product_host}/${companyId}`
      ).subscribe((products)=>{
        this.creditCardProducts = products;
      })
  }

  public getAllCCProduct(): void {
    this.httpClient
      .get<CreditCardProduct[]>(`${this.product_host}`
      ).subscribe((products)=>{
        this.creditCardProducts = products;
      })
  }
  
}

