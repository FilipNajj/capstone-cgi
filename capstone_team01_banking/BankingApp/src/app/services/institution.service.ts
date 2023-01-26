import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Institution } from '@app/interfaces/institution';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs';
import { JsonPipe } from '@angular/common';

const URL_INSTITUTION = "http://localhost:9009/api/v1/institution";

@Injectable({
  providedIn: 'root'
})
export class InstitutionService {

  openPanel: boolean;
  currentInstitution: Institution;
  institutions: Institution[] = [];
  constructor(private httpClient: HttpClient) { }
  institutionsSubject: BehaviorSubject<Institution[]> = new BehaviorSubject<Institution[]>(this.institutions);

  public fecthInstitutions() {
    this.httpClient.get<Institution[]>(`${URL_INSTITUTION}/list`)
      .subscribe((institutions) => {
        this.institutions = institutions
        this.institutionsSubject.next(this.institutions);
      })
  }

  getAllInstitutions(): BehaviorSubject<Institution[]> {
    return this.institutionsSubject;
  }

  public getInstitution(id: number) {
    this.httpClient.get<any>(`${URL_INSTITUTION}/findById/${id}`).subscribe({
      next: data => { this.currentInstitution = data },
      error: err => { }
    });
  }
  public deleteInstitution(id: number) {
    this.httpClient.delete<any>(`${URL_INSTITUTION}/delete/${id}`).subscribe({
      next: data => {
        let index = this.institutions.findIndex(x => x.institutionId == id);
        if (index != -1) this.institutions.splice(index, 1);
        this.institutionsSubject.next(this.institutions);
      },
      error: err => { console.log("deleted error" + JSON.stringify(err)) }
    });
  }

  public saveInstitution(newInstitution: Institution) {
    return this.httpClient.post<Institution>(`${URL_INSTITUTION}/save`, newInstitution)
      .subscribe({
        next: data => {
          this.institutions.push(data);
          this.institutionsSubject.next(this.institutions);
        },
        error: err => { }
      });
  }

  public updateInstitution(institution: Institution,) {
    return this.httpClient.put<any>(`${URL_INSTITUTION}/${institution.institutionId}`, institution).subscribe({
      next: data => {
        this.getAllInstitutions();
      },
      error: err => {
      }
    });
  }
}