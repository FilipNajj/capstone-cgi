import { SelectionModel } from '@angular/cdk/collections';
import { Component, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Contact } from '@app/interfaces/contact';
import { ContactService } from '@app/services/contact.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.scss']
})
export class ContactsComponent {
  displayedColumns: string[] = ['select', 'contactName', 'emailAddress', 'phoneNumber'];
  dataSource = new MatTableDataSource<Contact>();
  selection = new SelectionModel<Contact>(true, []);
  @ViewChild(MatTable) table: MatTable<Contact>;
  contacts: Contact[];

  constructor(
    private contactService: ContactService,
    private routerService: RouterService,
  ) {
    this.contactService.getContacts().subscribe({
      next: (contacts) => {
        this.contacts = contacts;
        this.dataSource = new MatTableDataSource<Contact>(contacts);
      }
    });
  }

  editContact(recipientId?: number) {
    if (recipientId) {
      this.routerService.routeToEditContact(recipientId);
    }
  }

  removeData() {
    const selectionId = this.selection.selected.map(function (obj) {
      return obj.recipientId;
    });
    this.contactService.deleteContact(selectionId);
    this.selection.clear();
    this.dataSource = new MatTableDataSource<Contact>(this.contacts);
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

  checkboxLabel(row?: Contact): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    if (row.recipientId)
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.recipientId + 1}`;
    return "";
  }

}
