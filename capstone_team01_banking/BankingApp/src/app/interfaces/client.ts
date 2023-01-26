import { Status } from "@app/enum/status";
import { Address } from "./address";
import { Account } from "./account";
import { CreditCard } from "./creditCard";
import { Institution } from "./institution";
import { Recipient } from "./recipient";
import { Contact } from "./contact";

export interface Client {
    profileId: number;
	firstName: string;
	lastName: string;
	phoneNumber : number;
	address: Address;
	email : string;
	createdDate: Date;
	accounts: Account[];
	cardsList: CreditCard[];
	institutionsList: Institution[]
	recipientList: Contact[]
	profileStatus: Status
}