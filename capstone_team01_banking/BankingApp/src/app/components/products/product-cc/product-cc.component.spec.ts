import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCCComponent } from './product-cc.component';

describe('ProductCCComponent', () => {
  let component: ProductCCComponent;
  let fixture: ComponentFixture<ProductCCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductCCComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
