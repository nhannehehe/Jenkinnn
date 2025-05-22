package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Category {
        male, female
    }

    public enum Type {
        clothes, pants, accessories
    }
}