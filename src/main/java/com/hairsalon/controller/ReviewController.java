package com.hairsalon.controller;


import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/customer/")
public class ReviewController {

    @Autowired
    ReviewService reviewService;


    @PostMapping("/addReviewService")
    public ResponseEntity<Object> addComment(@RequestBody String json) {
        return reviewService.add(json);
    }

    @GetMapping("/review/findToReview/{serviceHairId}/{customerId}")
    public ResponseEntity<ResponseObject> findToReview(@PathVariable Integer serviceHairId, @PathVariable Integer customerId) {
        return reviewService.isReview(serviceHairId, customerId);
    }

    @GetMapping("/review/findAllReviewByServiceId/{serviceId}")
    public ResponseEntity<ResponseObject> findAllReviewByServiceId(@PathVariable Integer serviceId) {
        return reviewService.findAllReviewByServiceId(serviceId);
    }

}
