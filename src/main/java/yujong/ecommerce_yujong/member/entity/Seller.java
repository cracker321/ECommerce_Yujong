package yujong.ecommerce_yujong.member.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import yujong.ecommerce_yujong.board.entity.Board;

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

    @Column
    private String imageUrl;

    //< Seller(1) : Member(1). 1:1 양방향 매핑. 주인객체: Seller >
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    //< Seller(1) : Member(1). 1:1 양방향 매핑. 연관관계 편의 메소드 >
    public void setMember(Member member){

        // Step 1)
        if(this.member != null){
            this.member.setSeller(null);
        }

        // Step 2)
        this.member = member;


        // Step 3)
        if(member.getSeller() != this){
            member.setSeller(this);
        }
    }




    //< Seller(1) : Board(N). N:1 양방향 매핑. 주인객체: Board >
    @OneToMany(mappedBy="seller", cascade=CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();


    //< Seller(1) : Board(N). N:1 양방향 매핑. 연관관계 편의 메소드 >

    //*****중요*****
    //< Seller(1) : Board(N). N:1 양방향 매핑. 연관관계 편의 메소드 >
    //- 이 Seller 클래스로 만든 어떤 판매자 Seller가 있는데, 그 판매자 Seller가 새로운 게시글 Board를 남기는 경우,
    //  그 판매자 Seller의 객체 정보에 그 Seller가 새로운 게시글을 남겼다 라는 정보를 넣어야 하고,
    //  이 때 그 새롭게 남긴 게시글 정보를 아래 메소드 addBoard를 외부 다른 클래스에서 사용하여
    //  그 판매자 Seller의 객체 내부에 넣어주는 것임.
    public void addBoards(Board board){

        boardList.add(board); //그 Seller가 기존에 작성했던 과거  게시글 이력 리스트들에,
                              //이제 이번에 새롭게 발생시킨 게시글 작성도 그 과거 게시글 이력 boardList에 추가해서 넣어줌.

        if(board.getSeller() != this){ //만약, 이 게시글의 작성자가 Seller가 아니라, 다른 사람이
            board.setSeller(this);
        }
    }


    

}
