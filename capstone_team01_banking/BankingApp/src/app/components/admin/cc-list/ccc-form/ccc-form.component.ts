import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '@app/services/admin.service';
import { CreditCardCompanyServiceService } from '@app/services/credit-card-company-service.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-ccc-form',
  templateUrl: './ccc-form.component.html',
  styleUrls: ['./ccc-form.component.scss']
})
export class CccFormComponent {

  public CCCForm: FormGroup;


  constructor(
    private creditCardCompanyService : CreditCardCompanyServiceService,
    private routerService: RouterService,
    private formBuilder: FormBuilder
  ) {
    this.createCCCForm();
  }

  private createCCCForm(): void {
    this.CCCForm = this.formBuilder.group({
      companyId: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.companyId, []),
      companyName: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.companyName, Validators.required),
      address: this.formBuilder.group({
        streetNumber: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.address?.streetNumber, [Validators.required]),
        streetName: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.address?.streetName, [Validators.required]),
        city: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.address?.city, [Validators.required]),
        postalCode: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.address?.postalCode, [Validators.required])
      }),
      email: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.email, Validators.required),
      createdDate: new FormControl(this.creditCardCompanyService.currentCreditCardCompany?.createdDate || new Date(), Validators.required),
      phoneNumber: new FormControl(this.creditCardCompanyService.currentCreditCardCompany.phoneNumber, Validators.required),
    })
  }


  submit(): void {
    this.creditCardCompanyService.editCompanyById(this.CCCForm.value);
    this.CCCForm.reset();
    this.routerService.routeToCCCList();
  }

}
