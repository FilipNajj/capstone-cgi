import { Injectable } from '@angular/core';
import { Status } from '@app/enum/status';
import { Address } from '@app/interfaces/address';
import { Client } from '@app/interfaces/client';
import { CreditCardCompany } from '@app/interfaces/creditCardCompany';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  address1: Address = {
    streetName: 'ddddd',
    streetNumber: 123,
    city: 'mtl',
    postalCode: 'f5f55f'
  };

  creditCardCom1: CreditCardCompany = {
    companyId: 1,
    companyName: 'TD',
    address: this.address1,
    email: 'a@gmail.com',
    createdDate: new Date(),
    phoneNumber: 12345798,
    productsList: [],
    accountsList: []
  }

  creditCardCom2: CreditCardCompany = {
    companyId: 2,
    companyName: 'RBC',
    address: this.address1,
    email: 'b@gmail.com',
    createdDate: new Date(),
    phoneNumber: 4567913,
    productsList: [],
    accountsList: []
  }

  client1: Client = {
    profileId: 1,
    firstName: 'ali',
    lastName: 'ali',
    address: this.address1,
    email: 'ali@gmail.com',
    createdDate: new Date(),
    accounts: [],
    cardsList: [],
    institutionsList: [],
    recipientList: [],
    profileStatus: Status.ACTIVE,
    phoneNumber: 123456798
  }

  client2: Client = {
    profileId: 2,
    firstName: 'zahra',
    lastName: 'zahra',
    address: this.address1,
    email: 'zahra@gmail.com',
    createdDate: new Date(),
    accounts: [],
    cardsList: [],
    institutionsList: [],
    recipientList: [],
    profileStatus: Status.CLOSED,
    phoneNumber: 14678798
  }

  creditCardCompanies: CreditCardCompany[] = [
    this.creditCardCom1, this.creditCardCom2
  ]

  clients: Client[] = [
    this.client1, this.client2
  ]

  currentCreditCardCompany: CreditCardCompany = this.creditCardCom1;
  currentClient: Client = this.client1;

  constructor() { }
}
