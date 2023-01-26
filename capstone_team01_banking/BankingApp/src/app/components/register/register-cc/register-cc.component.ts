import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { Roles } from '@app/enum/roles';
import { Status } from '@app/enum/status';
import { AuthenticationService } from '@app/services/authentication.service';

export class ErrorStateMatcherPassword implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control?.invalid && control?.parent?.dirty);
    const invalidParent = !!(control?.parent?.invalid && control?.parent?.dirty);

    return invalidCtrl || invalidParent;
  }
}

@Component({
  selector: 'app-register-cc',
  templateUrl: './register-cc.component.html',
  styleUrls: ['./register-cc.component.scss']
})
export class RegisterCCComponent {
  hide = true;
  public registrationForm: FormGroup;
  matcher = new ErrorStateMatcherPassword();

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService
  ) {
    this.createRegistrationForm();
  }

  private createRegistrationForm(): void {
    this.registrationForm = this.formBuilder.group({
      companyName: new FormControl("", [Validators.required]),
      email: new FormControl("", [Validators.required]),
      password: new FormControl("", [Validators.required]),
      confirmPassword: new FormControl("", [Validators.required]),
      phoneNumber: new FormControl("", [
        Validators.required,
        Validators.pattern('[- +()0-9]+')
      ]),
      address: this.formBuilder.group({
        streetNumber: new FormControl("", [Validators.required]),
        streetName: new FormControl("", [Validators.required]),
        city: new FormControl("", [Validators.required]),
        postalCode: new FormControl("", [Validators.required])
      }),
      createdDate: new FormControl(new Date(), [Validators.required]),
      productsList: new FormControl([], []),
      accountsList: new FormControl([], []),
      profileStatus: new FormControl(Status.INACTIVE, []),
      role: new FormControl(Roles.CCC, []),
    }, { validators: this.checkPasswords })
  }

  register(): void {
    this.authenticationService.register(this.registrationForm.value);
    this.registrationForm.reset();
  }

  checkPasswords: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('confirmPassword')?.value
    return pass === confirmPass ? null : { notSame: true }
  }


}
