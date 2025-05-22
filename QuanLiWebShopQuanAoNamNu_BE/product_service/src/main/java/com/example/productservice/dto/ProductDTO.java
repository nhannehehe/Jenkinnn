package com.example.productservice.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Integer quantity;
    private Double price;
    private String image;
    private String category;
    private String type;
}

