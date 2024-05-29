package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RestController
@RequestMapping(path = "/api/v1/")
public class CustomerController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;


    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private NewService newService;


    @GetMapping("news/getAll")
    public ResponseEntity<ResponseObject> getAllNews() {
        return newService.getAll();
    }

    @PostMapping("customer/order")
    public ResponseEntity<ResponseObject> order(@RequestBody String json) {
        return orderService.order(json);
    }

    @PutMapping("appointments/update-status")
    ResponseEntity<Object> updateStatusAppointment(@RequestBody String json) {
        return appointmentService.updateStatusAppointment(json);
    }

    @GetMapping("orders/getAllOrdersByCustomerId/{customerId}")
    public ResponseEntity<ResponseObject> findAllOrderByCustomerId(@PathVariable Integer customerId) {
        return orderService.findAllByCustomerId(customerId);
    }

    @PostMapping("customer/addToCart")
    public ResponseEntity<ResponseObject> addToCart(@RequestBody String json) {
        return cartItemService.addToCart(json);
    }

    @GetMapping("customer/findAllCartItems/{cartId}")
    public ResponseEntity<ResponseObject> findAllCartItem(@PathVariable Integer cartId ) {
        return cartItemService.findAllByCartId(cartId);
    }

    @DeleteMapping("customer/deleteCartItem/{id}")
    public ResponseEntity<ResponseObject> deleteCartItemById(@PathVariable Integer id) {
        return cartItemService.deleteCartItem(id);
    }
    @GetMapping("customer/getCartByCustomerId/{customerId}")
    public Integer getCartByCustomerId(@PathVariable Integer customerId) {
        return cartItemService.getCustomerCarts(customerId);
    }

    @DeleteMapping("customer/deleteAllCartItemByCartId/{cartId}")
    public ResponseEntity<ResponseObject> deleteAllCartItemByCartId(@PathVariable Integer cartId) {
        return cartItemService.deleteAllCartItemByCartId(cartId);
    }


    @PutMapping("customer/updateQuantityCartItem")
    public ResponseEntity<ResponseObject> updateQuantityCartItem(@RequestBody String json) {
        return cartItemService.updateQuantityItem(json);
    }

    @GetMapping("customer/findAllAppointmentByCustomerId/{id}")
    ResponseEntity<ResponseObject> getAllAppointmentByCustomerId(@PathVariable Integer id) {
        return appointmentService.getAllByCustomerId(id);
    }

    @PutMapping("customer/updateOrderStatus")
    ResponseEntity<Object> updateStatusCodeOrder(@RequestBody String json) {
        return orderService.updateStatusOrder(json);
    }

    @GetMapping("customer/findById/{customerId}")
    public ResponseEntity<ResponseObject> getCustomerById(@PathVariable Integer customerId) {
        return userService.findCustomerById(customerId);
    }



}
