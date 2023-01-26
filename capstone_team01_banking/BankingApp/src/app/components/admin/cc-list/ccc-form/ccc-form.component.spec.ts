import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CccFormComponent } from './ccc-form.component';

describe('CccFormComponent', () => {
  let component: CccFormComponent;
  let fixture: ComponentFixture<CccFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CccFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CccFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
