package com.example.shose.server.dto.response.cart;


import org.springframework.beans.factory.annotation.Value;

public interface ListCart {
    @Value("#{target.nameSize}")
    String getNameSize();

    @Value("#{target.idProduct}")
    String getIdProduct();
    @Value("#{target.idCartDetail}")
    String getIdCartDetail();
    @Value("#{target.idProductDetail}")
    String getIdProductDetail();

    @Value("#{target.codeColor}")
    String getCodeColor();

    @Value("#{target.image}")
    String getImage();

    @Value("#{target.nameProduct}")
    String getNameProduct();

    @Value("#{target.price}")
    String getPrice();

    @Value("#{target.quantity}")
    String getQuantity();

}
