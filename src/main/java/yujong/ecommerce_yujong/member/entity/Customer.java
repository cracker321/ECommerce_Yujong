package yujong.ecommerce_yujong.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.ord.entity.Ord;
import yujong.ecommerce_yujong.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long customerId;


    //< Customer(1) : Member(1). 1:1 양방향 매핑. 주인객체: Customer 객체 >
    //- 현재 Customer 클래스는 이 클래스 내부에 바로 아래처럼 member_id 라는 Member 객체의 PK를 가지고 있기 때문에,
    //  Customer 객체가 주인 객체가 되는 것이다!
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    //< Customer(1) : Member(1). 1:1 양방향 매핑. 연관관계 편의 메소드 >
    //기본적으로 Setter와 형식 같음
    public void setMember(Member member){


        // Step 1)
        //- 만약, 현재 특정된 고객 Customer의 회원 Member 정보가 비어져 있지 않은 상태(=깨끗하지 않은)이고,
        //  이 현재 고객 Customer이 과거에 이미 회원가입을 했었다든지 어떤 경로 통해서였든지
        //  이 현재 고객 Customer이 이 쇼핑몰에서 기존에 자신의 Member 정보를 가지고 있는 상태였다면,
        //  현재 특정된 고객 Customer의 기존 Member 정보를 지워버린다.
        //  즉, 현재 특정된 고객 Customer가 가지고 있었던 기존 Member 정보를
        //  현재 특정된 고객 Customer에서 떼어내어 제거시키는 것임.
        if(this.member != null){
            this.member.setCustomer(null);
        }

        
        this.member = member;

        if(member.getCustomer() != this){
            member.setCustomer(this);
        }
    }




    //< Customer(1) : Order(N). N:1 양방향 매핑. 주인객체: Ord 객체 >
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private List<Ord> ordList = new ArrayList<>();


    //< Customer(1) : Order(N). N:1 양방향 매핑. 연관관계 편의 메소드 >
    //- 이 Customer 클래스로 만든 어떤 Customer가 있는데, 그 Customer가 새로운 주문을 한 경우,
    //  그 Customer의 객체 정보에 그 Customer가 새로운 주문을 했다는 정보를 넣어야 하고,
    //  이 때 그 새로운 주문 정보를 아래 메소드 addOrd를 외부 다른 클래스에서 사용하여 그 Customer 객체 내부에 넣어주는 것임.
    public void addOrd(Ord ord){
        ordList.add(ord); //그 Customer가 기존에 주문한 과거 주문 내역 리스트들에,
                          // 이제 이번에 새롭게 발생시킨 주문도 그 과거 주문이력 ordList에 추가해서 넣어줌.

        if(ord.getCustomer() != this){
            ord.setCustomer(this);
        }

    }





    //< Customer(1) : Review(N). N:1 양방향 매핑. 주인객체: Review >
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();


    //< Customer(1) : Review(N). N:1 양방향 매핑. 연관관계 편의 메소드 >
    public void addReview(Review review){
        reviewList.add(review);

        if(review.getCustomer() != this){
            review.setCustomer(this);
        }
    }


    @Builder
    public Customer(Long customerId){
        this.customerId = customerId;
    }



}
