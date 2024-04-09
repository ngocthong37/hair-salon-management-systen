package com.hairsalon.respository;

import com.hairsalon.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query("SELECT c FROM CartItem c WHERE c.cart.id = :cartId")
    List<CartItem> findAllCartItemByCartId(Integer cartId);

    @Query("SELECT c FROM CartItem c WHERE c.cart.id = :cartId AND c.productItem.id = :productItemId")
    CartItem findExistCartItem(Integer cartId, Integer productItemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    void deleteAllCartItemByCartId(@Param("cartId") Integer cartId);

}
