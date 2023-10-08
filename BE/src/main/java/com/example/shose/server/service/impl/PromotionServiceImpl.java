package com.example.shose.server.service.impl;


import com.example.shose.server.dto.request.productdetail.IdProductDetail;
import com.example.shose.server.dto.request.promotion.CreatePromotionRequest;
import com.example.shose.server.dto.request.promotion.FindPromotionRequest;
import com.example.shose.server.dto.request.promotion.UpdatePromotionRequest;
import com.example.shose.server.dto.response.promotion.PromotionByIdRespone;
import com.example.shose.server.dto.response.promotion.PromotionByProDuctDetail;
import com.example.shose.server.dto.response.promotion.PromotionRespone;
import com.example.shose.server.entity.ProductDetail;
import com.example.shose.server.entity.Promotion;
import com.example.shose.server.entity.PromotionProductDetail;
import com.example.shose.server.entity.Voucher;
import com.example.shose.server.infrastructure.constant.Message;
import com.example.shose.server.infrastructure.constant.Status;
import com.example.shose.server.infrastructure.exception.rest.RestApiException;
import com.example.shose.server.repository.ProductDetailRepository;
import com.example.shose.server.repository.PromotionProductDetailRepository;
import com.example.shose.server.repository.PromotionRepository;
import com.example.shose.server.service.PromotionService;
import com.example.shose.server.util.RandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private PromotionProductDetailRepository promotionProductDetailRepository;

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());

    }

    @Override
    public List<PromotionRespone> getAll(FindPromotionRequest findPromotionRequest) {
        return promotionRepository.getAllPromotion(findPromotionRequest);
    }

    @Override
    public Promotion add(CreatePromotionRequest request) throws Exception {
        if(ObjectUtils.isEmpty(request.getIdProductDetails())){
            throw new RestApiException("Không có sản phẩm");
        }
        for (IdProductDetail x : request.getIdProductDetails()) {
            Optional<ProductDetail> optional = productDetailRepository.findById(x.getId());
            if(!optional.isPresent()){
                throw new RestApiException("Có sản phẩm không tồn tại");
            }
        }
        long currentSeconds = (System.currentTimeMillis() / 1000)*1000;
        Status status = (request.getStartDate() <= currentSeconds && currentSeconds <= request.getEndDate())
                ? Status.DANG_SU_DUNG
                : Status.KHONG_SU_DUNG;
        Promotion promotion = Promotion.builder()
                .code(request.getCode())
                .name(request.getName())
                .value(request.getValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(status).build();

        promotionRepository.save(promotion);


        for (IdProductDetail x : request.getIdProductDetails()) {
            Optional<ProductDetail> optional = productDetailRepository.findById(x.getId());
            PromotionProductDetail promotionProductDetail = new PromotionProductDetail();
            promotionProductDetail.setPromotion(promotion);
            promotionProductDetail.setProductDetail(optional.get());
            promotionProductDetail.setStatus(Status.DANG_SU_DUNG);
            promotionProductDetailRepository.save(promotionProductDetail);
        }
        return promotion;
    }

    @Override
    public Promotion update(UpdatePromotionRequest request) {
        Optional<Promotion> optional = promotionRepository.findById(request.getId());
        if (!optional.isPresent()) {
            throw new RestApiException("Khuyến mại không tồn tại");
        }
        for (IdProductDetail x : request.getIdProductDetails()) {
            Optional<ProductDetail> optional1 = productDetailRepository.findById(x.getId());
            if(!optional1.isPresent()){
                throw new RestApiException("Có sản phẩm không tồn tại");
            }
        }
        Promotion promotion = optional.get();
        promotion.setCode(request.getCode());
        promotion.setName(request.getName());
        promotion.setValue(request.getValue());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        if(request.getStartDate()<= ( System.currentTimeMillis() / 1000)*1000 && ( System.currentTimeMillis() / 1000)*1000 <=request.getEndDate()){
            promotion.setStatus(Status.DANG_SU_DUNG);
        }else{
            promotion.setStatus(Status.KHONG_SU_DUNG);
        }
        promotionRepository.save(promotion);

        PromotionByIdRespone promotionByIdRespone = promotionRepository.getByIdPromotion(request.getId());

        for (String idProductDetailOld : promotionByIdRespone.getProductDetailUpdate().split(",")) {
            boolean foundInNew = false;


            for (IdProductDetail idProductDetailNew : request.getIdProductDetails()) {
                if (idProductDetailNew.getId().contains(idProductDetailOld)) {
                    foundInNew = true;
                    break;
                }
            }

            if (!foundInNew) {
                PromotionProductDetail promotionProductDetail = promotionProductDetailRepository.getByProductDetailAndPromotion(idProductDetailOld, promotionByIdRespone.getId());
                promotionProductDetail.setStatus(Status.KHONG_SU_DUNG);
                promotionProductDetailRepository.save(promotionProductDetail);
            }else{
                PromotionProductDetail promotionProductDetail = promotionProductDetailRepository.getByProductDetailAndPromotion(idProductDetailOld, promotionByIdRespone.getId());
                promotionProductDetail.setStatus(Status.DANG_SU_DUNG);
                promotionProductDetailRepository.save(promotionProductDetail);
            }
        }

        for (IdProductDetail idProductDetailNew : request.getIdProductDetails()) {
            boolean foundInOld = false;

            for (String idProductDetailOld : promotionByIdRespone.getProductDetailUpdate().split(",")) {
                if (idProductDetailOld.contains(idProductDetailNew.getId())) {
                    foundInOld = true;
                    break;
                }
            }

            if (!foundInOld) {
                PromotionProductDetail promotionProductDetail = new PromotionProductDetail();
                promotionProductDetail.setPromotion(promotion);
                promotionProductDetail.setProductDetail(productDetailRepository.findById(idProductDetailNew.getId()).get());
                promotionProductDetail.setStatus(Status.DANG_SU_DUNG);
                promotionProductDetailRepository.save(promotionProductDetail);
            }
        }
        return promotion;
    }

    @Override
    public Promotion updateStatus(String id) {
        Optional<Promotion> optional = promotionRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RestApiException("Khuyến mại không tồn tại");
        }
        Promotion promotion = optional.get();
        if(promotion.getStatus().equals(Status.DANG_SU_DUNG)){
            promotion.setStatus(Status.KHONG_SU_DUNG);
        }else{
            promotion.setStatus(Status.DANG_SU_DUNG);
        }
        promotionRepository.save(promotion);
        return promotion;
    }


    @Override
    public Promotion getById(String id) {
        Promotion promotion = promotionRepository.findById(id).get();
        return promotion;
    }

    @Override
    public List<Promotion> expiredVoucher() {
        List<Promotion> expiredPromotions = promotionRepository.findExpiredPromotions(System.currentTimeMillis());

        for (Promotion promotion : expiredPromotions) {
            promotion.setStatus(Status.KHONG_SU_DUNG);
            promotionRepository.save(promotion);
        }
        return expiredPromotions;
    }

    @Override
    public List<Promotion> startVoucher() {
        List<Promotion> startPromotions = promotionRepository.findStartPromotions(System.currentTimeMillis());
        for (Promotion promotion : startPromotions) {
            promotion.setStatus(Status.DANG_SU_DUNG);
            promotionRepository.save(promotion);
        }
        return startPromotions;
    }

    @Override
    public PromotionByIdRespone getByIdPromotion(String id) {
        return promotionRepository.getByIdPromotion(id);
    }

    @Override
    public List<PromotionByProDuctDetail> getByIdProductDetail(String id) {
        return promotionRepository.getByIdProductDetail(id);
    }
}
