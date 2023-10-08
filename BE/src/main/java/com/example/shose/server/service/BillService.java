package com.example.shose.server.service;


import com.example.shose.server.dto.request.bill.*;
import com.example.shose.server.dto.request.bill.billaccount.CreateBillAccountOnlineRequest;
import com.example.shose.server.dto.request.bill.billcustomer.CreateBillCustomerOnlineRequest;
import com.example.shose.server.dto.response.bill.BillResponseAtCounter;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.dto.response.bill.BillResponse;
import com.example.shose.server.dto.response.bill.UserBillResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;


public interface BillService {

    List<BillResponse> getAll(BillRequest request);

    List<UserBillResponse> getAllUserInBill();

    List<BillResponseAtCounter> findAllBillAtCounterAndStatusNewBill(FindNewBillCreateAtCounterRequest request);

    Bill  saveOnline(CreateBillRequest request);

    Bill CreateCodeBill(String idEmployees);

    boolean updateBillWait(CreateBillOfflineRequest request);

    Bill  save(String id,  CreateBillOfflineRequest request);

    Bill updateBillOffline(String id, UpdateBillRequest bill);

    Bill detail(String id);

    Bill changedStatusbill(String id, String idEmployees, ChangStatusBillRequest request);

    int countPayMentPostpaidByIdBill(String id);
    boolean changeStatusAllBillByIds(ChangAllStatusBillByIdsRequest request, String idEmployees);

    Bill cancelBill(String id,  String idEmployees,ChangStatusBillRequest request);
    String createBillCustomerOnlineRequest( CreateBillCustomerOnlineRequest request) ;
    String createBillAccountOnlineRequest( CreateBillAccountOnlineRequest request) ;
}
