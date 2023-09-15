package yujong.ecommerce_yujong.board.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;
import yujong.ecommerce_yujong.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

@Data //그러나, 사실 엔티티에 '@Setter'를 넣어주는 것은 지양해야 함
@NoArgsConstructor
@Table(name="board")
@Entity
public class Board extends Auditable {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long boardId;

    @Column(length=100, nullable=false)
    private String title;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;

    @Column(nullable=false)
    private int reviewNum;

    @Column(nullable=false)
    private double reviewAvg; //double은 정수형 int보다 훨~~씬 넓은 범위의 15~16자리까지의 실수를 표현할 수 있음


//=============================================================================================================


    //< Board(1) : Product(1). 1:1 양방향 매핑. 주인객체: Board 객체 >
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName = "prodcutId") //이렇게 @JoinColumn이 있는 필드의 상위 엔티티 클래스가 주인객체! 여기서는 Board객체가 주인!
    private Product product;



    //< Board(1) : Product(1). 1:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setProduct(Product product){


        // Step 1)
        //- 'this.product != null'
        //  : 만약, 현재 특정된 게시글 Board의 상품 Product 정보가 비어져 있지 않은 상태(=깨끗하지 않은)이라면,
        //   이것은 즉 곧 이 현재 게시글 Board가 과거에 이미 해당 상품 Product를 포함한 게시글로 작성된 적이 있었다든지
        //   등 어떤 경로를 통해서였든지,
        //   이 현재 게시글 Board가 이 쇼핑몰에서 기존에 자신의 상품 Product 정보를 가지고 있는 상태라는 것을 의미하므로,
        //   (= 현재 특정된 게시글 Board가 이미 어떤 다른 상품 Product 객체와 연결되어 있는 상태인 경우 라는 의미임)
        //- 'this.product.setBoard(null)'
        //  : 현재 특정된 게시글 Board의 기존 상품 Product 정보를 지워버린다.
        //    즉, 현재 특정된 게시글 Board가 가지고 있었던 기존 상품 Product 정보를
        //    현재 특정된 게시글 Board에서 떼어내어 제거시키는 것임.
        //    즉, 현재 게시글 Board와 기존 상품 Product 객체 간의 연관관계 매핑을 끊어버리고 해제시키는 것임.
        //   (= 현재 특정된 게시글 Board와 매핑되어 있는 상품 Product 객체의 게시글 Board 정보를 null로 해버리는 것임)
        if(this.product != null){
            this.product.setBoard(null);
        }


        // Step 2)
        //- 현재 특정된 게시글 Board의 비어 있는 상품 Product 정보 칸에, 이제 내가 올리고자 하는
        //  상품 Product 정보를 자신인 현재 특정된 게시글 Board의 정보로 주입시켜서,
        //  이제 이 게시글 Board가 내가 올리고자 하는 상품 Product 정보를 갖도록 한다.
        this.product = product;


        // Step 3)
        //- 'product != null'
        //  : 혹시라도 만약에, 이 게시글 Board에 내가 등록하고자 하는 상품 Product 정보
        //    (=외부에서 새롭게 들어온 상품 Product 정보)가 비어 있는 상태가 아니면서(='&&'),
        //    (그냥 의례적으로 NullPointerException을 방지하기 위해 입력한 부분.
        //     당연히, 외부에서 새롭게 들어온 상품 Product 정보가 비어 있지 않은 상태여야
        //     (물론, 당연히 비어있지 않기 때문에 이 비어 있지 않은 정보를 떼어내면서 현재 내가 등록하고자 하는
        //     Product 정보로 대체해야 하는 것임),
        //     바로 다음에 나오는 product.getBoard() != this 이 부분에 대한 검사 검증이 진행될 수 있음. 당연한 소리임.)
        //- 'product.getBoard() != this'
        //  : 자신이 입력한 상품 Product 정보에 이미 다른 게시글 Board 정보가 들어있던(붙어있던) 상황이라면
        //    (=다른 게시글 Board 정보와 매핑되어 있는 상황이라면),
        //    (=내가 작성해서 외부에서 새롭게 들어온 상품 Product 정보가 이미 다른 게시글 Board를 참조하고 있는 상황이라면)
        //- 'product.setBaord(this)'
        //  : 그 과거의 다른 게시글 Board 정보를 내가 등록하고자 하는 상품 Product 정보에서 없애고 외부에서 새롭게 들어온
        //    고객으로서의 자신 상품 Product 정보로 대체해야 하기 때문에, 그 방법으로
        //    자신이 입력한 상품 Product 정보가 기존의 과거의 다른 게시글 Board 정보와 매핑되어 있는 연관관계를 해제시키고,
        //    이제 외부에서 들어온 자신이 입력한 상품 Product 정보에 현재 특정된 이 게시글 Board 정보를 집어넣어서,
        //    이제 비로소 외부에서 들어온 자신이 입력한 상품 Product 정보와
        //    현재 자기 자신인 게시글 Board 를 매핑시키는 것임.
        if(product != null && product.getBoard() != this){
            product.setBoard(this);
        }
    }


//=============================================================================================================



    //< Board(N) : Seller(1). N:1 양방향 매핑. 주인객체: Board >

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seller_id", referencedColumnName = "sellerId")
    private Seller seller;



//=============================================================================================================



    //< Review(N) : Board(1). N:1 양방향 매핑. 주인객체: Review >

    @OneToMany(mappedBy="board", cascade=CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();


    //*****중요*****
    //리뷰의 특성 상, 따로 그 Review와 Board 간 연관관계 메소드를 작성해줄 정도로
    //리뷰의 속성에 넣어줄 그런 중요한 건 없기 때문에,
    //Reivew와 Board 간 연관관계 메소드를 Board 객체의 정보 내부에 넣거나 그러지 않음.


//=============================================================================================================



    //< Comment(N) : Board(1). N:1 양방향 매핑. 주인객체: Comment >
    @OneToMany(mappedBy="board")
    private List<Comment> commentList = new ArrayList<>();



//=============================================================================================================





//=============================================================================================================





}
