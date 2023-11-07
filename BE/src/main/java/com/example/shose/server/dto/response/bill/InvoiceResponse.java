package com.example.shose.server.dto.response.bill;

import com.example.shose.server.dto.request.paymentsmethod.CreatePaymentsMethodRequest;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private String phoneNumber;
    private String address;
    private String userName;
    private String code;
    private String itemDiscount;
    private String totalMoney;
    private String note;
    private String moneyShip;
    private String totalBill;
    private String totalPayment;
    private boolean checkShip;
    private String ship;
    private boolean method;
    private boolean typeBill;
    private String date;
    private Integer quantity;
    private String change;
    private List<InvoiceItemResponse> items;
    private List<InvoicePaymentResponse> paymentsMethodRequests;

}
