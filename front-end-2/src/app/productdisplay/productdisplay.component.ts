import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { ProductDisplay } from '../Models/ProductDisplay.Model';

import { ProductService } from '../Services/product.service';
import { Product } from '../Models/Product.Model';
import { IAlert } from '../Models/IAlert';

@Component({
  selector: 'app-productdisplay',
  templateUrl: './productdisplay.component.html',
  styleUrls: ['./productdisplay.component.scss'],
  providers: [ProductService],
})
export class ProductdisplayComponent implements OnInit {
  public alerts: Array<IAlert> = [];
  @Output() cartEvent = new EventEmitter<number>();
  public globalResponse: any;
  yourByteArray: any;
  allProducts: ProductDisplay;
  productsAddedTocart: Map<Number, Product> = new Map<Number, Product>();
  constructor(private productService: ProductService) {
    this.productsAddedTocart = this.productService.getProductFromCart();
  }

  ngOnInit() {
    this.productService.getAllProducts().subscribe(
      (result) => {
        this.globalResponse = result;
      },
      (error) => {
        //This is error part
        console.log(error.message);
      },
      () => {
        //  This is Success part
        console.log('Product fetched sucssesfully.');
        console.log(this.globalResponse);
        this.allProducts = this.globalResponse;
      }
    );
  }
  OnAddCart(product: Product) {
    console.log(product);
    // kafka producer
    this.productService.sendProductMessage(product).subscribe(
      (result) => {
        console.log(result);
      },
      (error) => {
        //This is error part
        console.log(error.message);
      },
      () => {
        //  This is Success part
        console.log('Product sent to kafka producer sucssesfully.');
        this.productsAddedTocart = this.productService.getProductFromCart();
        if (this.productsAddedTocart.get(product.id) == null) {
          this.productService.addProductToCart(product);
          this.alerts.push({
            id: 1,
            type: 'success',
            message: 'Product added to cart.',
          });
          setTimeout(() => {
            this.closeAlert(this.alerts);
          }, 3000);
        } else {
          this.alerts.push({
            id: 2,
            type: 'warning',
            message: 'Product already exist in cart.',
          });
          setTimeout(() => {
            this.closeAlert(this.alerts);
          }, 3000);
        }
      }
    );
  }
  public closeAlert(alert: any) {
    const index: number = this.alerts.indexOf(alert);
    this.alerts.splice(index, 1);
  }
}
