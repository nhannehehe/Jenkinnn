package iuh.fit.cart_service.DTO;


import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private Integer quantity;
    private String name;
    private Double price;
    private String image;
    private String category;
    private String type;
}