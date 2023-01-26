import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { InstitutionService } from '@app/services/institution.service';

@Component({
  selector: 'app-institution-form',
  templateUrl: './institution-form.component.html',
  styleUrls: ['./institution-form.component.scss']
})
export class InstitutionFormComponent {
  public institutionForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    protected institutionService: InstitutionService
  ) {
    this.createInstitutionForm();
  }

  private createInstitutionForm(): void {
    this.institutionForm = this.formBuilder.group({
      institutionName: new FormControl(this.institutionService.currentInstitution?.institutionName, Validators.required),
      phoneNumber: new FormControl(this.institutionService.currentInstitution?.phoneNumber, Validators.required),
      dateCreated: new FormControl(new Date(), Validators.required)
    })
  }

  submit(): void {
    this.institutionService.saveInstitution(this.institutionForm.value);
    this.institutionForm.reset();
  }

}
