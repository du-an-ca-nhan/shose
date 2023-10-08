package com.example.shose.server.repository;


import com.example.shose.server.dto.request.promotion.FindPromotionRequest;
import com.example.shose.server.dto.response.promotion.PromotionByIdRespone;
import com.example.shose.server.dto.response.promotion.PromotionRespone;
import com.example.shose.server.dto.response.promotion.PromotionByProDuctDetail;
import com.example.shose.server.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,String> {
    @Query(value = """ 
            select 
            po.id as id,
            po.code as code,
            po.name as name,
            po.value as value,
            po.start_date as startDate,
            po.end_date as endDate,
            po.status as status,
            po.created_date AS createdDate,
            po.last_modified_date AS lastModifiedDate
                       
            from promotion po 
            where
                        
                (:#{#req.code} IS NULL 
                or :#{#req.code} LIKE '' 
                or po.code like  %:#{#req.code}%)
            and 
                (:#{#req.name} IS NULL 
                or :#{#req.name} LIKE '' 
                or po.name like %:#{#req.name}% )
            and 
                (:#{#req.value} IS NULL 
                or :#{#req.value} LIKE '' 
                or po.value = :#{#req.value})
            and 
                (:#{#req.status} IS NULL 
                or :#{#req.status} LIKE ''
                or po.status = :#{#req.status} )
            and (
                (:#{#req.startDate} IS NULL 
                or :#{#req.startDate} LIKE ''
                or :#{#req.endDate} IS NULL 
                or :#{#req.endDate} LIKE '')           
                or((po.start_date>= :#{#req.startDate} and  po.start_date<= :#{#req.endDate}) and(po.end_date>= :#{#req.startDate} and po.end_date<= :#{#req.endDate} )) )
            GROUP BY po.id
            ORDER BY po.last_modified_date DESC  
            """,
            nativeQuery = true )
    List<PromotionRespone> getAllPromotion(@Param("req") FindPromotionRequest req);
    @Query(value = """
               SELECT               
                    po.id as id,
                    po.code as code,
                    po.name as name,
                    po.value as value,
                    po.start_date as startDate,
                    po.end_date as endDate,
                    po.status as status,
                    (select GROUP_CONCAT(DISTINCT pd.id ) from product_detail pd join promotion_product_detail ppd on pd.id = ppd.id_product_detail
                     where ppd.status ='DANG_SU_DUNG'and ppd.id_promotion = po.id) as productDetail,
                     GROUP_CONCAT(DISTINCT pd.id ) as productDetailUpdate,
                    GROUP_CONCAT(DISTINCT p.id) as product,
                    GROUP_CONCAT(DISTINCT ppd.id) AS promotionProductDetail
                
            FROM promotion po 
              LEFT JOIN promotion_product_detail ppd on po.id = ppd.id_promotion
              LEFT JOIN product_detail pd on pd.id = ppd.id_product_detail
               LEFT JOIN product p on p.id = pd.id_product
               where po.id = :id
               group by po.id
               """,nativeQuery = true )
    PromotionByIdRespone getByIdPromotion(@Param("id") String id);
    @Query(value = """
                 select
                  i.name AS image,
                  p.code AS code,
                  p.name AS name,
                  pr.name AS namePromotion,
                  pr.value AS valuePromotion,
                  ppd.status AS statusPromotion
                  from product_detail pd 
                 JOIN image i on i.id_product_detail = pd.id
                  JOIN product p on p.id = pd.id_product
                 JOIN promotion_product_detail ppd on ppd.id_product_detail = pd.id
                 JOIN promotion pr on pr.id = ppd.id_promotion
                 
                 where pd.id =:id
            """,nativeQuery = true)
    List<PromotionByProDuctDetail> getByIdProductDetail(@Param("id")String id);

    @Query("SELECT po FROM Promotion po WHERE po.endDate < :currentDate")
    List<Promotion> findExpiredPromotions(@Param("currentDate") Long currentDate);
    @Query("SELECT po FROM Promotion po WHERE po.startDate = :currentDate")
    List<Promotion> findStartPromotions(@Param("currentDate") Long currentDate);
}
