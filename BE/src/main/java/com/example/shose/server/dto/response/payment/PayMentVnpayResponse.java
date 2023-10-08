package com.example.shose.server.dto.response.payment;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PayMentVnpayResponse {
    private String vnp_Amount;
    private String vnp_ResponseCode;
    public String vnp_TxnRef;
    public String vnp_OrderInfo;
    private String vnp_TransactionNo;
}
