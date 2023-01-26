import { AccountType } from "@app/enum/accountType";
import { Status } from "@app/enum/status";

export interface Account {
    profileId?: number;
    accountNumber: number;
    balance: number;
    maxDailyPurchasingLimit: number;
    maxDailyWithdrawlLimit: number;
    interestRate: number;
    accountStatus: Status;
    overDraft: number;
    creationDate: Date;
    accountType: AccountType;
}