import { CreditCardType } from "@app/enum/CreditCardType"

export class CreditCardProduct {
  ccProductId: number;
  name: string;
  id?: any;
  type: CreditCardType;
  description: String;
  startingDate: Date;
  endingDate: Date;
  monthsToExpire: number
  cashAdvanceInterestRate: number
  purchaseInterestRate: number
  averageRating?: number
  selected?: boolean = false;
}
