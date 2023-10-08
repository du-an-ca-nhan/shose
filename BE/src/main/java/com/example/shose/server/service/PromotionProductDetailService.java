package com.example.shose.server.service;


import com.example.shose.server.entity.PromotionProductDetail;

public interface PromotionProductDetailService {
    PromotionProductDetail getByProductDetailAndPromotion(String idProductDetail, String idPromotion);
}
