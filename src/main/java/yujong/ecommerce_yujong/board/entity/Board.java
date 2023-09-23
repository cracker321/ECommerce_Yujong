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

@Data //그러나, 사실 엔티티에 '@Setter'를 넣어주는 것은 지양해야 함
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


//=============================================================================================================


    //< Board(1) : Product(1). 1:1 양방향 매핑. 주인객체: Board 객체 >
    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;



    //< Board(1) : Product(1). 1:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setProduct(Product product){

        if(this.product != null){
            this.product.setBoard(null);
        }

        this.product = product;

        if(product != null && product.getBoard() != this){
            product.setBoard(this);
        }
    }


//=============================================================================================================



    //< Board(N) : Seller(1). N:1 양방향 매핑. 주인객체: Board >

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seller_id")//'referencedColumnName' 관련해서 아래 두 링크 반드시 참조!
    private Seller seller;





//=============================================================================================================



    //< Comment(N) : Board(1). N:1 양방향 매핑. 주인객체: Comment >
    @OneToMany(mappedBy="board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();



//=============================================================================================================



    }





