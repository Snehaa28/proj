export interface Product {
  id: number;
  name: string;
  description: string;
  billingAddress: string;
  unitPrice: number;
  category: string;
  quantity: number;
  imageFile: File;
  tC: string;
  sellerId: number;
  sellerName: string;
}
