package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
//import com.hairsalon.service.CustomerService;
import com.hairsalon.service.OrderService;
import com.hairsalon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/v1/users/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;


    @GetMapping("ordered")
    public ResponseEntity<ResponseObject> findAllOrder() {
        return orderService.findAll();
    }

    @GetMapping("ordered/{id}")
    public ResponseEntity<ResponseObject> findAllOrder(@PathVariable Integer id) {
        return orderService.findAllByStatusId(id);
    }


    @PutMapping("updateUserProfile")
    ResponseEntity<Object> updateUserProfile(@RequestBody String json) {
        return userService.update(json);
    }


}
