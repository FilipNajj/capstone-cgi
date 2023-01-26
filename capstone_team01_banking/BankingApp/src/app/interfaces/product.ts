import { AccountType } from "@app/enum/accountType";
import { Status } from "@app/enum/status";

export interface Product {
    balance: number;
    maxDailyPurchasingLimit: number;
    maxDailyWithdrawlLimit: number;
    interestRate: number;
    overDraft: number;
    creationDate? : Date;
    accountType: AccountType;    
    accountStatus: string;
}