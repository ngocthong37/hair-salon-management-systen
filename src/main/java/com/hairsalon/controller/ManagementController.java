package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/management/")
public class ManagementController {
    @Autowired
    private ServiceHairService serviceHair;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ProductItemService productItemService;

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

    @GetMapping("revenueFromServiceByMonth/{year}/{month}")
    public ResponseEntity<ResponseObject> getTotalRevenueFromServiceByYear(@PathVariable Integer year, @PathVariable Integer month) {
        return revenueService.getRevenueFromServiceByMonth(year, month);
    }

    @PostMapping("serviceHair/add")
    public ResponseEntity<Object> addServiceHair(@RequestBody String json) {
        return serviceHair.add(json);
    }

    @PutMapping("serviceHair/update")
    public ResponseEntity<Object> updateServiceHair(@RequestBody String json) {
        return serviceHair.update(json);
    }

    @PutMapping("serviceHair/updateStatus")
    public ResponseEntity<Object> updateStatusServiceHair(@RequestBody String json) {
        return serviceHair.updateStatus(json);
    }

    @PostMapping("hairService/uploadImageServiceHair")
    public String uploadImageServiceHair(@RequestParam("namePath") String namePath, @RequestParam("file") MultipartFile file,
                                              @RequestParam("serviceHairId") Integer serviceHairId) {
        return serviceHair.uploadImage(file, namePath, serviceHairId);
    }

    @PostMapping("productItem/uploadImageProductItem")
    public String uploadImageProductItem(@RequestParam("namePath") String namePath, @RequestParam("file") MultipartFile file,
                              @RequestParam("productItemId") Integer productItemId) {
        return productItemService.uploadImage(file, namePath, productItemId);
    }


    @GetMapping("customer/findAll")
    public ResponseEntity<ResponseObject> getAllCustomer() {
        return userService.findAllCustomer();
    }
    @GetMapping("employee/findAll")
    public ResponseEntity<ResponseObject> findAllEmployee() {
        return userService.findAllEmployee();
    }

    @GetMapping("customer/findById/{customerId}")
    public ResponseEntity<ResponseObject> getCustomerById(@PathVariable Integer customerId) {
        return userService.findCustomerById(customerId);
    }

    @GetMapping("employee/findById/{employeeId}")
    public ResponseEntity<ResponseObject> getEmployeeById(@PathVariable Integer employeeId) {
        return userService.findEmployeeById(employeeId);
    }

    @PostMapping ("employee/updateStatusUser")
    public ResponseEntity<Object> updateStatusUser(@RequestBody String json) {
        return userService.updateStatusUser(json);
    }

    @GetMapping("appointments/findAll")
    ResponseEntity<ResponseObject> findAll() {
        return appointmentService.getAll();
    }

    @GetMapping("appointments/{statusId}")
    ResponseEntity<ResponseObject> getAllByStatusId(@PathVariable Integer statusId) {
        return appointmentService.getAllByStatusId(statusId);
    }

    @PutMapping("appointments/update-status")
    ResponseEntity<Object> updateStatusAppointment(@RequestBody String json) {
        return appointmentService.updateStatusAppointment(json);
    }


}
