
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '@app/services/authentication.service';
import { Injectable } from '@angular/core';
import { Client } from '@app/interfaces/client';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  clientId: number = 4;
  private client_host = 'http://localhost:9001/api/v1/client/';
  clients: Client[] = [];

  constructor(
    private authenticationService: AuthenticationService,
    private httpClient: HttpClient
  ) {
    if (this.authenticationService.currentUser) {
      this.clientId = this.authenticationService.currentUser.id;
    }
  }

  createClient(client: Client): void {
    this.httpClient
      .post<Client>(`${this.client_host}`
        , client).subscribe((client) => {
          this.currentClient = client;
        });
  }

  removeClient(profileId: number): void {
    this.httpClient
      .delete<Client>(`${this.client_host}${profileId}`).subscribe((client) => {
        this.currentClient = client;
      });
  }

  currentClient: Client;

  getClientById(): Observable<Client> {
    return this.httpClient.get<Client>(this.client_host + this.clientId)
  }

  getClientByEmail(email: any) {
    return this.httpClient.get<Client>(this.client_host + "byEmail/" + email).subscribe((client) => {
      this.currentClient = client;
    });
  }

  updateClient(client: Client): void {
    this.httpClient
      .put<Client>(`${this.client_host}${client.profileId}`, client).subscribe((client) => {
        this.currentClient = client;
      });
  }

  getAllClients(): Observable<Client[]> {
    return this.httpClient
      .get<Client[]>(`${this.client_host}`)
  }

}
