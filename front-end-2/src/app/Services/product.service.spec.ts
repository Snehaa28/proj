import { TestBed, inject } from '@angular/core/testing';

import { ProductService } from '../Services/product.service';

describe('ServicesProductService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProductService],
    });
  });

  it('should be created', inject(
    [ProductService],
    (service: ProductService) => {
      expect(service).toBeTruthy();
    }
  ));
});
