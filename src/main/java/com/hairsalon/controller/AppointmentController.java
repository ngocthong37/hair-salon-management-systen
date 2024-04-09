package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/")
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @GetMapping("management/appointments/findAll")
    ResponseEntity<ResponseObject> findAll() {
        return appointmentService.getAll();
    }

    @PostMapping("appointments/makeApm")
    ResponseEntity<Object> makeAppointment(@RequestBody String json) {
            return appointmentService.makeAppointment(json);
    }

    @GetMapping("management/appointments/{statusId}")
    ResponseEntity<ResponseObject> getAllByStatusId(@PathVariable Integer statusId) {
        return appointmentService.getAllByStatusId(statusId);
    }


}
