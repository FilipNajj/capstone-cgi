import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent {

  public navMenu = [
    {
      label: "Clients",
      link : '/admin/clients'
    },
    {
      label: "Credit Card Company",
      link : '/admin/creditCardCompany'
    },
    {
      label: "Institutions",
      link : '/admin/institutions'
    },
    {
      label: "Cheque validations",
      link : '/admin/cheque-validation',
      isDisabled: true
    },
    {
      label: "Profile",
      link : '/admin/profile',
      isDisabled: true
    },
  ]
  
  constructor() { }

}
