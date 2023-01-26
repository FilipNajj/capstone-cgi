import { SelectionModel } from '@angular/cdk/collections';
import { Component, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Status } from '@app/enum/status';
import { Client } from '@app/interfaces/client';
import { AdminService } from '@app/services/admin.service';
import { ClientService } from '@app/services/client.service';
import { RouterService } from '@app/services/router.service';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent {
  displayedColumns: string[] = ['select', 'profileId', 'firstName', 'lastName', 'email', 'phoneNumber', 'profileStatus'];
  dataSource = new MatTableDataSource<Client>();
  selection = new SelectionModel<Client>(true, []);
  @ViewChild(MatTable) table: MatTable<Client>;

  constructor(
    private routerService: RouterService,
    private clientService: ClientService

  ) {
    this.clientService.getAllClients().
      subscribe((clients) => {
        this.clientService.clients = clients;
        this.dataSource = new MatTableDataSource<Client>(this.clientService.clients);
      })

  }

  goToClient(client: Client): void {
    this.clientService.currentClient = client;
    this.routerService.routeToClientForm();
  }

  removeData() {
    for (let client of this.selection.selected) {
      this.clientService.removeClient(client.profileId);
    }
    const selectionId = this.selection.selected.map(function (obj) {
      return obj.profileId;
    });
    const newElements = this.clientService.clients.filter(v => !selectionId.includes(v.profileId));
    this.selection.clear();
    this.dataSource = new MatTableDataSource<Client>(newElements);
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

  checkboxLabel(row?: Client): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.profileId + 1}`;
  }


  isGreen(status: Status): boolean {
    return status === Status.ACTIVE
  }

  isRed(status: Status): boolean {
    const statusArray = [Status.CLOSED, Status.INACTIVE, Status.SUSPENDED];
    return statusArray.includes(status);
  }

  isYellow(status: Status): boolean {
    return status === Status.PENDING;
  }


}
