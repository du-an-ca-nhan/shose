package com.example.shose.server.repository;

import com.example.shose.server.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,String> {

    List<CartDetail> getCartDetailByCart_Id(String idCart);

    List<CartDetail> getCartDetailByCart_IdAndProductDetail_Id(String idCart,String idProductDeatail);
}
