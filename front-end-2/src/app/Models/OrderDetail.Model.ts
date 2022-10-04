import { OrderItem } from './OrderItem.Model';

export interface OrderDetail {
  orderID: number;
  customerId: number;
  customerName: string;
  deliveryAddress: string;
  phone: number;
  orderPayMethod: string;
  paymentReferenceId: string;
  orderItems: OrderItem[];
}
