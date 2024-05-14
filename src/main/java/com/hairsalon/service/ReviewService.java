package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hairsalon.entity.*;
import com.hairsalon.model.ReviewServiceModel;
import com.hairsalon.respository.AppointmentRepository;
import com.hairsalon.respository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public ResponseEntity<Object> add(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewServiceHair review = new ReviewServiceHair();
        try {
            if (json == null || json.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("ERROR", "Empty JSON", ""));
            }
            JsonNode jsonObjectAppointment = objectMapper.readTree(json);
            Integer customerId = jsonObjectAppointment.get("customerId") != null ?
                    Integer.parseInt(jsonObjectAppointment.get("customerId").asText()) : 1;
            Integer serviceId = jsonObjectAppointment.get("serviceId") != null ?
                    Integer.parseInt(jsonObjectAppointment.get("serviceId").asText()) : 1;
            Integer rating = jsonObjectAppointment.get("rating") != null ?
                    Integer.parseInt(jsonObjectAppointment.get("rating").asText()) : 1;
            String comment = jsonObjectAppointment.get("comment") != null ?
                    jsonObjectAppointment.get("comment").asText() : "";
            if (getAppointmentId(serviceId, customerId) == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("ERROR", "Can't review this service hair", ""));
            }
            User customer = new User();
            customer.setId(customerId);

            ServiceHair serviceHair = new ServiceHair();
            serviceHair.setId(serviceId);
            review.setCustomer(customer);
            review.setServiceHair(serviceHair);
            review.setRatingValue(rating);
            review.setComment(comment);
            ReviewServiceHair savedReviewServiceHair = reviewRepository.save(review);
            if (savedReviewServiceHair.getId() > 0) {
                Optional<Appointment> appointmentOptional = appointmentRepository.findById(getAppointmentId(serviceId, customerId));
                if (appointmentOptional.isPresent()) {
                    Appointment appointment = appointmentOptional.get();
                    AppointmentStatus appointmentStatus = new AppointmentStatus();
                    appointmentStatus.setId(5);
                    appointment.setAppointmentStatus(appointmentStatus);
                    appointmentRepository.save(appointment);
                }
                else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseObject("ERROR", "Appointment not found", ""));
                }
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("OK", "Successfully", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "An error occurred", e.getMessage()));
        }
    }

    public Integer getAppointmentId(Integer serviceHairId, Integer customerId) {
        Integer appointmentId = reviewRepository.findToReview(serviceHairId, customerId);
        if (appointmentId != null && appointmentId > 0) {
            return appointmentId;
        }
        return 0;
    }

    public ResponseEntity<ResponseObject> isReview(Integer serviceHairId, Integer customerId) {
        Integer appointmentId = reviewRepository.findToReview(serviceHairId, customerId);
        if (appointmentId != null && appointmentId > 0) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("OK", "Successfully", true));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("OK", "Successfully", false));
    }


    public ResponseEntity<ResponseObject> findAllReviewByServiceId(Integer serviceHairId) {
        List<ReviewServiceHair> reviewServiceHairList = reviewRepository.findAllServiceByServiceId(serviceHairId);

        if (!reviewServiceHairList.isEmpty()) {
            List<ReviewServiceModel> reviewServiceModelList = reviewServiceHairList.stream()
                    .map(this::convertToModel) // Chuyển đổi từ ReviewServiceHair sang ReviewServiceModel
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", reviewServiceModelList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ReviewServiceModel convertToModel(ReviewServiceHair reviewServiceHair) {
        ReviewServiceModel reviewServiceModel = new ReviewServiceModel();
        reviewServiceModel.setReviewServiceId(reviewServiceHair.getId());
        reviewServiceModel.setCustomerName(reviewServiceHair.getCustomer().getFullName());
        reviewServiceModel.setComment(reviewServiceHair.getComment());
        reviewServiceModel.setRating(reviewServiceHair.getRatingValue());
        reviewServiceModel.setReviewDate(reviewServiceHair.getCreatedAt());
        return reviewServiceModel;
    }

}
