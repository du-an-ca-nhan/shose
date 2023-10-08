package com.example.shose.server.service.impl;


import com.example.shose.server.dto.request.voucher.CreateVoucherRequest;
import com.example.shose.server.dto.request.voucher.FindVoucherRequest;
import com.example.shose.server.dto.request.voucher.UpdateVoucherRequest;
import com.example.shose.server.dto.response.voucher.VoucherRespone;
import com.example.shose.server.entity.Voucher;
import com.example.shose.server.infrastructure.constant.Status;
import com.example.shose.server.infrastructure.exception.rest.RestApiException;
import com.example.shose.server.repository.VoucherRepository;
import com.example.shose.server.service.VoucherService;
import com.example.shose.server.util.ConvertDateToLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;



    @Override
    public List<VoucherRespone> getAll(FindVoucherRequest findVoucherRequest) {
        return voucherRepository.getAllVoucher(findVoucherRequest);
    }


    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher add(CreateVoucherRequest request) {
        long currentSeconds = (System.currentTimeMillis() / 1000)*1000;
        Status status = (request.getStartDate() <= currentSeconds && currentSeconds <= request.getEndDate())
                ? Status.DANG_SU_DUNG
                : Status.KHONG_SU_DUNG;
           Voucher voucher = Voucher.builder()
                   .code(request.getCode())
                   .name(request.getName())
                   .value(request.getValue())
                   .quantity(request.getQuantity())
                   .startDate(request.getStartDate())
                   .endDate(request.getEndDate())
                   .status(status).build();
           return voucherRepository.save(voucher);
    }

    @Override
    public Voucher update(UpdateVoucherRequest request) {
        Optional<Voucher> optional = voucherRepository.findById(request.getId());
        if (!optional.isPresent()) {
            throw new RestApiException("Khuyến mãi không tồn tại");
        }

        Voucher voucher = optional.get();
        voucher.setCode(request.getCode());
        voucher.setName(request.getName());
        voucher.setValue(request.getValue());
        voucher.setQuantity(request.getQuantity());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        if(request.getStartDate()<= ( System.currentTimeMillis() / 1000)*1000 && ( System.currentTimeMillis() / 1000)*1000 <=request.getEndDate()){
            voucher.setStatus(Status.DANG_SU_DUNG);
        }else{
            voucher.setStatus(Status.KHONG_SU_DUNG);
        }

        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateStatus(String id) {
        Optional<Voucher> optional = voucherRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RestApiException("Khuyến mãi không tồn tại");
        }
        Voucher voucher = optional.get();
        if(voucher.getStatus().equals(Status.DANG_SU_DUNG)){
            voucher.setStatus(Status.KHONG_SU_DUNG);
        }else{
            voucher.setStatus(Status.DANG_SU_DUNG);
        }
        voucherRepository.save(voucher);
        return voucher;
    }

    @Override
    public Boolean delete(String id) {
        Optional<Voucher> voucher = voucherRepository.findById(id);
        voucherRepository.delete(voucher.get());
        return true;
    }

    @Override
    public Voucher getById(String id) {
        Voucher voucher = voucherRepository.findById(id).get();
        return voucher;
    }

    @Override
    public List<Voucher> expiredVoucher() {
        List<Voucher> expiredVouchers = voucherRepository.findExpiredVouchers(( System.currentTimeMillis() / 1000)*1000);
        for (Voucher voucher : expiredVouchers) {
            voucher.setStatus(Status.KHONG_SU_DUNG);
            voucherRepository.save(voucher);
        }
        return expiredVouchers;
    }

    @Override
    public List<Voucher> startVoucher() {
        List<Voucher> startVouchers = voucherRepository.findStartVouchers(( System.currentTimeMillis() / 1000)*1000);
        for (Voucher voucher : startVouchers) {
            voucher.setStatus(Status.DANG_SU_DUNG);
            voucherRepository.save(voucher);
        }
        return startVouchers;
    }


    @Override
    public Voucher getByCode(String code) {
        return voucherRepository.getByCode(code);
    }

    @Override
    public List<Voucher> getVoucherByIdAccount(String idAccount) {
        return voucherRepository.getVoucherByIdAccount(idAccount);
    }

    public static void main(String[] args) {
        System.out.println(new ConvertDateToLong().longToDate(1695313928194l));
        System.out.println(new ConvertDateToLong().longToDate(1695313940000l));
    }
}
