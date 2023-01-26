import { User } from './../../../interfaces/user';
import { RouterService } from './../../../services/router.service';
import { AuthenticationService } from '@app/services/authentication.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, HostListener, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { Roles } from '@app/enum/roles';
import { Status } from '@app/enum/status';
import { ProductsService } from '@app/services/products.service';
import { Product } from '@app/interfaces/product';
import { Item } from '@app/components/client/transfer/transfer.component';

export class ErrorStateMatcherPassword implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control?.invalid && control?.parent?.dirty);
    const invalidParent = !!(control?.parent?.invalid && control?.parent?.dirty);

    return invalidCtrl || invalidParent;
  }
}

@Component({
  selector: 'app-register-client',
  templateUrl: './register-client.component.html',
  styleUrls: ['./register-client.component.scss']
})
export class RegisterClientComponent {
  hide = true;
  matcher = new ErrorStateMatcherPassword();
  public registrationForm: FormGroup;


  productList: Item[] = [];

  user!: User;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    protected productsService: ProductsService,
    private routerService: RouterService
  ) {
    for (let i = 0; i < this.productsService.productsAxle.length; i += 1) {
      this.productList.push({ label: this.productsService.productsAxle[i].accountType, value: this.productsService.productsAxle[i] })
    }
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
      accounts: new FormControl([]),
      cardsList: new FormControl([], []),
      recipientList: new FormControl([], []),
      institutionsList: new FormControl([], []),
      profileStatus: new FormControl(Status.ACTIVE, []),
      role: new FormControl(Roles.CLIENT, []),
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
