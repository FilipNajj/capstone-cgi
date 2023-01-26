import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CcHeaderComponent } from './cc-header.component';

describe('CcHeaderComponent', () => {
  let component: CcHeaderComponent;
  let fixture: ComponentFixture<CcHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CcHeaderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CcHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
