package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hairsalon.entity.*;
import com.hairsalon.respository.imp.ReviewRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    ReviewRepositoryImp reviewImp;

    public ResponseEntity<Object> add(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Review review = new Review();
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
            Customer customer = new Customer();
            customer.setId(customerId);
            ServiceHair serviceHair = new ServiceHair();
            serviceHair.setId(serviceId);
            review.setCustomer(customer);
            review.setServiceHair(serviceHair);
            review.setRatingValue(rating);
            review.setComment(comment);

            Integer messageId = reviewImp.add(review);
            if (messageId != 0) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("OK", "Successfully", ""));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Can not make an appointment", ""));
            }

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "An error occurred", e.getMessage()));
        }
    }

}
