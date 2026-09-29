import { TestBed } from '@angular/core/testing';

import { Calibration } from './calibration';

describe('Calibration', () => {
  let service: Calibration;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Calibration);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
