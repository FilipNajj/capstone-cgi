import { TestBed } from '@angular/core/testing';

import { BankproductService } from './bankproduct.service';

describe('BankproductService', () => {
  let service: BankproductService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankproductService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
