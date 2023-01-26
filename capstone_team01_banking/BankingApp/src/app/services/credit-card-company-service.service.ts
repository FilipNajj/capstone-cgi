import { ICreditCardCompanyService } from './../interfaces/ICreditCardCompanyService';
import { map, Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreditCardProduct } from '@app/interfaces/ccc-models/CreditCardProduct';
import { CreditCardType } from '@app/enum/CreditCardType';
import { CreditCardAccount } from '@app/interfaces/ccc-models/CreditCardAccount';
import { Company } from "@app/interfaces/ccc-models/company";
import { CreditCardCompany } from '@app/interfaces/creditCardCompany';

@Injectable({
  providedIn: 'root'
})
export class CreditCardCompanyServiceService {

  companyId: number = 0;
  creditCardCompanies: CreditCardCompany[] = [];
  currentCreditCardCompany: CreditCardCompany;

  private product_host = 'http://localhost:9003/api/v1/company/product';
  private company_host = 'http://localhost:9003/api/v1/company';

  constructor(private httpClient: HttpClient) {
  }

  addNewProduct(product: CreditCardProduct): Observable<CreditCardProduct> {

    return this.httpClient
      .post(`${this.product_host}/${this.companyId}`
        , product) as Observable<CreditCardProduct>;

  }

  deleteProductById(productId: any) {
    return this.httpClient
      .delete(`${this.product_host}/${this.companyId}/${productId}`);
  }

  updateCCProduct(newData: CreditCardProduct) {
    return this.httpClient
      .put(`${this.product_host}/${this.companyId}/${newData.ccProductId}`, newData
      ) as Observable<CreditCardProduct>;
  }

  getCCC_productById(id: Number) {
    return this.httpClient.get(this.product_host + "/" + id);
  }

  public getAllProducts(): Observable<CreditCardProduct[]> {

    return this.httpClient
      .get<CreditCardProduct[]>(`${this.product_host}/${this.companyId}`);
  }

  public getAllCompanies(): Observable<CreditCardCompany[]> {
    return this.httpClient
      .get<CreditCardCompany[]>(`${this.company_host}`)
  }

  public deleteCompanyById(id: number): void {
    this.httpClient
      .delete<any>(`${this.company_host}/${id}`).subscribe((res) => {
        console.log(res)
      })
  }

  public editCompanyById(company: CreditCardCompany): void {
    this.httpClient
      .put<CreditCardCompany>(`${this.company_host}/${company.companyId}`, company).subscribe((res) => {
        console.log(res)
      })
  }

  public getProductById(productId: any): Observable<CreditCardProduct> {
    return this.httpClient
      .get(`${this.product_host}/${this.companyId}/${productId}`
      ) as Observable<CreditCardProduct>;

  }

  getRangeData(page: number, pageSize: number) {
    let startIndex = page * pageSize;

    return this.getAllProducts()
      .pipe(
        map(data => data.slice(startIndex, startIndex + pageSize))
      );
  }

}
