import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterCCComponent } from './register-cc.component';

describe('RegisterCCComponent', () => {
  let component: RegisterCCComponent;
  let fixture: ComponentFixture<RegisterCCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterCCComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterCCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
