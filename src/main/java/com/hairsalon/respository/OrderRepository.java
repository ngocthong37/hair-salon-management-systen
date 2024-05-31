package com.hairsalon.respository;

import com.hairsalon.entity.Order;
import com.hairsalon.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "Select OD FROM Order OD where OD.customer.id = :customerId")
    List<Order> findAllOrderByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT SUM(o.totalPrice) FROM Order o " +
            "WHERE (o.orderDate >= :startDate AND o.orderDate <= :endDate) AND o.orderStatus.id = 5")
    Double findRevenueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT YEAR(o.orderDate), MONTH(o.orderDate), SUM(o.totalPrice) " +
            "FROM Order o " +
            "WHERE (o.orderDate >= :startDate AND o.orderDate <= :endDate) AND o.orderStatus.id = 5 " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate)")
    List<Object[]> findMonthlyRevenueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
