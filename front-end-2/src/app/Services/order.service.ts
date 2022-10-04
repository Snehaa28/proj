import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Http, Response } from '@angular/http';
import { Observable, of, throwError, pipe } from 'rxjs';
import { map, filter, catchError, mergeMap } from 'rxjs/operators';
import { AuthenticationService } from './authentication.service';
import { OrderDetail } from '../Models/OrderDetail.Model';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  public apiURL: string = 'http://localhost:8080/order/place-order';
  public apiURLUser: string = 'http://localhost:8080/user/userDetails';
  constructor(
    private httpClient: HttpClient,
    private authService: AuthenticationService
  ) {}

  getUserDetails(userName: string) {
    const params = new HttpParams().set('userName', userName);
    return this.httpClient.get(this.apiURLUser, { params }).pipe(
      map((res) => res),
      catchError(this.errorHandler)
    );
  }

  PlaceOrder(orderDetail: OrderDetail) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    // reqHeader.append('Content-Type', 'application/json');

    return this.httpClient.post(this.apiURL, orderDetail as OrderDetail).pipe(
      map((res) => res),
      catchError(this.errorHandler)
    );
  }

  errorHandler(error: Response) {
    console.log(error);
    return throwError(error);
  }
}
