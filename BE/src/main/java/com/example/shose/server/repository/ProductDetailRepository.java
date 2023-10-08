package com.example.shose.server.repository;

import com.example.shose.server.dto.request.productdetail.CreateProductDetailRequest;
import com.example.shose.server.dto.request.productdetail.FindProductDetailRequest;
import com.example.shose.server.dto.response.ProductDetailDTOResponse;
import com.example.shose.server.dto.response.ProductDetailReponse;
import com.example.shose.server.dto.response.cart.ListSizeOfItemCart;
import com.example.shose.server.dto.response.productdetail.GetDetailProductOfClient;
import com.example.shose.server.dto.response.productdetail.GetProductDetail;
import com.example.shose.server.dto.response.productdetail.GetProductDetailByCategory;
import com.example.shose.server.dto.response.productdetail.GetProductDetailByProduct;
import com.example.shose.server.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {

    @Query(value = """
                SELECT
                   ROW_NUMBER() OVER (ORDER BY detail.last_modified_date DESC) AS stt,
                   detail.id AS id,
                   i.name AS image,
                   CONCAT(p.name ,'[ ',s2.name,' - ',c2.name,' ]') AS nameProduct,
                   detail.price AS price,
                   detail.created_date AS created_date,
                   detail.gender AS gender,
                   detail.status AS status,
                   si.name AS nameSize,
                   c.name AS nameCategory,
                   b.name AS nameBrand,
                   detail.quantity AS quantity,
                   AVG(pr.value) AS promotion,
                   detail.quantity,
                   s2.name AS size,
                   c2.code AS color,
                   detail.maqr AS QRCode
                FROM product_detail detail
                JOIN product p ON detail.id_product = p.id
                JOIN (
                    SELECT id_product_detail, MAX(id) AS max_image_id
                    FROM image
                    GROUP BY id_product_detail
                ) max_images ON detail.id = max_images.id_product_detail
                LEFT JOIN image i ON max_images.max_image_id = i.id
                JOIN sole s ON s.id = detail.id_sole
                JOIN material m ON detail.id_material = m.id
                JOIN category c ON detail.id_category = c.id
                JOIN brand b ON detail.id_brand = b.id
                LEFT JOIN promotion_product_detail ppd on detail.id = ppd.id_product_detail
                LEFT JOIN promotion pr on pr.id = ppd.id_promotion
                JOIN size s2 on detail.id_size = s2.id
                JOIN color c2 on detail.id_color = c2.id
                LEFT JOIN size si ON detail.id_size = si.id
                WHERE i.status = true  
                AND p.id = :#{#req.idProduct}
                AND  ( :#{#req.size} = 0 OR s2.name = :#{#req.size} OR :#{#req.size} = '' )
                AND  ( :#{#req.color} IS NULL OR c2.code LIKE %:#{#req.color}% OR :#{#req.color} LIKE '' )
                AND  ( :#{#req.brand} IS NULL OR b.name LIKE %:#{#req.brand}% OR :#{#req.brand} LIKE '' )
                AND  ( :#{#req.material} IS NULL OR :#{#req.material} LIKE '' OR m.name LIKE %:#{#req.material}% ) 
                AND  ( :#{#req.product} IS NULL OR :#{#req.product} LIKE ''  OR p.name LIKE %:#{#req.product}% ) 
                AND  ( :#{#req.sole} IS NULL  OR :#{#req.sole} LIKE '' OR s.name LIKE %:#{#req.sole}% )
                AND  ( :#{#req.category} IS NULL OR :#{#req.category} LIKE '' OR c.name LIKE %:#{#req.category}% )
                AND  ( :#{#req.status} IS NULL   OR :#{#req.status} LIKE '' OR detail.status LIKE :#{#req.status} )
                AND  ( :#{#req.gender} IS NULL OR :#{#req.gender} LIKE '' OR detail.gender LIKE :#{#req.gender} )
                AND  ( :#{#req.minPrice} IS NULL OR detail.price >= :#{#req.minPrice} ) 
                AND  ( :#{#req.maxPrice} IS NULL OR detail.price <= :#{#req.maxPrice} )
                GROUP BY detail.id, i.name, p.name, s2.name, c2.name, detail.price, detail.created_date, detail.gender, detail.status, si.name, c.name, b.name, detail.quantity, s2.name, c2.code
                ORDER BY detail.last_modified_date DESC 
            """, nativeQuery = true)
    List<ProductDetailReponse> getAll(@Param("req") FindProductDetailRequest req);

    @Query(value = """

            SELECT
                detail.id AS id,
                i.name AS image,
                p.code AS codeProduct,
                p.name AS nameProduct,
                detail.price AS price,
                sum(pr.value) AS value,
                detail.created_date AS created_date,
                detail.gender AS gender,
                detail.status AS status,
                co.code AS codeColor,
                s.name AS nameSize
                       
            FROM product_detail detail
                     LEFT JOIN promotion_product_detail ppd on detail.id = ppd.id_product_detail
                     LEFT JOIN promotion pr on pr.id = ppd.id_promotion
                     JOIN product p on detail.id_product = p.id
                     JOIN (
                SELECT id_product_detail, MAX(id) AS max_image_id
                FROM image
                GROUP BY id_product_detail
            ) max_images ON detail.id = max_images.id_product_detail
                     LEFT JOIN image i ON max_images.max_image_id = i.id
                     JOIN color co on co.id = detail.id_color
                     JOIN size s on s.id = detail.id_size
            where p.id = :id
            group by detail.id
             """, nativeQuery = true)
    List<GetProductDetailByProduct> getByIdProduct(@Param("id") String id);


    @Query(value = """
             SELECT  ROW_NUMBER() OVER (ORDER BY detail.last_modified_date DESC) AS stt,
                    detail.id AS id,
                    i.name AS image,
                    p.code AS codeProduct,
                    p.name AS nameProduct,
                     pr.value AS promotion,
                    s.name AS nameSole,
                    m.name AS nameMaterial,
                    c.name AS nameCategory,
                    b.name AS nameBrand,
                    detail.quantity,
                    detail.price AS price,
                    detail.created_date AS created_date,
                    detail.gender AS gender,
                    detail.status AS status,
                    si.name AS nameSize,
                    col.code AS color,
                    detail.maqr AS QRCode
             FROM product_detail detail
             LEFT JOIN product p on detail.id_product = p.id
             LEFT JOIN image i on detail.id = i.id_product_detail
             LEFT JOIN sole s ON s.id = detail.id_sole
             LEFT JOIN material m ON detail.id_material = m.id
             LEFT JOIN promotion_product_detail ppd on detail.id = ppd.id_product_detail
             LEFT JOIN promotion pr on pr.id = ppd.id_promotion
             LEFT JOIN category c ON detail.id_category = c.id
             LEFT JOIN brand b ON detail.id_brand = b.id
             LEFT JOIN color col ON detail.id_color = col.id
             LEFT JOIN size si ON detail.id_size = si.id
            where p.id = :id
            GROUP BY detail.id
            ORDER BY detail.last_modified_date DESC 
            """, nativeQuery = true)
    List<ProductDetailReponse> findAllByIdProduct(@Param("id") String id);

    @Query(value = """
            SELECT
                p.id as idProduct,
                pd.id as idProductDetail,
                REPLACE(c.code, '#', '%23') as codeColor,
                s.name as nameSize,
                GROUP_CONCAT(i.name) as image,
                p.name as nameProduct,
                pd.price as price,
                promotion_summary.valuePromotion as valuePromotion,
                pd.created_date as createdDate
                      
            FROM product_detail pd
                     LEFT JOIN (
                SELECT
                    pd.id as pd_id,
                    SUM(po.value) as valuePromotion
                FROM product_detail pd
                         LEFT JOIN promotion_product_detail ppd ON pd.id = ppd.id_product_detail
                         LEFT JOIN promotion po ON po.id = ppd.id_promotion
                GROUP BY pd.id
            ) promotion_summary ON pd.id = promotion_summary.pd_id
                      
                     JOIN product p ON pd.id_product = p.id
                     JOIN color c ON c.id = pd.id_color
                     JOIN size s ON s.id = pd.id_size
                     LEFT JOIN image i ON i.id_product_detail = pd.id
                     JOIN category ca on ca.id = pd.id_category
            where ca.id = :id 
            group by pd.id
                   """, nativeQuery = true)
    List<GetProductDetailByCategory> getProductDetailByCategory(@Param("id") String id);

    @Query(value = """
            SELECT
                p.id as idProduct,
                pd.id as idProductDetail,
                REPLACE(c.code, '#', '%23') as codeColor,
                s.name as nameSize,
                GROUP_CONCAT(i.name) as image,
                p.name as nameProduct,
                pd.price as price,
                promotion_summary.valuePromotion as valuePromotion,
                pd.created_date as createdDate
                        
            FROM product_detail pd
                     LEFT JOIN (
                SELECT
                    pd.id as pd_id,
                    SUM(po.value) as valuePromotion,
                    ppd.status as status
                FROM product_detail pd
                         LEFT JOIN promotion_product_detail ppd ON pd.id = ppd.id_product_detail
                         LEFT JOIN promotion po ON po.id = ppd.id_promotion
                        
                GROUP BY pd.id , ppd.status
            ) promotion_summary ON pd.id = promotion_summary.pd_id
                        
                     JOIN product p ON pd.id_product = p.id
                     JOIN color c ON c.id = pd.id_color
                     JOIN size s ON s.id = pd.id_size
                     LEFT JOIN image i ON i.id_product_detail = pd.id
            where promotion_summary.status ='DANG_SU_DUNG'
            group by pd.id
                """, nativeQuery = true)
    Page<GetProductDetail> getProductDetailHavePromotion(Pageable pageable);

    @Query(value = """
            SELECT
                p.id as idProduct,
                pd.id as idProductDetail,
                REPLACE(c.code, '#', '%23') as codeColor,
                s.name as nameSize,
                GROUP_CONCAT(i.name) as image,
                p.name as nameProduct,
                pd.price as price,
                promotion_summary.valuePromotion as valuePromotion,
                pd.created_date as createdDate
                      
            FROM product_detail pd
                     LEFT JOIN (
                SELECT
                    pd.id as pd_id,
                    SUM(po.value) as valuePromotion
                FROM product_detail pd
                         LEFT JOIN promotion_product_detail ppd ON pd.id = ppd.id_product_detail
                         LEFT JOIN promotion po ON po.id = ppd.id_promotion
                WHERE  ppd.status = 'DANG_SU_DUNG' OR ppd.status IS NULL
                GROUP BY pd.id
            ) promotion_summary ON pd.id = promotion_summary.pd_id
                      
                     JOIN product p ON pd.id_product = p.id
                     JOIN color c ON c.id = pd.id_color
                     JOIN size s ON s.id = pd.id_size
                     LEFT JOIN image i ON i.id_product_detail = pd.id
            WHERE DATE_FORMAT(FROM_UNIXTIME(pd.created_date / 1000), '%Y-%m-%d %H:%i:%s') between DATE_SUB(NOW(), INTERVAL 15 DAY)  and  NOW()
            GROUP BY pd.id
                      
                  """, nativeQuery = true)
    Page<GetProductDetail> getProductDetailNew(Pageable pageable);

    @Query(value = """
            SELECT
                p.id as idProduct,
                pd.id as idProductDetail,
                REPLACE(c.code, '#', '%23') as codeColor,
                s.name as nameSize,
                GROUP_CONCAT(i.name) as image,
                p.name as nameProduct,
                pd.price as price,
                promotion_summary.valuePromotion as valuePromotion,
                pd.created_date as createdDate
            FROM product_detail pd
                     LEFT JOIN (
                SELECT
                    pd.id as pd_id,
                    SUM(po.value) as valuePromotion
                FROM product_detail pd
                         LEFT JOIN promotion_product_detail ppd ON pd.id = ppd.id_product_detail
                         LEFT JOIN promotion po ON po.id = ppd.id_promotion
                WHERE  ppd.status = 'DANG_SU_DUNG' OR ppd.status IS NULL
                GROUP BY pd.id
            ) promotion_summary ON pd.id = promotion_summary.pd_id
                       
                     JOIN product p ON pd.id_product = p.id
                     JOIN color c ON c.id = pd.id_color
                     JOIN size s ON s.id = pd.id_size
                     LEFT JOIN image i ON i.id_product_detail = pd.id
                     JOIN bill_detail bd on pd.id = bd.id_product_detail
                       
            GROUP BY pd.id
            having sum(bd.quantity) >5
            order by sum(bd.quantity)  desc
                 """, nativeQuery = true)
    Page<GetProductDetail> getProductDetailSellMany(Pageable pageable);

    @Query(value = """
            SELECT
                pd.id as idProductDetail,
                GROUP_CONCAT(i.name)as image,
                p.name as nameProduct,
                pd.price as price,
                pd.quantity as quantity,
                c.code as codeColor,
                s.name as nameSize
            from product_detail pd
                     left JOIN image i on i.id_product_detail = pd.id
                     JOIN product p on pd.id_product = p.id
                     JOIN color c on c.id = pd.id_color
                     JOIN size s on s.id = pd.id_size
            where pd.id = :id
                 """, nativeQuery = true)
    GetDetailProductOfClient getDetailProductOfClient(@Param("id") String id);


    @Query(value = """
                SELECT
                   detail.id AS id,
                   i.name AS image,
                   p.name AS nameProduct,
                   detail.description AS description,
                   b.id AS idBrand,
                   s.id AS idSole,
                   c.id AS idCategory,
                   detail.status AS status,
                   m.id AS idMaterial,
                   detail.gender AS gender,
                   detail.quantity AS quantity,
                   detail.price AS price,
                   c2.id AS idCode,
                   s2.id AS idSize,
                   detail.maqr AS QRCode,
                   p2.value AS promotion
                FROM product_detail detail
                JOIN product p ON detail.id_product = p.id
                JOIN (
                    SELECT id_product_detail, MAX(id) AS max_image_id
                    FROM image
                    GROUP BY id_product_detail
                ) max_images ON detail.id = max_images.id_product_detail
                LEFT JOIN image i ON max_images.max_image_id = i.id
                JOIN sole s ON s.id = detail.id_sole
                JOIN material m ON detail.id_material = m.id
                JOIN category c ON detail.id_category = c.id
                JOIN brand b ON detail.id_brand = b.id
                JOIN size s2 on detail.id_size = s2.id
                JOIN color c2 on detail.id_color = c2.id
                LEFT JOIN size si ON detail.id_size = si.id
                LEFT JOIN promotion_product_detail ppd ON detail.id = ppd.id_product_detail
                LEFT JOIN promotion p2 ON ppd.id_promotion = p2.id
                WHERE  detail.id = :id
            """, nativeQuery = true)
    ProductDetailDTOResponse getOneById(@Param("id") String id);

    @Query(value = """
                SELECT
                    s.name as nameSize,
                    p.id as idProduct,
                    REPLACE(c.code, '#','%23') as codeColor
                FROM product_detail detail
                join product p on detail.id_product = p.id
                join size s on detail.id_size = s.id
                join color c on detail.id_color = c.id
                where p.id = :idProduct and c.code = :codeColor
            """, nativeQuery = true)
    List<ListSizeOfItemCart> listSizeByProductAndColor(@Param("idProduct") String idProduct, @Param("codeColor") String codeColor);


    @Query(value = """
                SELECT
                   ROW_NUMBER() OVER (ORDER BY detail.last_modified_date DESC) AS stt,
                   detail.id AS id,
                   i.name AS image,
                   CONCAT(p.name ,'[ ',s2.name,' - ',c2.name,' ]') AS nameProduct,
                   detail.price AS price,
                   detail.created_date AS created_date,
                   detail.gender AS gender,
                   detail.status AS status,
                   si.name AS nameSize,
                   c.name AS nameCategory,
                   b.name AS nameBrand,
                   detail.quantity AS quantity,
                   AVG(pr.value) AS promotion,
                   detail.quantity,
                   s2.name AS size,
                   c2.code AS color,
                   detail.maqr AS QRCode
                FROM product_detail detail
                JOIN product p ON detail.id_product = p.id
                JOIN (
                    SELECT id_product_detail, MAX(id) AS max_image_id
                    FROM image
                    GROUP BY id_product_detail
                ) max_images ON detail.id = max_images.id_product_detail
                LEFT JOIN image i ON max_images.max_image_id = i.id
                JOIN sole s ON s.id = detail.id_sole
                JOIN material m ON detail.id_material = m.id
                JOIN category c ON detail.id_category = c.id
                JOIN brand b ON detail.id_brand = b.id
                LEFT JOIN promotion_product_detail ppd on detail.id = ppd.id_product_detail
                LEFT JOIN promotion pr on pr.id = ppd.id_promotion
                JOIN size s2 on detail.id_size = s2.id
                JOIN color c2 on detail.id_color = c2.id
                LEFT JOIN size si ON detail.id_size = si.id
                WHERE i.status = true  
                AND  s2.name = :#{#req.size} 
                AND  c2.code LIKE %:#{#req.color}% 
                AND  b.id = :#{#req.brandId} 
                AND  m.id = :#{#req.materialId} 
                AND  p.name LIKE %:#{#req.productId}% 
                AND  s.id = :#{#req.soleId}  
                AND  c.id = :#{#req.categoryId} 
                AND  detail.gender LIKE :#{#req.gender}  
                GROUP BY detail.id, i.name, p.name, s2.name, c2.name, detail.price, detail.created_date, detail.gender, detail.status, si.name, c.name, b.name, detail.quantity, s2.name, c2.code
                ORDER BY detail.last_modified_date DESC 
            """, nativeQuery = true)
    ProductDetailReponse getOneProductDetailByAll(@Param("req") CreateProductDetailRequest req);

}