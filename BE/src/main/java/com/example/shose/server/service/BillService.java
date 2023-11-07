package com.example.shose.server.service;


import com.example.shose.server.dto.request.bill.BillRequest;
import com.example.shose.server.dto.request.bill.ChangAllStatusBillByIdsRequest;
import com.example.shose.server.dto.request.bill.ChangStatusBillRequest;
import com.example.shose.server.dto.request.bill.CreateBillOfflineRequest;
import com.example.shose.server.dto.request.bill.CreateBillRequest;
import com.example.shose.server.dto.request.bill.FindNewBillCreateAtCounterRequest;
import com.example.shose.server.dto.request.bill.UpdateBillRequest;
import com.example.shose.server.dto.request.bill.billaccount.CreateBillAccountOnlineRequest;
import com.example.shose.server.dto.request.bill.billcustomer.CreateBillCustomerOnlineRequest;
import com.example.shose.server.dto.response.bill.BillResponseAtCounter;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.dto.response.bill.BillResponse;
import com.example.shose.server.dto.response.bill.UserBillResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface BillService {

    List<BillResponse> getAll(BillRequest request);

    List<UserBillResponse> getAllUserInBill();

    List<BillResponseAtCounter> findAllBillAtCounterAndStatusNewBill(String id,FindNewBillCreateAtCounterRequest request);

    Bill  saveOnline(CreateBillRequest request);

    Bill CreateCodeBill(String idEmployees);

    boolean updateBillWait(CreateBillOfflineRequest request);

    Bill  save(String id,HttpServletRequest requests,  CreateBillOfflineRequest request);

    Bill updateBillOffline(String id, UpdateBillRequest bill);

    Bill detail(String id);

    Bill changedStatusbill(String id, String idEmployees, ChangStatusBillRequest request, HttpServletRequest requests);

    int countPayMentPostpaidByIdBill(String id);

    boolean changeStatusAllBillByIds(ChangAllStatusBillByIdsRequest request, HttpServletRequest requests, String idEmployees);

    Bill cancelBill(String id,  String idEmployees,ChangStatusBillRequest request, HttpServletRequest requests);

    String createBillCustomerOnlineRequest( CreateBillCustomerOnlineRequest request) ;

    String createBillAccountOnlineRequest( CreateBillAccountOnlineRequest request) ;

    boolean createFilePdf(String idBill, HttpServletRequest request);

    Bill findByCode(String code, String phoneNumber);
}
