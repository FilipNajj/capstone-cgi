import { CreditCardProduct } from '@app/interfaces/ccc-models/CreditCardProduct';
import { CreditCardCompanyServiceService } from '@app/services/credit-card-company-service.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { RouterService } from "@app/services/router.service";
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';


@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss']
})
export class ProductsListComponent implements OnInit {

  productList: CreditCardProduct[];
  displayedColumns: string[] = ['select', 'ccProductId', 'name', 'type', 'description', 'cashAdvanceInterestRate', 'purchaseInterestRate'];
  dataSource = new MatTableDataSource<CreditCardProduct>();
  selection = new SelectionModel<CreditCardProduct>(true, []);
  @ViewChild(MatTable) table: MatTable<CreditCardProduct>;


  constructor(
    private cccService: CreditCardCompanyServiceService,
    private routerService: RouterService
  ) { }

  ngOnInit(): void {
    this.cccService.getAllProducts()
      .subscribe({
        next: (data: CreditCardProduct[]) => {
          this.dataSource = new MatTableDataSource<CreditCardProduct>(data);
          this.productList = data;
        },
        error: (err: any) => {
          this.productList = [];
        }
      });
  }

  editProduct(product: CreditCardProduct): void {
    this.routerService.gotoEditCCCProduct(product);
  }

  removeData() {
    for (let client of this.selection.selected) {
      this.cccService.deleteProductById(client.ccProductId).subscribe((res) => {
        console.log(res)
      });
    }
    const selectionId = this.selection.selected.map(function (obj) {
      return obj.ccProductId;
    });
    const newElements = this.productList.filter(v => !selectionId.includes(v.ccProductId));
    this.selection.clear();
    this.dataSource = new MatTableDataSource<CreditCardProduct>(newElements);
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  checkboxLabel(row?: CreditCardProduct): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.ccProductId + 1}`;
  }

}
