import { Address } from "./Address"
import {CreditCardProduct} from "@app/interfaces/ccc-models/CreditCardProduct";
import {CreditCardAccount} from "@app/interfaces/ccc-models/CreditCardAccount";

export interface Company{

  companyId: Number,
  companyName: string,
  email: string,
  createdDate: Date,
  phoneNumber:Number,

  address: Address,
  productsList: CreditCardProduct[],
  accountList: CreditCardAccount[]
}
