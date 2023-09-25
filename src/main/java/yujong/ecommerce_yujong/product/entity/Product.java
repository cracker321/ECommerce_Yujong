package yujong.ecommerce_yujong.product.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.ord.entity.Ord;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.PRD_SELLING;

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





    //< Board(1) - Product(1) 일대일 양방향 매핑. 주인객체: Board >
    @OneToOne(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Board board;



    //< Board - Product 연관관계 편의 메소드 > //교재 p190~
    public void setProduct(Board board){



        // Step 1)
        if(this.board != null){
            this.board.setProduct(null);
        }

        //Step2)
        this.board = board;

        //Step3)
        if(board != null && board.getProduct() != this){
            board.setProduct(this);
        }


    }



    @ManyToOne
    @JoinColumn(name="seller_id")
    private Seller seller;




    /* Order(N) : Product(1). N:1 양방향 연관관계. 주인객체: Ord 객체. 주문 - 상품 일대일 연관 관계 : 상품 참조 */
    @OneToMany(mappedBy="product", cascade=CascadeType.ALL)
    private List<Ord> ordList = new ArrayList<>();


    public enum ProductStatus{
        PRD_SELLING("1", "판매중"),
        PRD_SOLDOUT("2", "매진");

        private String value;
        private String code;


        //*****중요*****
        ProductStatus(String value,String code){
            this.value = value;
            this.code = code;
        }

        public String getValue(){
            return value;
        }

        public String getCode(){
            return code;
        }



    }


}
