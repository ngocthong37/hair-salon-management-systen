package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hairsalon.entity.Product;
import com.hairsalon.entity.ProductItem;
import com.hairsalon.entity.ResponseObject;
import com.hairsalon.entity.User;
import com.hairsalon.model.ProductItemModel;
import com.hairsalon.respository.ProductItemRepository;
import com.hairsalon.respository.UserRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductItemService {
    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<ResponseObject> findAll() {
        List<ProductItem> productItemList = productItemRepository.findAllProductItem();
        List<ProductItemModel> productItemModelList = productItemList.stream().map(
                productItem -> {
                    ProductItemModel productItemModel = new ProductItemModel();
                    productItemModel.setId(productItem.getId());
                    productItemModel.setProductItemName(productItem.getProductItemName());
                    productItemModel.setPrice(productItem.getPrice());
                    productItemModel.setImageUrl(productItem.getImageUrl());
                    productItemModel.setStatus(productItem.getStatus());
                    productItemModel.setQuantityInStock(productItem.getQuantityInStock());
                    productItemModel.setWarrantyTime(productItem.getWarrantyTime());
                    productItemModel.setDescription(productItem.getDescription());
                    return productItemModel;
                }).collect(Collectors.toList());
        if (!productItemModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", productItemModelList));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
        }
    }

    public ResponseEntity<Object> add(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            Double price = jsonNode.get("price") != null ? jsonNode.get("price").asDouble() : null;
            String productItemName = jsonNode.get("productItemName") != null ? jsonNode.get("productItemName").asText() : "";
            String imageUrl = jsonNode.get("imageUrl") != null ? jsonNode.get("imageUrl").asText() : "";
            Integer quantity = jsonNode.get("quantity") != null ? jsonNode.get("quantity").asInt() : -1;
            Integer productId = jsonNode.get("productId") != null ? jsonNode.get("productId").asInt() : -1;
            Integer warrantyTime = jsonNode.get("warrantyTime") != null ? jsonNode.get("warrantyTime").asInt() : -1;
            String description = jsonNode.get("description") != null ? jsonNode.get("description").asText() : "";

            ProductItem productItem = new ProductItem();
            productItem.setProductItemName(productItemName);
            Product product = new Product();
            product.setId(productId);
            productItem.setProduct(product);
            productItem.setPrice(price);
            productItem.setStatus("OK");
            productItem.setQuantityInStock(quantity);
            productItem.setWarrantyTime(warrantyTime);
            productItem.setImageUrl(imageUrl);
            productItem.setDescription(description);
            ProductItem saveProduct = productItemRepository.save(productItem);
            if (saveProduct.getId() != null) {
                List<User> customerList = userRepository.findAllCustomer();
                String[] cc = new String[customerList.size()];
                for (int i = 0; i < customerList.size(); i++) {
                    User customer = customerList.get(i);
                    cc[i] = customer.getEmail();
                }
                Map<String, Object> model = new HashMap<>();
                model.put("nameProduct", saveProduct.getProductItemName());
                model.put("price", saveProduct.getPrice() + " VND");
                model.put("number", saveProduct.getQuantityInStock());
                model.put("description", saveProduct.getDescription());
                emailSendService.sendMail("thongnguyenngoc3738@gmail.com", cc, "Thông báo sản phẩm mới", model);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", saveProduct.getId()));
            }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Have error when add product item", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }

    }

    public ResponseEntity<Object> update(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            String productItemName = jsonNode.get("productItemName") != null ? jsonNode.get("productItemName").asText() : "";
            String imageUrl = jsonNode.get("imageUrl") != null ? jsonNode.get("imageUrl").asText() : "";
            Double price = jsonNode.get("price") != null ? jsonNode.get("price").asDouble() : null;
            Integer quantity = jsonNode.get("quantity") != null ? jsonNode.get("quantity").asInt() : -1;
            Integer productItemId = jsonNode.get("productItemId") != null ? jsonNode.get("productItemId").asInt() : -1;
            Integer warrantyTime = jsonNode.get("warrantyTime") != null ? jsonNode.get("warrantyTime").asInt() : -1;
            String description = jsonNode.get("description") != null ? jsonNode.get("description").asText() : "";
            Optional<ProductItem> productItemOptional = productItemRepository.findById(productItemId);
            if (productItemOptional.isPresent()) {
                ProductItem productItem = productItemOptional.get();
                productItem.setProductItemName(productItemName);
                productItem.setPrice(price);
                productItem.setWarrantyTime(warrantyTime);
                productItem.setQuantityInStock(quantity);
                productItem.setImageUrl(imageUrl);
                productItem.setDescription(description);
                productItemRepository.save(productItem);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", productItem.getId()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Have error when update product item", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }

    }

    public ResponseEntity<Object> updateStatus(String json) {
        JsonNode jsonNode;
        JsonMapper jsonMapper = new JsonMapper();
        try {
            jsonNode = jsonMapper.readTree(json);
            Integer productItemId = jsonNode.get("productItemId") != null ? jsonNode.get("productItemId").asInt() : -1;
            Optional<ProductItem> productItemOptional = productItemRepository.findById(productItemId);
            if (productItemOptional.isPresent()) {
                ProductItem productItem = productItemOptional.get();
                productItem.setStatus("NOT_OK");
                productItemRepository.save(productItem);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", productItem.getId()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ERROR", "Have error when update product item status", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
        }

    }


    public String uploadImage(MultipartFile file, String namePath, Integer serviceHairId) {
        String imageUrl = storageService.uploadImages(file, namePath);
        productItemRepository.updateImage(imageUrl, serviceHairId);
        return imageUrl;
    }

    public ResponseEntity<ResponseObject> findByProductItemName(String productItemName) {
        try {
            List<ProductItem> productItemList = productItemRepository.findByProductName(productItemName);
            List<ProductItemModel> productItemModelList = productItemList.stream().map(
                    productItem -> {
                        ProductItemModel productItemModel = new ProductItemModel();
                        productItemModel.setId(productItem.getId());
                        productItemModel.setProductItemName(productItem.getProductItemName());
                        productItemModel.setPrice(productItem.getPrice());
                        productItemModel.setImageUrl(productItem.getImageUrl());
                        productItemModel.setStatus(productItem.getStatus());
                        productItemModel.setQuantityInStock(productItem.getQuantityInStock());
                        productItemModel.setWarrantyTime(productItem.getWarrantyTime());
                        productItemModel.setDescription(productItem.getDescription());
                        return productItemModel;
                    }).collect(Collectors.toList());
            if (!productItemModelList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", productItemModelList));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "Have error:", e.getMessage()));
        }
    }


}
