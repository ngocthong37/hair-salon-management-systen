package com.hairsalon.respository;


import com.hairsalon.entity.ProductItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProductItemRepository extends JpaRepository<ProductItem, Integer> {
    @Query(value = "SELECT S FROM ProductItem S WHERE S.productItemName LIKE CONCAT('%', :productName, '%')")
    List<ProductItem> findByProductName(@Param("productName") String productName);

    @Query(value = "SELECT P FROM ProductItem P WHERE P.id = :id")
    Optional<ProductItem> findById(@Param("id") Integer id);

    @Query(value = "SELECT P FROM ProductItem P WHERE P.status = 'OK'")
    List<ProductItem> findAllProductItem();

    @Transactional
    @Modifying
    @Query("UPDATE ProductItem p SET p.imageUrl = :image where p.id = :productItemId")
    void updateImage(String image, Integer productItemId);



}
