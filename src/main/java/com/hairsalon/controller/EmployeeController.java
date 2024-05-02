package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.AppointmentService;
import com.hairsalon.service.CartItemService;
import com.hairsalon.service.OrderService;
import com.hairsalon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping(path = "/api/v1/")
public class EmployeeController {
    @Autowired
    UserService userService;
    @GetMapping("employee/findAll")
    public ResponseEntity<ResponseObject> findAllEmployee() {
        return userService.findAllEmployee();
    }


}
