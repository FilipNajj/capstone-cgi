import { AccountType } from '@app/enum/accountType';

export interface CreditCardProduct {
    ccProductId: number;
    name: string;
    type: AccountType;
    description: string;
    startingDate: Date
    endingDate: Date
    monthsToExpire: number;
    cashAdvanceInterestRate: number;
    purchaseInterestRate: number;
    averageRating: number;
}