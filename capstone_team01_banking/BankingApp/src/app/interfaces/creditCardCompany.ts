import { Address } from "./address";
import { CreditCard } from "./creditCard";
import { CreditCardProduct } from "./creditCardProduct";

export interface CreditCardCompany {
    companyId: number;
    companyName: string;
	address: Address;
	email : string;
	createdDate: Date;
	phoneNumber : number;
	productsList: CreditCardProduct[];
	accountsList: CreditCard[];
}