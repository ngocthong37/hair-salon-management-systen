package com.hairsalon.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemModel {
    private Integer orderItemId;
    private Double price;
    private Integer quantity;
    private Integer productItemId;
    private String productItemUrl;
    private String productItemName;
}
