package yujong.ecommerce_yujong.board.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.comment.entity.Comment;
import yujong.ecommerce_yujong.global.audit.Auditable;
import yujong.ecommerce_yujong.member.entity.Seller;
import yujong.ecommerce_yujong.product.entity.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name="board")
@Entity
public class Board extends Auditable {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long boardId;

    @Column(length=100, nullable=false)
    private String title;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;



    /* Board(1) : Product(1). 1:1 양방향 매핑. 주인객체: Board 객체. 게시판 - 상품 일대일 연관 관계 : 상품 참조 */
    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;


    /* Board(1) : Product(1). 1:1 양방향 매핑. 주인객체: Board 객체. 게시판 - 상품 연관관계 편의 메소드 */
    public void setProduct(Product product){

        if(this.product != null){
            this.product.setBoard(null);
        }

        this.product = product;

        if(product != null && product.getBoard() != this){
            product.setBoard(this);
        }
    }




    /*  Board(N) : Seller(1). N:1 양방향 매핑. 주인객체: Board. 게시판 - 판매자 다대일 연관 관계 : 판매자 참조 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seller_id")
    private Seller seller;





    /* Comment(N) : Board(1). N:1 양방향 매핑. 주인객체: Comment. 판매 댓글 - 판매자 다대일 연관 관계 : 판매자 참조 */
    @OneToMany(mappedBy="board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();



    }





