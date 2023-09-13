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
    public void setProduct(Board board){

        //Step1)
        //외부 클래스 어딘가에서 이 메소드 setProduct를 새로운 인자값(=새로운 게시글 'Board board')으로 호출할 때,
        //그 때 주어지는 새로운 게시글('Board board')을 '현재 상품의 게시글('this.board')'로 새롭게 설정함.
        //즉, '현재 Product 엔티티 객체의 필드 board'를, 여기서 외부 클래스 어딘가에서 이 메소드 setProduct를 
        //새로운 인자값(=새로운 게시글 'Board board')'으로 호출할 때 주어지는 새로운 게시글('Board board') Board 객체로 설정하는 것임.
        //즉, 이 때의 Product 객체가 어떤 Board에 속하는지 지정하는 것임.
        this.board = board;

        //Step2)
        //- 외부 클래스 어딘가에서 이 메소드 setProduct를 새로운 인자값(=새로운 게시글 'Board board')으로 호출할 때,
        //  step1 에서 그 외부에서 들어온 새로운 Board 객체를 현재 Product 엔티티 객체의 필드 board의 값으로 받아들였고,
        //  이제 그 새롭게 받아들인 게시글 board가, 기존에 이미 어떤 다른 상품 Product 객체와 연결되어 있는 상태인지를 확인하는 것임.
        //- board.getProduct() != this
        //  : 만약, 새롭게 받아들인 게시글 board가 현재 Product 객체와 연결되어 있는 것이 아닌 기존에 어떤 다른 상품 Product 객체와
        //    연결되어 있는 상태라면,
        //- board.setProduct(this)
        //  : 자기 자신 board 객체로부터 자기 자신 board 객체와 기존에 연결되어 있는 어떤 다른 상품 Product 객체를 제거하고,
        //    자기 자신 board에 새롭게 현재 상품 Product 객체를 연결하는 것임
        if(board.getProduct() != this){
            board.setProduct(this);
        }



    }





















}
