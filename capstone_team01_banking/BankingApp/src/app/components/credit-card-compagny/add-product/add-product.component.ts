import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { CreditCardProduct } from "@app/interfaces/ccc-models/CreditCardProduct";
import { CreditCardType } from "@app/enum/CreditCardType";
import { CreditCardCompanyServiceService } from "@app/services/credit-card-company-service.service";
import { RouterService } from "@app/services/router.service";
import { Router } from "@angular/router";
import { formatDate } from "@angular/common";


@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss']
})
export class AddProductComponent implements OnInit {

  private product: CreditCardProduct;
  protected isUpdating = false;
  private dateFormat = 'yyyy-MM-dd';
  addProductForm: FormGroup;

  constructor(
    private cccService: CreditCardCompanyServiceService,
    private routerService: RouterService,
    private router: Router
  ) {

    try {
      // @ts-ignore
      this.product = this.router.getCurrentNavigation()?.extras.state.subjectProduct;
      // @ts-ignore
      let result = this.router.getCurrentNavigation()?.extras.state.mode;
      this.isUpdating = result == undefined ? false : result;
    } catch (ex) {
    }
  }

  ngOnInit(): void {
    let startingDateValue = (this.product?.startingDate != undefined) ? formatDate(this.product?.startingDate, this.dateFormat
      , 'en')
      : '';

    let endingDateValue = this?.product?.endingDate == undefined ? ''
      : formatDate(this.product?.endingDate, this.dateFormat, 'en');

    this.addProductForm = new FormGroup({
      ccProductId: new FormControl(this.product?.ccProductId || -1, []),
      name: new FormControl(this.product?.name, [Validators.required, Validators.min(3)]),
      type: new FormControl(this.product?.type, [Validators.required]),
      description: new FormControl(this.product?.description, Validators.required),
      cashAdvanceInterestRate: new FormControl(this.product?.cashAdvanceInterestRate, [Validators.required,
      Validators.pattern('^[1-9]\\d*(\\.\\d+)?$')]),
      purchaseInterestRate: new FormControl(this.product?.purchaseInterestRate, [Validators.required]),
    })
  }

  addNewProduct() {
    if (this.isUpdating) {
      this.cccService.updateCCProduct(this.addProductForm.value).subscribe({
        next: data => {
          this.routerService.goToCCCDashboard();
        }
      })
    } else {
      this.cccService.addNewProduct(this.addProductForm.value).subscribe({
        next: (result) => {
          this.routerService.goToCCCDashboard();
        }
      })
    }
  }

  listProduct: any[] = [
    {
      label: 'Visa',
      value: CreditCardType.Visa
    },
    {
      label: 'MasterCard',
      value: CreditCardType.MasterCard
    },
    {
      label: 'Amex',
      value: CreditCardType.Amex
    },
  ]

  deleteById(id: Number) {
    return this.cccService.deleteProductById(id);
  }

}
