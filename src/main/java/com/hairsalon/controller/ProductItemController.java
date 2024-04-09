package com.hairsalon.controller;


import com.hairsalon.entity.ResponseObject;
import com.hairsalon.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/")
public class ProductItemController {

    @Autowired
    private ProductItemService productItemService;

    @GetMapping("productItem/findAll")
    public ResponseEntity<ResponseObject> findAll() {
        return productItemService.findAll();
    }

    @PostMapping("productItem/add")
    public ResponseEntity<Object> addProductItem(@RequestBody String json) {
        return productItemService.add(json);
    }

    @PutMapping("productItem/update")
    public ResponseEntity<Object> updateProductItem(@RequestBody String json) {
        return productItemService.update(json);
    }

    @GetMapping("productItem/search/{productItemName}")
    public ResponseEntity<ResponseObject> findAllByProductItemName(@PathVariable String productItemName) {
        return productItemService.findByProductItemName(productItemName);
    }

}
