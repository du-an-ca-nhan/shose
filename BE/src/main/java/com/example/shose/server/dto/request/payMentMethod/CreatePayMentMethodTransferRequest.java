package com.example.shose.server.dto.request.payMentMethod;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CreatePayMentMethodTransferRequest {

    public String vnp_Ammount ;
    public String vnp_OrderInfo = "Thanh toán hóa đơn";
    public String vnp_OrderType = "Thanh toán hóa đơn";
    public String vnp_TxnRef;

}
