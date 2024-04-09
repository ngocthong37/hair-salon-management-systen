package com.hairsalon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hairsalon.entity.*;
import com.hairsalon.model.CartItemModel;
import com.hairsalon.respository.CartItemRepository;
import com.hairsalon.respository.CartRepository;
import com.hairsalon.respository.ProductItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<ResponseObject> addToCart(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (json == null || json.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("ERROR", "Empty JSON", ""));
            }
            JsonNode jsonObjectCartItem = objectMapper.readTree(json);
            Integer cartId = jsonObjectCartItem.get("cart_id") != null ?
                    jsonObjectCartItem.get("cart_id").asInt() : 1;
            Integer productItemId = jsonObjectCartItem.get("product_item_id") != null ?
                    jsonObjectCartItem.get("product_item_id").asInt() : 1;
            Integer quantity = jsonObjectCartItem.get("quantity") != null ?
                    jsonObjectCartItem.get("quantity").asInt() : 1;

            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            Optional<ProductItem> productItemOptional = productItemRepository.findById(productItemId);

            if (cartOptional.isPresent() && productItemOptional.isPresent()) {
                Cart cart = cartOptional.get();
                ProductItem productItem = productItemOptional.get();

                Optional<CartItem> existingCartItemOptional = Optional.ofNullable(cartItemRepository.findExistCartItem(cart.getId(), productItem.getId()));

                if (existingCartItemOptional.isPresent()) {
                    // If the item already exists in the cart, update the quantity
                    CartItem existingCartItem = existingCartItemOptional.get();
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                    cartItemRepository.save(existingCartItem);
                } else {
                    // If the item doesn't exist in the cart, create a new cart item
                    CartItem cartItem = new CartItem();
                    cartItem.setCart(cart);
                    cartItem.setProductItem(productItem);
                    cartItem.setQuantity(quantity);
                    cartItemRepository.save(cartItem);
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("OK", "Successfully", ""));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("ERROR", "Cart or product item not found", ""));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "An error occurred", e.getMessage()));
        }
    }

    public ResponseEntity<ResponseObject> findAllByCartId(Integer cartId) {
        List<CartItemModel> cartItemModelList = null;
        List<CartItem> cartItemList = cartItemRepository.findAllCartItemByCartId(cartId);
        cartItemModelList = cartItemList.stream().map(cartItem -> {
            CartItemModel cartItemModel = new CartItemModel();
            cartItemModel.setId(cartItem.getId());
            cartItemModel.setQuantity(cartItem.getQuantity());
            if (cartItem.getProductItem() != null) {
                cartItemModel.setProductItemName(cartItem.getProductItem().getProductItemName());
                cartItemModel.setImageUrl(cartItem.getProductItem().getImageUrl());
                cartItemModel.setPrice(cartItem.getProductItem().getPrice());
                cartItemModel.setProductItemId(cartItem.getProductItem().getId());
            }
            return cartItemModel;
        }).toList();

        if (!cartItemModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", cartItemModelList));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", ""));
    }

    public ResponseEntity<ResponseObject> deleteCartItem(Integer cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
             cartItemRepository.delete(cartItem.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Cannot found this item", ""));
    }

    public ResponseEntity<ResponseObject> deleteAllCartItemByCartId(Integer cartItemId) {
        try {
            cartItemRepository.deleteAllCartItemByCartId(cartItemId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", ""));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", e.getMessage(), ""));
        }
    }


    public ResponseEntity<ResponseObject> updateQuantityItem(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (json == null || json.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("ERROR", "Empty JSON", ""));
            }
            JsonNode jsonObjectUpdateQuantity = objectMapper.readTree(json);
            Integer cartItemId = jsonObjectUpdateQuantity.get("cartItemId") != null ?
                    Integer.parseInt(jsonObjectUpdateQuantity.get("cartItemId").asText()) : 1;
            Integer number = jsonObjectUpdateQuantity.get("number") != null ?
                    jsonObjectUpdateQuantity.get("number").asInt() : 1;
            Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(number);
                cartItemRepository.save(cartItem.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully", ""));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Cannot update quantity", ""));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("ERROR", "An error occurred", e.getMessage()));
        }
    }
}
