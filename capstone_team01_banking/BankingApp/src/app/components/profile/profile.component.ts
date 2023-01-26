import { Component, OnInit } from '@angular/core';
import { ClientService } from '@app/services/client.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  public client: any;

  constructor(private clientService: ClientService) { }

  ngOnInit(): void {
    this.clientService
      .getClientById()
      .subscribe((data) => (this.client = data));
  }
}
