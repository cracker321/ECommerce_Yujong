package yujong.ecommerce_yujong.product.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.board.entity.Board;

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

    //확인하기!
    @Column
    private String mainImage;

    //< Board(1) - Product(1) 양방향 매핑. 주인 객체: Board >
    @OneToOne(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Board board;

    //< Board - Product 연관관계 편의 메소드 >



}
