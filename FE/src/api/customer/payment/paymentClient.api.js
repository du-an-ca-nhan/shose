import { request } from "../../../helper/request";
export class PaymentClientApi {
  
  
    static paymentVnpay = ( data) => {
      return request({
        method: "POST",
        url: `/client/payment/payment-vnpay` ,
        data: data
      });
    };

    static changeQuantityProductAfterPayment = ( data) => {
      return request({
        method: "POST",
        url: `/client/payment/change-quantity-payment` ,
        data: data
      });
    };
  }