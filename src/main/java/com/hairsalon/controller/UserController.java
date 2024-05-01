package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
//import com.hairsalon.service.CustomerService;
import com.hairsalon.service.OrderService;
import com.hairsalon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("ordered/confirm")
    ResponseEntity<Object> updateStatusCodeOrder(@RequestBody String json) {
        return orderService.updateStatusOrder(json);
    }

    @PutMapping("updateUserProfile")
    ResponseEntity<Object> updateUserProfile(@RequestBody String json) {
        return userService.update(json);
    }


}
