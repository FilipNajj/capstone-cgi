import { TestBed } from '@angular/core/testing';

import { ProductCCService } from './product-cc.service';

describe('ProductCCService', () => {
  let service: ProductCCService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductCCService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
