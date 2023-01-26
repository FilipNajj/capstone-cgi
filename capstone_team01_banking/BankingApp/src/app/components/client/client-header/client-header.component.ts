import { Component, ElementRef, ViewChild } from '@angular/core';
import { ClientComponent } from '../client.component';

@Component({
  selector: 'app-client-header',
  templateUrl: './client-header.component.html',
  styleUrls: ['./client-header.component.scss']
})
export class ClientHeaderComponent {

  public navMenu = [
    {
      label: "Accounts",
      link : '/client/accounts'
    },
    {
      label: "Budget app",
      link : '/client/budget-app',
      isDisabled: true
    },
    {
      label: "Profile",
      link : '/client/profile'
    },
  ]
  
  constructor(
    protected clientComponent: ClientComponent
  ) { }




}
