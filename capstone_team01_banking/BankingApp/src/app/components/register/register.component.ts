import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Roles } from '@app/enum/roles';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  accountTypes = [
    {
      value: Roles.CLIENT,
      label: 'Client'
    },
    {
      value: Roles.CCC,
      label: 'Credit card company'
    },
    {
      value: Roles.ADMIN,
      label: 'Administration'
    },
  ]

  form = new FormGroup({
    typeUser: new FormControl((Roles.CLIENT), [Validators.required])
  })

  constructor(
    private routerService: RouterService
  ) { }

  ngOnInit(): void {
  }

  goToForm(): void {
    if (this.form.valid) {
      this.routerService.routeToRegister(this.form.controls['typeUser'].value);
    }
  }

}
