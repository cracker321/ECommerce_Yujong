package yujong.ecommerce_yujong.member.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.product.entity.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column
    private String introduction;




    /* Seller(1) : Member(1). 1:1 양방향 매핑. 주인객체: Seller. 판매자 - 회원 일대일 연관 관계 : 회원 참조 */
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    /* Seller(1) : Member(1). 1:1 양방향 매핑. 판매자 - 회원  연관관계 편의 메소드 */
    public void setMember(Member member){

        // Step 1)
        if(this.member != null){
            this.member.setSeller(null);
        }

        // Step 2)
        this.member = member;


        // Step 3)
        if(member != null && member.getSeller() != this){
                 member.setSeller(this);
        }
    }



    /* Board(N) : Seller(1). N:1 양방향 매핑. 주인객체: Board. 게시판 - 판매자 다대일 연관 관계 : 판매자 참조 */
    @OneToMany(mappedBy="seller", cascade=CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();



    /* Board(N) : Seller(1). N:1 양방향 매핑. 주인객체: Board. 게시판 - 판매자 연관관계 편의 메서드*/
    public void addBoards(Board board){


        if(board.getSeller() != this){

            board.setSeller(this);
        }

    }



    /* Product(N) : Seller(1). N:1 양방향 매핑. 주인객체: Product. 판매자 - 상품 다대일 연관 관계 : 판매자 참조 */
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();



    /* Product(N) : Seller(1). N:1 양방향 매핑. 주인객체: Product. 판매자 - 상품 연관관계 편의 메서드*/
    public void addProduct(Product product){
        productList.add(product);

        if(product.getSeller() !=this){
            product.setSeller(this);
        }
    }



}
