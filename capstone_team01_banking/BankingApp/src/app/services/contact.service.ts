import { Injectable } from "@angular/core";
import { Client } from "@app/interfaces/client";
import { Contact } from "@app/interfaces/contact";
import { BehaviorSubject, Observable, of, tap } from "rxjs";
import { ClientService } from "./client.service";


@Injectable({
    providedIn: 'root'
})
export class ContactService {
    contactSubject: BehaviorSubject<Contact[]> = new BehaviorSubject<Contact[]>([]);
    currentClient: Client;
    constructor(private clientService: ClientService) {
        this.clientService.getClientById().subscribe(
            (client) => {
                this.currentClient = client;
                this.currentClient.recipientList = this.currentClient.recipientList || [];
                this.contactSubject.next(this.currentClient.recipientList)
            }
        )
     }


    getContacts(): BehaviorSubject<Contact[]> {
        return this.contactSubject;
    }

    getContactById(recipientId: number): Contact | undefined {
        return this.currentClient.recipientList.find(c => c.recipientId === recipientId);          
    }

    addContact(contact: Contact): Observable<Contact> {
        contact.recipientId = Math.max(...this.currentClient.recipientList.flatMap(c=>c.recipientId)) + 1;
        if(this.currentClient.recipientList === null){
            this.currentClient.recipientList = [contact]
        }else{
            this.currentClient.recipientList.push(contact);
        }
        this.clientService.updateClient(this.currentClient);
        this.contactSubject.next(this.currentClient.recipientList);
        return of(contact);
    }

    editContact(contact: Contact): Observable<Contact> {
        this.currentClient.recipientList.forEach((c)=>{
            if (c.recipientId === contact.recipientId){
                c = contact;
            }
        });
        this.clientService.updateClient(this.currentClient);
        this.contactSubject.next(this.currentClient.recipientList);
        return of(contact);
    }

    deleteContact(recipientIds: number[]): Observable<boolean> {
        this.currentClient.recipientList = this.currentClient.recipientList.filter(c => !recipientIds.includes(c.recipientId));
        this.clientService.updateClient(this.currentClient);
        this.contactSubject.next(this.currentClient.recipientList);
        return of(true);
    }

}
