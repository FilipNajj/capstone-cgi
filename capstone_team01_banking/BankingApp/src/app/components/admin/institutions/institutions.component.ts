import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Institution } from '@app/interfaces/institution';
import { InstitutionService } from '@app/services/institution.service';

@Component({
  selector: 'app-institutions',
  templateUrl: './institutions.component.html',
  styleUrls: ['./institutions.component.scss']
})
export class InstitutionsComponent implements OnInit {
  displayedColumns: string[] = ['select', 'institutionId', 'institutionName', 'createdDate', 'phoneNumber'];
  dataSource = new MatTableDataSource<Institution>();
  selection = new SelectionModel<Institution>(true, []);
  @ViewChild(MatTable) table: MatTable<Institution>;
  openPanel = false;

  institutions: Institution[] = [];

  constructor(
    protected institutionService: InstitutionService
  ) {
    this.institutionService.fecthInstitutions();
    this.dataSource = new MatTableDataSource<Institution>(this.institutions);
  }

  ngOnInit(): void {

    this.institutionService.institutionsSubject.subscribe({
      next: data => {
        this.institutions = data;
        this.dataSource.data = this.institutions
      }
    })
  };

  edit(institution: Institution): void {
    this.institutionService.openPanel = true;
    this.institutionService.currentInstitution = institution;
  }

  removeData() {
    for (let institution of this.selection.selected) {
      this.institutionService.deleteInstitution(institution.institutionId)
    }
    const selectionId = this.selection.selected.map(function (obj) {
      return obj.institutionId;
    });
    const newElements = this.institutions.filter(v => !selectionId.includes(v.institutionId));
    this.selection.clear();
    this.dataSource = new MatTableDataSource<Institution>(newElements);
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

  checkboxLabel(row?: Institution): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.institutionId + 1}`;
  }
}
