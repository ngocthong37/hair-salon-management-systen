package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
//import com.hairsalon.service.CustomerService;
import com.hairsalon.entity.ServiceHair;
import com.hairsalon.service.AppointmentService;
import com.hairsalon.service.OrderService;
import com.hairsalon.service.ServiceHairService;
import com.hairsalon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/v1/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ServiceHairService serviceHair;


    @PutMapping("appointment/update-status")
    ResponseEntity<Object> updateStatusAppointment(@RequestBody String json) {
        return appointmentService.updateStatusAppointment(json);
    }


    @PutMapping("users/updateUserProfile")
    ResponseEntity<Object> updateUserProfile(@RequestBody String json) {
        return userService.update(json);
    }

    @PutMapping("employee/serviceHair/updateStatus")
    public ResponseEntity<Object> updateStatusServiceHair(@RequestBody String json) {
        return serviceHair.updateStatus(json);
    }

    @GetMapping ("employee/findAllAppointment/{employeeId}")
    public ResponseEntity<ResponseObject> findAllAppointmentForEmployee(@PathVariable Integer employeeId) {
        return appointmentService.getAllAppointmentForEmployee(employeeId);
    }

    @GetMapping("employee/appointments/findAll")
    ResponseEntity<ResponseObject> findAllAppointment() {
        return appointmentService.getAll();
    }


}
