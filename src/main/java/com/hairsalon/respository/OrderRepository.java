package com.hairsalon.respository;

import com.hairsalon.entity.Order;
import com.hairsalon.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "Select OD FROM Order OD where OD.customer.id = :customerId")
    List<Order> findAllOrderByCustomerId(@Param("customerId") Integer customerId);
}
