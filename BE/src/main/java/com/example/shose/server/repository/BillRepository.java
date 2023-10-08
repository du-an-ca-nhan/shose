package com.example.shose.server.repository;

import com.example.shose.server.dto.request.bill.FindNewBillCreateAtCounterRequest;
import com.example.shose.server.dto.request.statistical.FindBillDateRequest;
import com.example.shose.server.dto.response.bill.BillResponseAtCounter;
import com.example.shose.server.dto.response.statistical.StatisticalBestSellingProductResponse;
import com.example.shose.server.dto.response.statistical.StatisticalBillDateResponse;
import com.example.shose.server.dto.response.statistical.StatisticalDayResponse;
import com.example.shose.server.dto.response.statistical.StatisticalMonthlyResponse;
import com.example.shose.server.dto.response.statistical.StatisticalStatusBillResponse;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.dto.request.bill.BillRequest;
import com.example.shose.server.dto.response.bill.BillResponse;
import com.example.shose.server.dto.response.bill.UserBillResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
        @Query(value = """
                SELECT  ROW_NUMBER() OVER( ORDER BY bi.created_date DESC ) AS stt, bi.id, bi.code, bi.created_date, bi.user_name AS userName ,  usem.full_name AS nameEmployees , bi.type, bi.status_bill, bi.total_money, bi.item_discount  FROM bill bi
                LEFT JOIN account ac ON ac.id = bi.id_account
                LEFT JOIN account em ON em.id = bi.id_employees
                LEFT JOIN customer cu ON cu.id = bi.id_customer
                LEFT JOIN user usac ON usac.id = ac.id_user
                LEFT JOIN user usem ON usem.id = em.id_user
                WHERE  ( :#{#request.startTime} = 0
                         OR bi.created_date >= :#{#request.startTime}  )
                AND ( :#{#request.endTime} = 0
                         OR bi.created_date <= :#{#request.endTime}  )
                 AND ( :#{#request.startDeliveryDate} = 0
                         OR bi.delivery_date >= :#{#request.startDeliveryDate}  )
                AND ( :#{#request.endDeliveryDate} = 0
                         OR bi.delivery_date <= :#{#request.endDeliveryDate}  )         
                AND ( :#{#request.converStatus} IS NULL
                         OR :#{#request.converStatus} LIKE '[]'
                         OR bi.status_bill IN (:#{#request.status}))
                AND ( :#{#request.key} IS NULL
                         OR :#{#request.key} LIKE ''
                         OR bi.code LIKE %:#{#request.key}% 
                         OR bi.user_name LIKE %:#{#request.key}% 
                         OR usem.full_name LIKE %:#{#request.key}% 
                         OR bi.phone_number LIKE %:#{#request.key}% )
                AND ( :#{#request.type} IS NULL
                         OR :#{#request.type} LIKE ''
                         OR bi.type = :#{#request.type})
                ORDER BY bi.created_date DESC
                            
                """, nativeQuery = true)
        List<BillResponse> getAll( BillRequest request);

        @Query(value = """
                SELECT  ROW_NUMBER() OVER( ORDER BY bi.created_date DESC ) AS stt, bi.id, bi.code, bi.created_date, IF(usac.full_name IS NULL, cu.full_name, usac.full_name )  AS userName ,   bi.status_bill, bi.total_money, bi.item_discount, COUNT(bide.quantity) AS quantity FROM bill bi
                            LEFT JOIN account ac ON ac.id = bi.id_account
                            LEFT JOIN bill_detail bide ON bide.id_bill = bi.id
                            LEFT JOIN account em ON em.id = bi.id_employees
                            LEFT JOIN customer cu ON cu.id = bi.id_customer
                            LEFT JOIN user usac ON usac.id = ac.id_user
                            LEFT JOIN user usem ON usem.id = em.id_user
                WHERE bi.type = 'OFFLINE'
                AND bi.status_bill = 'TAO_HOA_DON'
                AND ( :#{#request.key} IS NULL
                         OR :#{#request.key} LIKE ''
                         OR bi.user_name LIKE :#{#request.key}
                         OR bi.code LIKE :#{#request.key}
                         OR bi.phone_number LIKE :#{#request.key})
               GROUP BY   bi.id, bi.code, bi.created_date, IF(usac.full_name IS NULL, cu.full_name, usac.full_name ) ,   bi.status_bill, bi.total_money, bi.item_discount 
                ORDER BY bi.created_date DESC          
                """, nativeQuery = true)
        List<BillResponseAtCounter> findAllBillAtCounterAndStatusNewBill(FindNewBillCreateAtCounterRequest request);

        @Query(value = """
                SELECT  ROW_NUMBER() OVER( ORDER BY bi.created_date ASC ) AS stt, IF(bi.id_account IS NULL, cu.id, usac.id )  AS id ,  IF(usac.full_name IS NULL, cu.full_name, usac.full_name )  AS userName   FROM bill bi
                             LEFT JOIN account ac ON ac.id = bi.id_account
                             LEFT JOIN customer cu ON cu.id = bi.id_customer
                             LEFT JOIN user usac ON usac.id = ac.id_user
                             ORDER BY bi.created_date
                """, nativeQuery = true)
        List<UserBillResponse> getAllUserInBill();

        @Query(value = """
                SELECT bi.id FROM bill bi
                LEFT JOIN bill_detail bide ON bide.id_bill = bi.id
                GROUP BY bi.id
                HAVING COUNT(bide.id) = 0
                """, nativeQuery = true)
        List<String> getAllBillTrash();


    @Query(value = """
            SELECT   
            COUNT(DISTINCT b.id) AS totalBill,
            SUM(b.total_money) AS totalBillAmount,
            SUM(bd.quantity) AS totalProduct
            FROM bill b JOIN bill_detail bd ON b.id = bd.id_bill
            WHERE
            b.completion_date >= :startOfMonth AND b.completion_date <= :endOfMonth
            AND b.status_bill = 'THANH_CONG';
            
                                         
              """, nativeQuery = true)

        List<StatisticalMonthlyResponse> getAllStatisticalMonthly(@Param("startOfMonth") Long startOfMonth, @Param("endOfMonth") Long endOfMonth);

    @Query(value = """
            SELECT
                COUNT(id) AS totalBillToday,
                SUM(total_money) AS totalBillAmountToday
            FROM
                bill
            WHERE
                completion_date >= :currentDate
                AND status_bill like 'THANH_CONG';                       
                          """, nativeQuery = true)
    List<StatisticalDayResponse> getAllStatisticalDay(@Param("currentDate") Long currentDate);


        @Query(value = """
              SELECT
                  status_bill AS statusBill,
                  COUNT(*) AS totalStatusBill
              FROM
                  bill
              GROUP BY
                  statusBill;                   
                              """, nativeQuery = true)
        List<StatisticalStatusBillResponse> getAllStatisticalStatusBill();

    @Query(value = """
   SELECT
       i.name AS image,
       p.name  AS nameProduct,
       pd.price AS price,
       SUM(bd.quantity) AS sold,
       SUM(bd.price) AS sales
   FROM bill_detail bd
            JOIN bill b on bd.id_bill = b.id
            JOIN product_detail pd on pd.id = bd.id_product_detail
            JOIN product p on pd.id_product = p.id
            JOIN (SELECT id_product_detail, MAX(id) AS max_image_id
                  FROM image
                  GROUP BY id_product_detail) max_images ON pd.id = max_images.id_product_detail
            LEFT JOIN image i ON max_images.max_image_id = i.id
   WHERE bd.id_product_detail IS NOT NULL AND b.status_bill like 'THANH_CONG'
   GROUP BY image, nameProduct, price
   ORDER BY sold desc
                                      """, nativeQuery = true)
    List<StatisticalBestSellingProductResponse> getAllStatisticalBestSellingProduct();

    @Query(value = """
    SELECT
        completion_date AS billDate,
        COUNT(*) AS totalBillDate
    FROM
        bill
    WHERE   (completion_date >= :#{#req.startDate} AND completion_date <= :#{#req.endDate} )
        AND (status_bill like 'THANH_CONG')
    GROUP BY billDate
    ORDER BY completion_date ASC;
                          """, nativeQuery = true)
    List<StatisticalBillDateResponse> getAllStatisticalBillDate(@Param("req") FindBillDateRequest req);
    Optional<Bill> findByCode(String code);
}
