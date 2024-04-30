package com.hairsalon.respository;

import com.hairsalon.entity.ServiceHair;
import com.hairsalon.model.HairServiceModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceHairRepository extends JpaRepository<ServiceHair, Integer> {
    @Query(value = "SELECT S FROM ServiceHair S WHERE S.serviceName LIKE CONCAT('%', :serviceName, '%')")
    List<ServiceHair> findByPartialServiceName(@Param("serviceName") String serviceName);

    @Query(value = "SELECT S FROM ServiceHair S WHERE S.status = 'OK'")
    List<ServiceHair> findAllServiceHair();

    @Transactional
    @Modifying
    @Query("UPDATE ServiceHair s SET s.url = :image where s.id = :serviceHairId")
    void updateImage(String image, Integer serviceHairId);



}
