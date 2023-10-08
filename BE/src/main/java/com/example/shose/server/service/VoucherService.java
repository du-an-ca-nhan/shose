package com.example.shose.server.service;


import com.example.shose.server.dto.request.voucher.CreateVoucherRequest;
import com.example.shose.server.dto.request.voucher.FindVoucherRequest;
import com.example.shose.server.dto.request.voucher.UpdateVoucherRequest;
import com.example.shose.server.dto.response.voucher.VoucherRespone;
import com.example.shose.server.entity.Voucher;

import java.util.List;

public interface VoucherService {

    List<VoucherRespone> getAll(FindVoucherRequest findVoucherRequest);
    List<Voucher> findAll();
    Voucher add(CreateVoucherRequest request);
    Voucher update(UpdateVoucherRequest request);
    Voucher updateStatus(String id);

    Boolean delete(String id);
    Voucher getById(String id);
    List<Voucher> expiredVoucher();
    List<Voucher> startVoucher();
    Voucher getByCode(String code);

    List<Voucher> getVoucherByIdAccount(String idAccount);
}
