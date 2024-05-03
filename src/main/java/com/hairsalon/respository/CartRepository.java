package com.hairsalon.respository;

import com.hairsalon.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT A.id AS cartID FROM Cart A " +
            "WHERE A.customer.id= :customerId")
    Integer findCartIdsByCustomerId(@Param("customerId") int customerId);
}
