import { TestBed } from '@angular/core/testing';

import { CanActivateRouteGuardGuard } from './can-activate-route-guard.guard';

describe('CanActivateRouteGuardGuard', () => {
  let guard: CanActivateRouteGuardGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(CanActivateRouteGuardGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
