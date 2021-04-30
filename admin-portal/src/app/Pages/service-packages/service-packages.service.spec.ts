import { TestBed } from '@angular/core/testing';

import { ServicePackagesService } from './service-packages.service';

describe('ServicePackagesService', () => {
  let service: ServicePackagesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicePackagesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
