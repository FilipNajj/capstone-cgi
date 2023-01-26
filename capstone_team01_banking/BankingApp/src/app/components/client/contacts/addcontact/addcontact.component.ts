import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Contact } from '@app/interfaces/contact';
import { ContactService } from '@app/services/contact.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-add-contact',
  templateUrl: './addcontact.component.html',
  styleUrls: ['./addcontact.component.scss']
})
export class AddContactComponent implements OnInit {
  contactForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private contactService: ContactService, private routerService: RouterService) {
    this.contactForm = this.formBuilder.group({
      contactName: new FormControl("", Validators.required),
      mobilePhone: new FormControl("", Validators.required),
      emailAddress: new FormControl("", [Validators.required, Validators.email]),
    })
  }

  ngOnInit(): void {
  }

  addContact(){
    if (this.contactForm.valid){
      let contact: Contact = {
        recipientId: 0,
        recipientName: this.contactForm.get('contactName')?.value,
        mobilePhone: this.contactForm.get('mobilePhone')?.value,
        emailAddress: this.contactForm.get('emailAddress')?.value,
      }
      this.contactService.addContact(contact);
      this.contactForm.reset();
    }
    
  }

}
