package com.hairsalon.controller;

import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RestController
@RequestMapping(path = "/api/v1/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("products/findAll")
    public ResponseEntity<ResponseObject> findAll() {
        return productService.findAll();
    }


    @GetMapping("products/{id}")
    public ResponseEntity<ResponseObject> findAllByCateId(@PathVariable Integer id) {
        return productService.findAllByCateId(id);
    }
    @PostMapping("products/add")
    public ResponseEntity<Object> addProduct(@RequestBody String json) {
        return productService.add(json);
    }

    @PutMapping("products/update")
    public ResponseEntity<Object> updateProduct(@RequestBody String json) {
        return productService.update(json);
    }

}
