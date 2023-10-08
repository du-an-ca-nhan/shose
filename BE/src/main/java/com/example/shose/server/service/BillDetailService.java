package com.example.shose.server.service;

import com.example.shose.server.dto.request.billdetail.CreateBillDetailRequest;
import com.example.shose.server.dto.request.billdetail.RefundProductRequest;
import com.example.shose.server.dto.response.billdetail.BillDetailResponse;
import com.example.shose.server.entity.BillDetail;

import java.util.List;


public interface BillDetailService {

    List<BillDetailResponse> findAllByIdBill(String idBill);

    BillDetailResponse findBillById(String id);

    BillDetail refundProduct(RefundProductRequest request);

    String create(CreateBillDetailRequest request);

    String update(String id, CreateBillDetailRequest request);

    boolean delete(String id, String productDetail);
}
