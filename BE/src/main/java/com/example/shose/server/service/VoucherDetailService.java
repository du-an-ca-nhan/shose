package com.example.shose.server.service;

import com.example.shose.server.dto.response.voucher.VoucherDetailCustomAtCountry;
import org.springframework.data.repository.query.Param;


public interface VoucherDetailService {

    VoucherDetailCustomAtCountry getVoucherDetailByIdBill( String idBill);
}
