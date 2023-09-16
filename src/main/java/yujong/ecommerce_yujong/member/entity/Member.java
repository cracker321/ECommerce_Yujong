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
    private String name;

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




//=============================================================================================================



    //< Member(1) : Comment(N). N:1 양방향 매핑. 주인객체: Comment 객체 >
    //  교재 p208
    //- 이 Member 엔티티 클래스 내부에 @OneToMany를 작성하 것 자체가
    //  Member 엔티티는 Comment와 의 관계에서 주인이 아니라는 뜻이다!
    //- cascade=CascadeType.ALL 은, 주인 아닌 객체인 Member(1) 엔티티 클래스의 내부에 있는
    //  @OneToMany 옆에 입력하는 것이다!
    //  즉, N:1 양뱡향 매핑에서는 mappedBy 옆에 입력하면 된다!!
    //  즉, Member 엔티티 객체에서 발생하는 모든 변경이 바로 Comment 엔티티 객체로 전파된다는 것임!
    @OneToMany(mappedBy="member", cascade=CascadeType.ALL)
    private List<Comment> commentList;



    //*****중요*****
    //< Member(1) : Comment(N). N:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- 이 Member 클래스로 만든 어떤 회원 Member가 있는데, 그 회원 Member가 새로운 댓글 Comment를 남기는 경우,
    //  그 Member의 객체 정보에 그 Member가 새로운 댓글을 남겼다 라는 정보를 넣어야 하고,
    //  이 때 그 새롭게 남긴 댓글 정보를 아래 메소드 addComment를 외부 다른 클래스에서 사용하여
    //  그 Member 객체 내부에 넣어주는 것임.
    public void addComment(Comment comment){

        commentList.add(comment); //그 Member가 기존에 작성했던 과거 댓글 이력 리스트들에,
                                  // 이제 이번에 새롭게 발생시킨 댓글 작성도 그 과거 댓글 이력 commentList에 추가해서 넣어줌.

        if(comment.getMember() != this){
            comment.setMember(this);
        }



    }



//=============================================================================================================


    //< Member(1) : Customer(1). 1:1 양뱡향 매핑. 주인객체: Customer 객체 >
    //- 왜냐하면, Customer 클래스 내부에는 아래처럼 이미 정의되어 있기 때문!
    //  @OneToOne(fetch=FetchType.LAZY)
    //  @JoinColumn(name="member_id")
    //  private Member member;
    @OneToOne(mappedBy="member", cascade=CascadeType.ALL)
    private Customer customer;




    //< Member(1) : Customer(1). 1:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setCustomer(Customer customer){

        // Step 1)
        //- 'this.customer != null'
        //  : 만약, 현재 특정된 회원 Member의 고객 Customer 정보가 비어져 있지 않은 상태(=깨끗하지 않은)이라면,
        //   이것은 즉 곧 이 현재 회원 Member가 과거에 이미 회원가입을 했었다든지 어떤 경로 통해서였든지
        //   이 현재 회원 Member가 이 쇼핑몰에서 기존에 자신의 고객 Customer 정보를 가지고 있는 상태라는 것을 의미하므로,
        //   (= 현재 특정된 회원 Member가 이미 어떤 다른 고객 Customer 객체와 연결되어 있는 상태인 경우 라는 의미임)
        //- 'this.customer.setMember(null)'
        //  : 현재 특정된 회원 Member의 기존 고객 Customer 정보를 지워버린다.
        //    즉, 현재 특정된 회원 Member가 가지고 있었던 기존 고객 Customer 정보를
        //    현재 특정된 회원 Member에서 떼어내어 제거시키는 것임.
        //    즉, 현재 회원 Member와 기존 고객 Customer 객체 간의 연관관계 매핑을 끊어버리고 해제시키는 것임.
        //   (= 현재 특정된 회원 Member와 매핑되어 있는 고객 Customer 객체의 회원 Member 정보를 null로 해버리는 것임)
        if(this.customer != null){
            this.customer.setMember(null);
        }

        // Step 2)
        //- 현재 특정된 회원 Member의 비어 있는 고객 Customer 정보 칸에, 이제 자신이 입력한
        //  고객인 자신의 고객 Customer 정보를 자신인 현재 특정된 회원 Member의 정보로 주입시켜서,
        //  이제 이 회원 Member가 고객으로서의 자신만의 고객 Customer 정보를 갖도록 한다.
        this.customer = customer;


        // Step 3)
        //- 'customer != null'
        //  : 혹시라도 만약에, 이 회원 Member가 입력한 고객으로서의 자신의 고객 Customer 정보
        //    (=외부에서 새롭게 들어온 고객 Customer 정보)가 비어 있는 상태가 아니면서(='&&'),
        //    (그냥 의례적으로 NullPointerException을 방지하기 위해 입력한 부분.
        //     당연히, 외부에서 새롭게 들어온 고객 Customer 정보가 비어 있지 않은 상태여야
        //     (물론, 당연히 비어있지 않기 때문에 이 비어 있지 않은 정보를 떼어내면서 현재 자신의 고객으로서의 정보로 대체해야
        //     하는 것임),
        //     바로 다음에 나오는 customer.getMember() != this 이 부분에 대한 검사 검증이 진행될 수 있음. 당연한 소리임.)
        //- 'customer.getMember() != this'
        //  : 자신이 입력한 고객 Customer정보에 이미 다른 회원 Member 정보가 들어있던(붙어있던) 상황이라면
        //    (=다른 회원 Member 정보와 매핑되어 있는 상황이라면),
        //    (=내가 작성해서 외부에서 새롭게 들어온 고객 Customer 정보가 이미 다른 회원 Member를 참조하고 있는 상황이라면)
        //- 'customer.setMember(this)'
        //  : 그 과거의 다른 회원 Member 정보를 자신이 입력한 고객 Customer 정보에서 없애고 외부에서 새롭게 들어온
        //    고객으로서의 자신 고객 Customer 정보로 대체해야 하기 때문에, 그 방법으로
        //    자신이 입력한 고객 Customer 정보가 기존의 다른 회원 Member 정보와 매핑되어 있는 연관관계를 해제시키고,
        //    이제 외부에서 들어온 자신이 입력한 고객 Customer 정보에 현재 특정된 이 회원 Member 정보를 집어넣어서,
        //    이제 비로소 외부에서 들어온 자신이 입력한 고객 Customer 정보와
        //    현재 자기 자신인 회원 Member 를 매핑시키는 것임.
        if(customer != null && customer.getMember() != this){
            customer.setMember(this);
        }
    }




//=============================================================================================================




    //'먼저 발생하고, 먼저 일어나는 엔티티 객체의 클래스 내부에 cascadeType을 넣는 것이다!!'
    //즉, 여기서는 Member 객체의 변화가 먼저 발생하고, 먼저 일어나기 때문에, 
    //아래처럼 이 Member 엔티티 클래스 내부에 cascadeType을 작성했다!

    //< Member(1) : Customer(1). 1:1 양방향 매핑. 주인객체: Seller 객체 >
    @OneToOne(mappedBy="member", cascade=CascadeType.ALL)
    private Seller seller;



    //< Member(1) : Seller (1). 1:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setSeller(Seller seller){

        // Step 1)
        //- 'this.seller != null'
        //  : 만약, 현재 특정된 회원 Member의 판매자 Seller 정보가 비어져 있지 않은 상태(=깨끗하지 않은)이라면,
        //   이것은 즉 곧 이 현재 회원 Member가 과거에 이미 판매자회원 가입을 했었다든지 어떤 경로 통해서였든지
        //   이 현재 회원 Member가 이 쇼핑몰에서 기존에 자신의 Seller 정보를 가지고 있는 상태라는 것을 의미하므로,
        //   (= 현재 특정된 회원 Member가 이미 어떤 다른 Seller 객체와 연결되어 있는 상태인 경우 라는 의미임)
        //- 'this.seller.setMember(null)'
        //  : 현재 특정된 회원 Member의 기존 Seller 정보를 지워버린다.
        //    즉, 현재 특정된 회원 Member가 가지고 있었던 기존 Seller 정보를
        //    현재 특정된 회원 Member에서 떼어내어 제거시키는 것임.
        //    즉, 현재 Member와 기존 Seller 객체 간의 연관관계 매핑을 끊어버리고 해제시키는 것임.
        //   (= 현재 특정된 회원 Member와 매핑되어 있는 판매자 Seller 객체의 회원 Member 정보를 null로 해버리는 것임)
        if(this.seller != null){
            this.seller.setMember(null);
        }


        // Step 2)
        //- 현재 특정된 회원 Member의 비어 있는 판매자 Seller 정보 칸에, 이제 자신이 입력한
        //  판매자인 자신의 판매자 Seller 정보를 주입시켜서,
        //  이제 이 회원 Member가 판매자로서의 자신의 Seller 정보를 갖도록 한다.
        this.seller = seller;


        // Step 3)
        //- 'seller != null'
        //  : 혹시라도 만약에, 이 회원 Member가 입력한 판매자로서의 자신의 판매자 Seller 정보
        //    (=외부에서 새롭게 들어온 판매자 Seller 정보)가 비어 있는 상태가 아니면서(='&&'),
        //    (그냥 의례적으로 NullPointerException을 방지하기 위해 입력한 부분.
        //     당연히, 외부에서 새롭게 들어온 판매자 Seller 정보가 비어 있지 않은 상태여야
        //     (물론, 당연히 비어있지 않기 때문에 이 비어 있지 않은 정보를 떼어내면서 현재 자신의 판매자로서의 정보로 대체해야
        //     하는 것임),
        //     바로 다음에 나오는 seller.getMember() != this 이 부분에 대한 검사 검증이 진행될 수 있음. 당연한 소리임.)
        //- 'seller.getMember() != this'
        //  : 자신이 입력한 판매자 Seller 정보에 이미 다른 회원 Member 정보가 들어있던(붙어있던) 상황이라면
        //    (=다른 회원 Member 정보와 매핑되어 있는 상황이라면),
        //    (=내가 작성해서 외부에서 새롭게 들어온 판매자 Seller 정보가 이미 다른 회원 Member를 참조하고 있는 상황이라면)
        //- 'seller.setMember(this)'
        //  : 그 과거의 다른 회원 Member 정보를 자신이 입력한 판매자 Seller 정보에서 없애고 외부에서 새롭게 들어온
        //    고객으로서의 자신 판매자 Seller 정보로 대체해야 하기 때문에, 그 방법으로
        //    자신이 입력한 판매자 Seller 정보가 기존의 다른 회원 Member 정보와 매핑되어 있는 연관관계를 해제시키고,
        //    이제 외부에서 들어온 자신이 입력한 판매자 Seller 정보에 현재 특정된 이 회원 Member 정보를 집어넣어서,
        //    이제 비로소 외부에서 들어온 자신이 입력한 판매자 Seller 정보와
        //    현재 자기 자신인 회원 Member 를 매핑시키는 것임.
        if(seller != null && seller.getMember() != this){
            seller.setMember(this);
        }
    }


//=============================================================================================================


    //사용자 생성자(Builder와는 별개로 만든 것임)
    public Member(String name, String email, String password, ProviderType providerType, String role,
                  List<String> roles, String socialId){
        this.name = name;
        this.email = email;
        this.password = password;
        this.providerType = providerType;
        this.role = role;
        this.roles = roles;
        this. socialId = socialId;


    }

}
