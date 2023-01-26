import { SelectionModel } from '@angular/cdk/collections';
import { Component, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { CreditCardCompany } from '@app/interfaces/creditCardCompany';
import { AdminService } from '@app/services/admin.service';
import { CreditCardCompanyServiceService } from '@app/services/credit-card-company-service.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-cc-list',
  templateUrl: './cc-list.component.html',
  styleUrls: ['./cc-list.component.scss']
})
export class CcListComponent {
  displayedColumns: string[] = ['select', 'companyId', 'companyName', 'email', 'createdDate', 'phoneNumber'];
  dataSource = new MatTableDataSource<CreditCardCompany>();
  selection = new SelectionModel<CreditCardCompany>(true, []);
  @ViewChild(MatTable) table: MatTable<CreditCardCompany>;

  constructor(
    private creditCardCompanyService : CreditCardCompanyServiceService,
    private routerService: RouterService
  ) {
    this.creditCardCompanyService.getAllCompanies().subscribe((companies) => {
      this.creditCardCompanyService.creditCardCompanies = companies;
      this.dataSource = new MatTableDataSource<CreditCardCompany>(companies);
    });
  }

  goToCCC(element: CreditCardCompany): void {
    this.creditCardCompanyService.currentCreditCardCompany = element;
    this.routerService.routeToCCCForm();
  }

  removeData() {
    for(let company of this.selection.selected){
      this.creditCardCompanyService.deleteCompanyById(company.companyId);
    }
    const selectionId = this.selection.selected.map(function (obj) {
      return obj.companyId;
    });
    const newElements = this.creditCardCompanyService.creditCardCompanies.filter(v => !selectionId.includes(v.companyId));
    this.selection.clear();
    this.dataSource = new MatTableDataSource<CreditCardCompany>(newElements);
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

  checkboxLabel(row?: CreditCardCompany): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.companyId + 1}`;
  }
}
