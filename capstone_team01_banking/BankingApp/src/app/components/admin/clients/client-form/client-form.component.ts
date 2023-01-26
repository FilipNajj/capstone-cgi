import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Status } from '@app/enum/status';
import { ClientService } from '@app/services/client.service';
import { RouterService } from '@app/services/router.service';


@Component({
  selector: 'app-client-form',
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.scss']
})
export class ClientFormComponent {

  public clientForm: FormGroup;
  status: any[] = [
    {
      label: 'Active',
      value: Status.ACTIVE
    },
    {
      label: 'Inactive',
      value: Status.INACTIVE
    },
  ]


  constructor(
    private clientService: ClientService,
    private routerService: RouterService,
    private formBuilder: FormBuilder
  ) {
    this.createClientForm();
  }

  private createClientForm(): void {
    this.clientForm = this.formBuilder.group({
      profileId: new FormControl(this.clientService.currentClient?.profileId, []),
      firstName: new FormControl(this.clientService.currentClient?.firstName, Validators.required),
      lastName: new FormControl(this.clientService.currentClient?.lastName, Validators.required),
      phoneNumber: new FormControl(this.clientService.currentClient?.phoneNumber, Validators.required),
      address: this.formBuilder.group({
        streetNumber: new FormControl(this.clientService.currentClient.address?.streetNumber, [Validators.required]),
        streetName: new FormControl(this.clientService.currentClient.address?.streetName, [Validators.required]),
        city: new FormControl(this.clientService.currentClient.address?.city, [Validators.required]),
        postalCode: new FormControl(this.clientService.currentClient.address?.postalCode, [Validators.required])
      }),
      email: new FormControl(this.clientService.currentClient?.email, Validators.required),
      createdDate: new FormControl(this.clientService.currentClient?.createdDate, Validators.required),
      profileStatus: new FormControl(this.clientService.currentClient?.profileStatus, Validators.required),
      accounts: new FormControl(this.clientService.currentClient?.accounts, []),
      cardsList: new FormControl(this.clientService.currentClient?.cardsList, []),
      institutionList: new FormControl(this.clientService.currentClient?.institutionsList, []),
      recipientList: new FormControl(this.clientService.currentClient?.recipientList, []),
    })
  }

  clientsubmit(): void {
    let modifiedValues = this.clientForm.value;
    modifiedValues.profileStatus = this.statusToUpperCase(modifiedValues.profileStatus);
    this.clientService.updateClient(this.clientForm.value);
    this.clientForm.reset();
    this.routerService.routeToClientList();
  }

  statusToUpperCase(element: string): string {
    return element.toUpperCase();
  }

}
