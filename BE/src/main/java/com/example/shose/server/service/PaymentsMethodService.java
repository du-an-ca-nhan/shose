package com.example.shose.server.service;

import com.example.shose.server.dto.request.bill.billcustomer.BillDetailOnline;
import com.example.shose.server.dto.request.payMentMethod.CreatePayMentMethodTransferRequest;
import com.example.shose.server.dto.request.paymentsmethod.CreatePaymentsMethodRequest;
import com.example.shose.server.dto.request.paymentsmethod.QuantityProductPaymentRequest;
import com.example.shose.server.dto.response.payment.PayMentVnpayResponse;
import com.example.shose.server.entity.PaymentsMethod;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;


public interface PaymentsMethodService {

    List<PaymentsMethod> findByAllIdBill(String idBill);

    PaymentsMethod create(String idBill, String idEmployees, CreatePaymentsMethodRequest request);

    BigDecimal sumTotalMoneyByIdBill(String idBill);

    String payWithVNPAY(CreatePayMentMethodTransferRequest payModel, HttpServletRequest request) throws UnsupportedEncodingException;

    boolean refundVnpay(String idUser, String codeBill, HttpServletRequest request) ;

    boolean paymentSuccess( String idEmployees,PayMentVnpayResponse response, HttpServletRequest requests);

    boolean changeQuantityProduct(QuantityProductPaymentRequest request);


    boolean updatepayMent(String idBill,String idEmployees,List<String> ids);
    String payWithVNPAYOnline(CreatePayMentMethodTransferRequest payModel, HttpServletRequest request) throws UnsupportedEncodingException;
}
