package com.example.shose.server.repository;

import com.example.shose.server.dto.response.voucher.VoucherDetailCustomAtCountry;
import com.example.shose.server.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail,String> {

    @Modifying
    @Query(value = """
             DELETE FROM  voucher_detail
                   WHERE id_bill = :id
            """, nativeQuery = true)
    int deleteAllByIdBill(@Param("id") String idBill);

    @Query(value = """
          SELECT  vode.id_voucher, vode.after_price, vode.befor_price, vode.discount_price, CONCAT(vo.code , ' - ' , vo.name)  AS name   FROM voucher_detail vode
                           LEFT JOIN bill bi ON bi.id = vode.id_bill
                           LEFT JOIN voucher vo ON vo.id = vode.id_voucher
                           WHERE bi.id  = :id
                           ORDER BY vode.created_date
                           LIMIT 1;
            """, nativeQuery = true)
    VoucherDetailCustomAtCountry getVoucherDetailByIdBill(@Param("id") String idBill);
}
