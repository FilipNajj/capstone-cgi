import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '@app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  hide = true;
  public loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService
  ) {
    this.createLoginForm();
  }

  private createLoginForm(): void {
    this.loginForm = this.formBuilder.group({
      username: new FormControl("", [Validators.required, Validators.email]),
      password: new FormControl("", [Validators.required])
    })
  }

  login(): void {
    this.loginForm.reset();
  }

  onEnter(): void {
    if (!this.loginForm.invalid) {
      this.login();
    }
  }

}
