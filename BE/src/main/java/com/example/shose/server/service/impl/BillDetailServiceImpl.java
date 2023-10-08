package com.example.shose.server.service.impl;

import com.example.shose.server.dto.request.billdetail.CreateBillDetailRequest;
import com.example.shose.server.dto.request.billdetail.RefundProductRequest;
import com.example.shose.server.dto.response.billdetail.BillDetailResponse;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.entity.BillDetail;
import com.example.shose.server.entity.BillHistory;
import com.example.shose.server.entity.ProductDetail;
import com.example.shose.server.entity.Size;
import com.example.shose.server.infrastructure.constant.Message;
import com.example.shose.server.infrastructure.constant.StatusBill;
import com.example.shose.server.infrastructure.exception.rest.RestApiException;
import com.example.shose.server.repository.BillDetailRepository;
import com.example.shose.server.repository.BillHistoryRepository;
import com.example.shose.server.repository.BillRepository;
import com.example.shose.server.repository.ProductDetailRepository;
import com.example.shose.server.repository.SizeRepository;
import com.example.shose.server.service.BillDetailService;
import com.example.shose.server.util.FormUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BillDetailServiceImpl implements BillDetailService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    @Autowired
    private SizeRepository sizeRepository;

    private FormUtils formUtils = new FormUtils();

    @Override
    public List<BillDetailResponse> findAllByIdBill(String idBill) {
        return billDetailRepository.findAllByIdBill(idBill);
    }

    @Override
    public BillDetailResponse findBillById(String id) {
        return billDetailRepository.findBillById(id);
    }

    @Override
    public BillDetail refundProduct(RefundProductRequest request) {
        Optional<BillDetail> billDetail = billDetailRepository.findById(request.getId());
        Optional<ProductDetail> productDetail = productDetailRepository.findById(request.getId());
        Optional<Bill> bill = billRepository.findById(request.getId());
        Optional<Size> size = sizeRepository.findByName(request.getSize());
        if (!size.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }

        if (billDetail.get().getQuantity() < request.getQuantity()) {
            throw new RestApiException(Message.ERROR_QUANTITY);
        }
        if(bill.get().getStatusBill() != StatusBill.DA_THANH_TOAN ||
                bill.get().getStatusBill() != StatusBill.THANH_CONG ||
                bill.get().getStatusBill() != StatusBill.TRA_HANG )
        {
            throw new RestApiException(Message.BILL_NOT_REFUND);
        }

        productDetail.get().setQuantity( productDetail.get().getQuantity() + request.getQuantity());

        billDetail.get().setStatusBill(StatusBill.TRA_HANG);
        billDetail.get().setQuantity(billDetail.get().getQuantity() - request.getQuantity());

        bill.get().setStatusBill(StatusBill.TRA_HANG);
        bill.get().setTotalMoney(new BigDecimal(request.getTotalMoney()));
        billRepository.save(bill.get());

        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatusBill(StatusBill.TRA_HANG);
        billHistory.setActionDescription(request.getNote());
        billHistoryRepository.save(billHistory);

        productDetailRepository.save(productDetail.get());

        return billDetailRepository.save(billDetail.get());
    }

    @Override
    public String create(CreateBillDetailRequest request) {
        Optional<Bill> bill = billRepository.findById(request.getIdBill());
        Optional<ProductDetail> productDetail = productDetailRepository.findById(request.getIdProduct());
        Optional<Size> size = sizeRepository.findByName(request.getSize());
        if (!size.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        if(!bill.isPresent()){
            throw new RestApiException(Message.BILL_NOT_EXIT);
        }
        if (!productDetail.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        if (productDetail.get().getQuantity() < request.getQuantity()) {
            throw new RestApiException(Message.ERROR_QUANTITY);
        }
        productDetail.get().setQuantity( productDetail.get().getQuantity() - request.getQuantity());
        productDetailRepository.save(productDetail.get());

        bill.get().setTotalMoney(bill.get().getTotalMoney().add(new BigDecimal(request.getPrice()).multiply(BigDecimal.valueOf(request.getQuantity()))));
        billRepository.save(bill.get());
        BillDetail billDetail = new BillDetail();
        billDetail.setStatusBill(StatusBill.TAO_HOA_DON);
        billDetail.setQuantity(request.getQuantity());
        billDetail.setPrice(new BigDecimal(request.getPrice()));
        billDetail.setProductDetail(productDetail.get());
        billDetail.setBill(bill.get());
        billDetailRepository.save(billDetail);
        return billDetail.getId();
    }
    @Override
    public String update(String id, CreateBillDetailRequest request) {
        Optional<Bill> bill = billRepository.findById(request.getIdBill());
        Optional<ProductDetail> productDetail = productDetailRepository.findById(request.getIdProduct());
        Optional<Size> size = sizeRepository.findByName(request.getSize());
        Optional<BillDetail> billDetail = billDetailRepository.findById(id);
        if (!size.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        if(!bill.isPresent()){
            throw new RestApiException(Message.BILL_NOT_EXIT);
        }
        if (!productDetail.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }

        if ((productDetail.get().getQuantity() + billDetail.get().getQuantity()) < request.getQuantity()) {
            throw new RestApiException(Message.ERROR_QUANTITY);
        }
        productDetail.get().setQuantity( (productDetail.get().getQuantity() + billDetail.get().getQuantity() ) - request.getQuantity());
        productDetailRepository.save(productDetail.get());


        bill.get().setTotalMoney(new BigDecimal(request.getTotalMoney()));
        billRepository.save(bill.get());
//        billDetail.get().setPrice(new BigDecimal(request.getPrice()));
        billDetail.get().setQuantity(request.getQuantity());
        billDetail.get().setStatusBill(StatusBill.TAO_HOA_DON);
        billDetailRepository.save(billDetail.get());
        return billDetail.get().getId();
    }

    @Override
    public boolean delete(String id, String productDetail) {
        Optional<BillDetail> billDetail = billDetailRepository.findById(id);
        if (!billDetail.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        Optional<ProductDetail> optional = productDetailRepository.findById(productDetail);
        if (!optional.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        Bill bill = billDetail.get().getBill();
        bill.setTotalMoney(bill.getTotalMoney().subtract(billDetail.get().getPrice()));
        billRepository.save(bill);

        optional.get().setQuantity(billDetail.get().getQuantity() + optional.get().getQuantity());
        productDetailRepository.save(optional.get());
        billDetailRepository.deleteById(id);
        return true;
    }

}
