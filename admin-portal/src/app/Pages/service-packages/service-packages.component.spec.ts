import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicePackagesComponent } from './service-packages.component';

describe('ServicePackagesComponent', () => {
  let component: ServicePackagesComponent;
  let fixture: ComponentFixture<ServicePackagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ServicePackagesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ServicePackagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
