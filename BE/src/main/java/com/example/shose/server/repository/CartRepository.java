package com.example.shose.server.repository;

import com.example.shose.server.dto.response.cart.ListCart;
import com.example.shose.server.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartRepository extends JpaRepository<Cart,String> {
    Cart getCartByAccount_Id(String idAccount);

    @Query(value = """
               SELECT p.id as idProduct,
                      p.name as nameProduct,
                      pd.id as idProductDetail,
                      cd.id as idCartDetail,
                      s.name as nameSize,
                      REPLACE(c.code, '#','%23') as codeColor,
                      GROUP_CONCAT(i.name) as image,
                      cd.price as price,
                      cd.quantity as quantity
               FROM cart cart
                        JOIN account a on a.id = cart.id_account
                        JOIN cart_detail cd on cart.id = cd.id_cart
                        JOIN product_detail pd on cd.id_product_detail = pd.id
                        JOIN product p on pd.id_product = p.id
                        JOIN image i on pd.id = i.id_product_detail
                        JOIN color c on pd.id_color = c.id
                        JOIN size s on pd.id_size = s.id

               WHERE a.id = :idAccount 
               group by p.id, p.name, pd.id, cd.id, s.name, REPLACE(c.code, '#','%23'), cd.price, cd.quantity
               ORDER BY  cd.created_date desc
            """,nativeQuery = true)
    List<ListCart> getListCart(@Param("idAccount") String idAccount);
    @Query(value = "select sum(cd.quantity) from Cart cart" +
            " JOIN Account a on a.id = cart.account.id" +
            " JOIN CartDetail cd on cd.cart.id = cart.id" +
            " WHERE a.id = :idAccount")
    Integer quantityInCart(@Param("idAccount") String idAccount);



}





