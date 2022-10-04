import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpClientModule,
  HttpHeaders,
} from '@angular/common/http';
import { Http, Response } from '@angular/http';
import { Observable, of, throwError, pipe, Subject } from 'rxjs';
import { map, filter, catchError, take, takeUntil } from 'rxjs/operators';
import { AuthenticationService } from './authentication.service';
import { Product } from '../Models/Product.Model';
import { RxStompService } from '@stomp/ng2-stompjs';
import { Message } from '@stomp/stompjs';
import { SharedService } from '../Services/shared.service';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  public apiURL: string = 'http://localhost:8080/product/products';
  public kafkaApiURL: string = 'http://localhost:8080/kafka/producer';
  private destroy$ = new Subject();
  private kafkaProd: Product;
  private static productsAddedToCart: Map<Number, Product> = new Map<
    Number,
    Product
  >();
  constructor(
    private httpClient: HttpClient,
    private authService: AuthenticationService,
    private rxStompService: RxStompService,
    private sharedService: SharedService
  ) {}

  ngOnInit() {
    this.rxStompService.watch('/topic/product').pipe(takeUntil(this.destroy$));
  }

  saveProductInfo(product: any) {
    var reqHeader = new HttpHeaders({
      Authorization: 'Bearer ' + this.authService.getToken(),
    });
    reqHeader.append('Content-Type', 'application/json');
    const formData: FormData = new FormData();
    formData.append('UnitPrice', product['Price']);
    formData.append('Name', product.Name.toString());
    formData.append('SellerId', product.SellerId.toString());
    formData.append('SellerName', product.SellerName.toString());
    formData.append('Category', product.Category.toString());
    formData.append('TC', product['Conditions']);
    formData.append('Quantity', product.Quantity.toString());
    formData.append('Description', product.Description.toString());
    formData.append('Image', product['ImageFile']);

    return this.httpClient
      .post(this.apiURL, formData, { headers: reqHeader })
      .pipe(
        map((res) => res),
        catchError(this.errorHandler)
      );
  }
  getAllProducts() {
    return this.httpClient.get(this.apiURL).pipe(
      map((res) => res),
      catchError(this.errorHandler)
    );
  }
  addProductToCart(product: Product) {
    this.subscribeProductMessage();
  }
  subscribeProductMessage() {
    this.rxStompService
      .watch('/topic/product')
      .pipe(takeUntil(this.destroy$))
      .subscribe((message: Message) => {
        console.log('Received from websocket: ' + message.body);
        this.kafkaProd = JSON.parse(message.body);
        ProductService.productsAddedToCart.set(
          this.kafkaProd.id,
          this.kafkaProd
        );
        localStorage.setItem(
          'product_' + this.kafkaProd.id,
          JSON.stringify(this.kafkaProd)
        );
        this.sharedService.updateCartCount(
          ProductService.productsAddedToCart.size
        );
        console.log(ProductService.productsAddedToCart);
      });
  }
  getProductFromCart() {
    return ProductService.productsAddedToCart;
  }
  sendProductMessage(product: Product) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.httpClient
      .post(this.kafkaApiURL, JSON.stringify(product), { headers: reqHeader })
      .pipe(
        map((res) => res),
        catchError(this.errorHandler)
      );
  }
  removeAllProductFromCart() {
    return localStorage.removeItem('product');
  }
  removeProductFromCart(product: any) {
    if (product.quantity <= 0) {
      localStorage.removeItem(product.id);
    }
  }
  errorHandler(error: Response) {
    console.log(error);
    return throwError(error);
  }
}
