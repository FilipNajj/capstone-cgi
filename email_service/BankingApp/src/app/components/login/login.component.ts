import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '@app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide = true;
  public loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService
  ) {
    this.createLoginForm();
  }

  ngOnInit(): void {
  }

  private createLoginForm(): void {
    this.loginForm = this.formBuilder.group({
      username: new FormControl("", [Validators.required, Validators.email]),
      password: new FormControl("", [Validators.required])
    })
  }

  login(): void {
    console.log(this.loginForm.value)
    this.authenticationService.login(this.loginForm.value);
  }

  onEnter(): void {
    if (!this.loginForm.invalid) {
      this.login();
    }
  }

}
