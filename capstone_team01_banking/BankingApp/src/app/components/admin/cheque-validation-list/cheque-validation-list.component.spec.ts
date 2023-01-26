import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChequeValidationListComponent } from './cheque-validation-list.component';

describe('ChequeValidationListComponent', () => {
  let component: ChequeValidationListComponent;
  let fixture: ComponentFixture<ChequeValidationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChequeValidationListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChequeValidationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
