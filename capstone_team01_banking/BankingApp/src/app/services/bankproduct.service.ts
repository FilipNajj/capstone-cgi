import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { bankProduct } from '@app/interfaces/bankProduct';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BankproductService {

  bankProducts: bankProduct[]=[];
  private bank_product_host = 'http://localhost:9012/api/v1/bankproducts';

  constructor(private httpClient: HttpClient) { }

  public getAllBankProducts():Observable<bankProduct[]>{
    return this.httpClient.get<bankProduct[]>(`${this.bank_product_host}`);
  }
}
