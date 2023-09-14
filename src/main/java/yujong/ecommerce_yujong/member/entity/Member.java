package yujong.ecommerce_yujong.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String email;

    @Column(length=50, nullable=false)
    private String password;

    @Column(length=100, nullable=false)
    private String address;

    //# 확인하기!
    //< 소셜 아이디 추가 >
    @Column
    private String socialId;

    //< 소설 로그인 추가하면서, 해당 판별을 위한 provider 타입 추가 >
    @Enumerated(EnumType.STRING);
    private ProviderType providerType;

    //# 확인하기!
    //< Authority로 일일히 확인하기 어려우니 컬럼 추가 >
    @Column
    private String role;

    //# 확인하기!!
    //< Security 사용하여 역할 추가 >
    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> roles;






    //< Member(1) : Comment(N). N:1 양방향 매핑. 주인객체: Comment 객체 >
    //교재 p208
    //- 이 Member 엔티티 클래스 내부에 @OneToMany를 작성하 것 자체가
    //  Member 엔티티는 Comment와 의 관계에서 주인이 아니라는 뜻이다!
    //- cascade=CascadeType.ALL 은, 주인 아닌 객체인 Member(1) 엔티티 클래스의 내부에 있는
    //  @OneToMany 옆에 입력하는 것이다!
    //  즉, N:1 양뱡향 매핑에서는 mappedBy 옆에 입력하면 된다!!
    //  즉, Member 엔티티 객체에서 발생하는 모든 변경이 바로 Comment 엔티티 객체로 전파된다는 것임!
    @OneToMany(mappedBy="member", cascade=CascadeType.ALL)
    private List<Comment> commentList;

    //*****중요*****
    //< Member(N)과 Comment(1) 연관관계 편의 메소드 >
    public void addComment(Comment comment){

        this.comment = comment;

        if(comment.getMember() != this){
            comment.setMember(this);
        }
    }






    //< Member(1) : Customer(1). 1:1 양뱡향 매핑. 주인객체: Customer 객체 >
    //- 왜냐하면, Customer 클래스 내부에는 아래처럼 이미 정의되어 있기 때문!
    //  @OneToOne(fetch=FetchType.LAZY)
    //  @JoinColumn(name="member_id")
    //  private Member member;
    @OneToOne(mappedBy="member", cascade=CascadeType.ALL)
    private Customer customer;


    //< Member(1) : Customer(1). 1:1 양방향 매핑. 연관과계 편의 메소드 >
    public void setCustomer(Customer customer){

        this.customer=customer;

        if(customer.getMember() != this){
            customer.setMember(this);
        }
    }





    //여기 cascadeType 확실히 하기!!
    @OneToOne(mappedBy="member", cascade=CascadeType.ALL)
    private Seller seller;




}
