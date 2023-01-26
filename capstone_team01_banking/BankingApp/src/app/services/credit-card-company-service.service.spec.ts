import { TestBed } from '@angular/core/testing';

import { CreditCardCompanyServiceService } from './credit-card-company-service.service';

describe('CreditCardCompanyServiceService', () => {
  let service: CreditCardCompanyServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreditCardCompanyServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
