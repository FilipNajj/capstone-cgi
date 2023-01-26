import { Component, HostListener, OnInit } from '@angular/core';
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
  selector: 'app-register-admin',
  templateUrl: './register-admin.component.html',
  styleUrls: ['./register-admin.component.scss']
})
export class RegisterAdminComponent {
  hide = true;
  matcher = new ErrorStateMatcherPassword();
  public registrationForm: FormGroup;
  TYPE_ACCOUNTS = [
    'Chequing', 'Savings'
  ]

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService
  ) {
    this.createRegistrationForm();
  }

  private createRegistrationForm(): void {
    this.registrationForm = this.formBuilder.group({
      firstName: new FormControl("", [Validators.required]),
      lastName: new FormControl("", [Validators.required]),
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
      profileStatus: new FormControl(Status.INACTIVE, []),
      role: new FormControl(Roles.ADMIN, []),
    }, { validators: this.checkPasswords })
  }

  checkPasswords: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('confirmPassword')?.value
    return pass === confirmPass ? null : { notSame: true }
  }

  register(): void {
    this.authenticationService.register(this.registrationForm.value);
    this.registrationForm.reset();
  }
}
