package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.ServiceHair;
import com.hairsalon.service.RevenueService;
import com.hairsalon.service.ServiceHairService;
import com.hairsalon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/management/")
public class ManagementController {
    @Autowired
    private ServiceHairService serviceHair;

    @Autowired
    private UserService userService;

    @Autowired
    RevenueService revenueService;
    @GetMapping(value = "/services/{id}")
    public ResponseEntity<ResponseObject> getServiceById(@PathVariable Integer id) {
        return serviceHair.findById(id);
    }

    @GetMapping("revenueFromService")
    public ResponseEntity<ResponseObject> getTotalRevenueFromService() {
        return revenueService.getRevenueFromService();
    }

    @GetMapping("revenueFromProduct")
    public ResponseEntity<ResponseObject> getTotalRevenueFromProduct() {
        return revenueService.getRevenueFromProduct();
    }

    @GetMapping("revenueService/{year}")
    public ResponseEntity<ResponseObject> getTotalRevenueFromServiceByYear(@PathVariable Integer year) {
        return revenueService.getRevenueFromServiceByYear(year);
    }

    @GetMapping("revenueService/{year}/{month}")
    public ResponseEntity<ResponseObject> getTotalRevenueFromServiceByYear(@PathVariable Integer year, @PathVariable Integer month) {
        return revenueService.getRevenueFromServiceByMonth(year, month);
    }

    @PostMapping("addServiceHair")
    public ResponseEntity<Object> addServiceHair(@RequestBody String json) {
        return serviceHair.add(json);
    }

    @PutMapping("updateServiceHair")
    public ResponseEntity<Object> updateServiceHair(@RequestBody String json) {
        return serviceHair.update(json);
    }


    @GetMapping("customer/findAll")
    public ResponseEntity<ResponseObject> getAllCustomer() {
        return userService.findAllCustomer();
    }

    @GetMapping("employee/findAll")
    public ResponseEntity<ResponseObject> getAllEmployee() {
        return userService.findAllEmployee();
    }


}
