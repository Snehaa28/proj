import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormControl,
} from '@angular/forms';
import { Product } from '../Models/Product.Model';
import { ProductService } from '../Services/product.service';
import { IAlert } from '../Models/IAlert';
import { OrderDetail } from '../Models/OrderDetail.Model';
import { Registration } from '../Models/User.Models';
import { AuthenticationService } from '../Services/authentication.service';
import { OrderService } from '../Services/order.service';
import { OrderItem } from '../Models/OrderItem.Model';
import { SharedService } from '../Services/shared.service';

@Component({
  selector: 'app-mycart',
  templateUrl: './mycart.component.html',
  styleUrls: ['./mycart.component.scss'],
})
export class MycartComponent implements OnInit {
  dafualtQuantity: number = 1;
  productsAddedToCart: Map<Number, Product> = new Map<Number, Product>();
  allTotal: number;
  currentUser: Registration;
  orderDetail: any;
  orderItem: OrderItem[];
  userName: string;
  public globalResponse: any;
  public alerts: Array<IAlert> = [];

  deliveryForm: FormGroup;

  constructor(
    private productService: ProductService,
    private fb: FormBuilder,
    private orderService: OrderService
  ) {
    this.productsAddedToCart = this.productService.getProductFromCart();
    for (let i in this.productsAddedToCart) {
      this.productsAddedToCart[i].quantity = 1;
    }
    this.productService.removeAllProductFromCart();
    this.calculteAllTotal(this.productsAddedToCart);
    this.userName = SharedService.currentUser;
    this.GetLoggedinUserDetails();
  }

  ngOnInit() {
    this.deliveryForm = this.fb.group({
      userName: ['', [Validators.required]],
      deliveryAddress: ['', [Validators.required]],
      phone: ['', [Validators.required]],
      email: ['', [Validators.required]],
      message: ['', []],
      amount: ['', [Validators.required]],
    });
  }
  onAddQuantity(product: Product) {
    //Get Product
    this.productsAddedToCart = this.productService.getProductFromCart();
    this.productsAddedToCart.get(product.id).quantity = product.quantity + 1;
    this.productService.removeAllProductFromCart();
    this.productService.addProductToCart(product);
    this.calculteAllTotal(this.productsAddedToCart);
    this.deliveryForm.controls['amount'].setValue(this.allTotal);
  }
  onRemoveQuantity(product: Product) {
    this.productsAddedToCart = this.productService.getProductFromCart();
    if (product.quantity - 1 >= 0) {
      this.productsAddedToCart.get(product.id).quantity = product.quantity - 1;
    }
    this.productService.removeAllProductFromCart();
    this.productService.addProductToCart(product);
    this.calculteAllTotal(this.productsAddedToCart);
    this.deliveryForm.controls['amount'].setValue(this.allTotal);
  }
  calculteAllTotal(allItems: Map<Number, Product>) {
    let total = 0;
    for (let [key, value] of allItems) {
      total = total + value.quantity * value.unitPrice;
    }
    this.allTotal = total;
  }

  GetLoggedinUserDetails() {
    this.orderService.getUserDetails(SharedService.currentUser).subscribe(
      (result: OrderDetail) => {
        this.globalResponse = result;
      },
      (error) => {
        //This is error part
        console.log(error.message);
        this.alerts.push({
          id: 2,
          type: 'danger',
          message:
            'Something went wrong while placing the order, Please try after sometime.',
        });
      },
      () => {
        this.currentUser = this.globalResponse;
        this.deliveryForm.controls['userName'].setValue(
          this.currentUser['userName']
        );
        this.deliveryForm.controls['deliveryAddress'].setValue(
          this.currentUser['address']
        );
        this.deliveryForm.controls['phone'].setValue(this.currentUser['phone']);
        this.deliveryForm.controls['email'].setValue(this.currentUser['email']);
        this.deliveryForm.controls['amount'].setValue(this.allTotal);
        const date: Date = new Date();
        var id = this.currentUser['id'];
        var name = this.currentUser['userName'];
        var day = date.getDate();
        var monthIndex = date.getMonth();
        var year = date.getFullYear();
        var minutes = date.getMinutes();
        var hours = date.getHours();
        var seconds = date.getSeconds();
        var dateTimeStamp =
          day.toString() +
          monthIndex.toString() +
          year.toString() +
          minutes.toString() +
          hours.toString() +
          seconds.toString();
        //  This is Success part
        this.orderDetail = {};
        //Orderdetail is object which hold all the value, which needs to be saved into database
        this.orderDetail.customerId = this.currentUser['id'];
        this.orderDetail.customerName = this.userName;
        this.orderDetail.deliveryAddress = this.deliveryForm.controls[
          'deliveryAddress'
        ].value;
        this.orderDetail.phone = this.deliveryForm.controls['phone'].value;

        this.orderDetail.paymentReferenceId = id + '-' + name + dateTimeStamp;
        this.orderDetail.orderPayMethod = 'Cash On Delivery';

        //Assigning the ordered item details
        this.orderItem = [];
        for (let [key, value] of this.productsAddedToCart) {
          this.orderItem.push({
            id: key as number,
            productID: value.id,
            sellerID: value.sellerId,
            productName: value.name,
            orderedQuantity: value.quantity,
            perUnitPrice: value.unitPrice,
            orderID: 0,
          });
        }
        //So now compelte object of order is
        this.orderDetail.orderItems = this.orderItem;
      }
    );
  }

  ConfirmOrder() {
    this.orderService.PlaceOrder(this.orderDetail).subscribe(
      (result) => {
        this.globalResponse = result;
      },
      (error) => {
        //This is error part
        console.log(error.message);
        this.alerts.push({
          id: 2,
          type: 'danger',
          message:
            'Something went wrong while placing the order, Please try after sometime.',
        });
      },
      () => {
        //  This is Success part
        //console.log(this.globalResponse);
        this.alerts.push({
          id: 1,
          type: 'success',
          message: 'Order has been placed succesfully.',
        });
      }
    );
  }
  public closeAlert(alert: IAlert) {
    const index: number = this.alerts.indexOf(alert);
    this.alerts.splice(index, 1);
  }
}
