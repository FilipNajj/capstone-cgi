import { CreditCardProduct } from '@app/interfaces/ccc-models/CreditCardProduct';
import { Observable } from 'rxjs';
import { CreditCardAccount } from './ccc-models/CreditCardAccount';
export interface ICreditCardCompanyService{

  getAllProducts(companyId:any): Observable<CreditCardProduct[]>;

  getProductById(productId:any) : Observable<CreditCardProduct >;

  addNewProduct(product: CreditCardProduct): Observable<CreditCardProduct >;

  deleteProductById(productId:any) :any;

  getAllClientOfProduct(productId:any): Observable<CreditCardAccount[]>;




}
