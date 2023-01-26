import { Status } from "@app/enum/status";
import { Rating } from "./rating";

export interface CreditCard {
    creditAccountNumber: number;
    accountProvider: string;
    accountType: string;
    creationDate : Date;
    balance: number;
    creditLimit: number;
    // maxDailyPurchasingLimit: number;
    // maxDailyWithdrawlLimit: number;
    purchaseInterestRate: number;
    cashAdvanceInterestRate: number;
    accountStatus: Status;
}