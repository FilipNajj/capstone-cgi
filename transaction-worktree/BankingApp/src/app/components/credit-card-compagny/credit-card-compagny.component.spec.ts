import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreditCardCompagnyComponent } from './credit-card-compagny.component';

describe('CreditCardCompagnyComponent', () => {
  let component: CreditCardCompagnyComponent;
  let fixture: ComponentFixture<CreditCardCompagnyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreditCardCompagnyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreditCardCompagnyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
