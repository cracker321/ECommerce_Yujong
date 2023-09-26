package yujong.ecommerce_yujong.member.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.comment.entity.Comment;
import yujong.ecommerce_yujong.member.role.Role;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;

    @Column(length=45, nullable=false)
    private String name;

    @Column(length=45, nullable=false)
    private String email;

    @Column(length=50, nullable=false)
    private String password;

    @Column(length=100, nullable=false)
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;




    /* Member(1) : Comment(N). N:1 양방향 매핑. 주인객체: Comment 객체. 회원 - 문의 다대일 연관 관계 : 회원 참조 */
    @OneToMany(mappedBy="member", cascade=CascadeType.ALL)
    private List<Comment> commentList;




    /* Member(1) : Comment(N). N:1 양방향 매핑. 주인객체: Comment 객체. 회원 - 문의 연관 관계 편의 메서드 */
    public void addComment(Comment comment){

        commentList.add(comment);

        if(comment.getMember() != this){
            comment.setMember(this);
        }

    }



    /* Member(1) : Customer(1). 1:1 양뱡향 매핑. 주인객체: Customer 객체. 회원 - 고객 일대일 연관 관계 : 회원 참조 */
    @OneToOne(mappedBy="member", cascade=CascadeType.ALL)
    private Customer customer;




    /*< Member(1) : Customer(1). 1:1 양방향 매핑. 주인객체: Customer 객체. 회원 - 고객 연관관계 편의 메소드 */
    public void setCustomer(Customer customer){

        // Step 1)
        if(this.customer != null){
            this.customer.setMember(null);
        }

        // Step 2)
        this.customer = customer;


        // Step 3)
        if(customer != null && customer.getMember() != this){
            customer.setMember(this);
        }
    }




    /* Member(1) : Seller(1). 1:1 양방향 매핑. 주인객체: Seller 객체. 회원 - 판매자 일대일 연관 관계 : 회원 참조 */
    @OneToOne(mappedBy="member", cascade=CascadeType.ALL)
    private Seller seller;



    /*  Member(1) : Seller (1). 1:1 양방향 매핑. 주인객체: Seller 객체. 회원 - 판매자 연관관계 편의 메소드 */
    public void setSeller(Seller seller){

        // Step 1)
        if(this.seller != null){
            this.seller.setMember(null);
        }


        // Step 2)
        this.seller = seller;


        // Step 3)
        if(seller != null && seller.getMember() != this){
            seller.setMember(this);
        }
    }




    //* 사용자 생성자(Builder와는 별개) */
    public Member(String name, String email, String password, Role role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
