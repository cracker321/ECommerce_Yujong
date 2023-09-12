package yujong.ecommerce_yujong.product.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name="Product")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable=false)
    private int price;

    @PositiveOrZero
    @Max(50)
    @Column(nullable=false)
    private int stock;

    @PositiveOrZero
    @Max(50)
    @Column(nullable=false)
    private int leftStock;

    @Column(nullable=false)
    private int category;

    @Column
    private String mainImage; //확인하기!


}
