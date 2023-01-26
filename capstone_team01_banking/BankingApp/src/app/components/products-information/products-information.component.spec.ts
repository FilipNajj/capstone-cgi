import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductsInformationComponent } from './products-information.component';

describe('ProductsInformationComponent', () => {
  let component: ProductsInformationComponent;
  let fixture: ComponentFixture<ProductsInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductsInformationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductsInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
