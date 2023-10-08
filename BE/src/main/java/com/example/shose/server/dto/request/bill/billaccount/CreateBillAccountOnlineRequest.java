package com.example.shose.server.dto.request.bill.billaccount;


import com.example.shose.server.dto.request.bill.billcustomer.BillDetailOnline;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class CreateBillAccountOnlineRequest {

    private String userName;
    private String phoneNumber;
    private String address;

    private BigDecimal moneyShip;
    private BigDecimal itemDiscount;

    private BigDecimal totalMoney;
    private String paymentMethod;

    private List<BillDetailOnline> billDetail;
    private BigDecimal afterPrice;

    private String idVoucher;
    private String idAccount;
}
