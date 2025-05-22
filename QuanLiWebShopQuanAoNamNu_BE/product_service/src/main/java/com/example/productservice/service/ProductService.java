package com.example.productservice.service;


import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.responsitory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found")); // Nếu không tìm thấy sản phẩm thì ném lỗi
        return toDTO(product); // Sử dụng phương thức toDTO thay vì gọi productMapper
    }

    public List<ProductDTO> getProductsByCategoryAndType(String category, String type) {
        return productRepository.findByCategoryAndType(
                Product.Category.valueOf(category),
                Product.Type.valueOf(type)
        ).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> filterProducts(Double minPrice, Double maxPrice, String category, String type) {
        List<Product> products;

        if (category != null && type != null) {
            products = productRepository.findByPriceBetweenAndCategoryAndType(
                    minPrice, maxPrice,
                    Product.Category.valueOf(category),
                    Product.Type.valueOf(type)
            );
        } else if (category != null) {
            products = productRepository.findByPriceBetweenAndCategory(
                    minPrice, maxPrice,
                    Product.Category.valueOf(category)
            );
        } else if (type != null) {
            products = productRepository.findByPriceBetweenAndType(
                    minPrice, maxPrice,
                    Product.Type.valueOf(type)
            );
        } else {
            products = productRepository.findByPriceBetween(minPrice, maxPrice);
        }

        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }



    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        dto.setCategory(product.getCategory().name());
        dto.setType(product.getType().name());
        return dto;
    }
}