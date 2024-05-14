package com.hairsalon.respository;

import com.hairsalon.entity.ReviewServiceHair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewServiceHair, Integer> {

    @Query("SELECT ap.id FROM Appointment ap WHERE ap.appointmentStatus.id = 4 AND ap.customer.id = :customerId AND ap.serviceHair.id = :serviceHairId")
    Integer findToReview(Integer serviceHairId, Integer customerId);

    @Query("SELECT rs FROM ReviewServiceHair rs where rs.serviceHair.id = :serviceHairId")
    List<ReviewServiceHair> findAllServiceByServiceId(Integer serviceHairId);

}
