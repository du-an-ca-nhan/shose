package com.example.shose.server.service;

import com.example.shose.server.dto.request.statistical.FindBillDateRequest;
import com.example.shose.server.dto.response.statistical.StatisticalBestSellingProductResponse;
import com.example.shose.server.dto.response.statistical.StatisticalBillDateResponse;
import com.example.shose.server.dto.response.statistical.StatisticalDayResponse;
import com.example.shose.server.dto.response.statistical.StatisticalMonthlyResponse;
import com.example.shose.server.dto.response.statistical.StatisticalStatusBillResponse;

import java.util.List;


public interface StatisticalService {
    List<StatisticalDayResponse> getAllStatisticalDay();
    List<StatisticalMonthlyResponse> getAllStatisticalMonth();
    List<StatisticalStatusBillResponse> getAllStatisticalStatusBill();
    List<StatisticalBestSellingProductResponse> getAllStatisticalBestSellingProduct();
    List<StatisticalBillDateResponse> getAllStatisticalBillDate(final FindBillDateRequest findBillDateRequest);
}
