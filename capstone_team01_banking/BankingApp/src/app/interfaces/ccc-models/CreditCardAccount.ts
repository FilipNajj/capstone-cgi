import { NumberSymbol } from "@angular/common";
import { CreditCardType } from "@app/enum/CreditCardType";

export interface CreditCardAccount{

  creditAccountNumber: number,
  name : string,
  type : CreditCardType,
  description : string,
  expirationDate :Date,
  cashAdvanceInterestRate : number,
  purchaseInterestRate : number,
  creditLimit : Number,
  usedCredit : Number,
  clientId: number,
  rating? : number
}
