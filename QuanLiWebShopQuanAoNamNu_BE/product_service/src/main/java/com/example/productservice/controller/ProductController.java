package com.example.productservice.controller;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(params = {"category", "type"})
    public List<ProductDTO> getProductsByCategoryAndType(
            @RequestParam String category,
            @RequestParam String type) {
        return productService.getProductsByCategoryAndType(category, type);
    }
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String name) {

        return productService.searchProducts(name);
    }

    @GetMapping("/filter")
    public List<ProductDTO> filterProducts(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type) {
        return productService.filterProducts(minPrice, maxPrice, category, type);
    }

}