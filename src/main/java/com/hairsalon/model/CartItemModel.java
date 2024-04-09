package com.hairsalon.model;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
public class CartItemModel {
    private Integer id;
    private String productItemName;
    private Integer productItemId;
    private Integer quantity;
    private String imageUrl;
    private Double price;
}
