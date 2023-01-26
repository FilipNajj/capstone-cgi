import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ContactService } from '@app/services/contact.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-edit-contact',
  templateUrl: './editcontact.component.html',
  styleUrls: ['./editcontact.component.scss']
})
export class EditContactComponent {
  contactForm: FormGroup;
  contactId: number;

  constructor(private formBuilder: FormBuilder, private contactService: ContactService, private routerService: RouterService, private route: ActivatedRoute) {
    let contactIdString = this.route.snapshot.paramMap.get("contactId")

    if (contactIdString) {
      this.contactId = parseInt(contactIdString);
      let contact = this.contactService.getContactById(this.contactId);
      this.contactForm = this.formBuilder.group({
        contactName: new FormControl(contact?.recipientName, Validators.required),
        mobilePhone: new FormControl(contact?.mobilePhone, Validators.required),
        emailAddress: new FormControl(contact?.emailAddress, [Validators.required, Validators.email]),
      })
    } else {
      this.routerService.routeToContacts();
    }

  }

  editContact() {
    let contact = this.contactService.getContactById(this.contactId)
    if (contact && this.contactForm.valid) {
      contact.recipientName = this.contactForm.get('contactName')?.value,
        contact.mobilePhone = this.contactForm.get('mobilePhone')?.value,
        contact.emailAddress = this.contactForm.get('emailAddress')?.value,
        this.contactService.editContact(contact);
      this.routerService.routeToContacts();
    }
  }

}
