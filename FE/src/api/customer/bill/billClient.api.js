import {request } from "../../../helper/request";
export class BillClientApi {
  static createBillOnline = (data) => {
    return request({
        method: "POST",
        url: `/client/bill`,
        data:data
      });
  };
  static createBillAccountOnline = (data) => {
    return request({
        method: "POST",
        url: `/client/bill/account`,
        data:data
      });
  };

  //  code bill Detail 
  static fetchDetailBill = (code,phoneNumber) => {
    return request({
      method: "GET",
      url: `/client/bill/`+ code + "/"+phoneNumber
    });
  };
  static fetchAllBillHistoryInBill = (id) => {
    return request({
      method: "GET",
      url: `/client/bill-history/`+ id
    });
  };
  static fetchAllBillDetailInBill = (id) => {
    return request({
      method: "GET",
      url: `/client/bill-detail/`+ id
    });
  };

  static fetchAllPayMentlInBill = (id) => {
    return request({
      method: "GET",
      url: `/client/payment/bill/`+ id
    });
  };

  static changeCancelStatusBill = (id, data) =>{
    return request({
      method: "PUT",
      url: `/client/bill/cancel-status/`+ id,
      params: data,
    });
  } 
}
