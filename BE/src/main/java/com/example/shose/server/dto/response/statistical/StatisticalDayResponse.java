package com.example.shose.server.dto.response.statistical;

import org.springframework.beans.factory.annotation.Value;


public interface StatisticalDayResponse {
    @Value("#{target.totalBillToday}")
    Integer getTotalBillToday();
    @Value("#{target.totalBillAmountToday}")
    Integer getTotalBillAmountToday();
}
