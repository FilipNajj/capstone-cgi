import { Component } from '@angular/core';

@Component({
  selector: 'app-cc-header',
  templateUrl: './cc-header.component.html',
  styleUrls: ['./cc-header.component.scss']
})
export class CcHeaderComponent {

  constructor() { }

  public navMenu = [
    {
      label: "Products",
      link: '/creditCardCompany'
    },
    {
      label: "Profile",
      link: '',
      isDisabled: true
    },
  ]

}
