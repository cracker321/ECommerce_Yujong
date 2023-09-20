package yujong.ecommerce_yujong.member.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.ord.entity.Ord;
import yujong.ecommerce_yujong.review.entity.Review;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long customerId;



//=============================================================================================================


    //< Customer(1) : Member(1). 1:1 양방향 매핑. 주인객체: Customer 객체 >
    //- 현재 Customer 클래스는 이 클래스 내부에 바로 아래처럼 member_id 라는 Member 객체의 PK를 가지고 있기 때문에,
    //  Customer 객체가 주인 객체가 되는 것이다!
    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    //< Customer(1) : Member(1). 1:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setMember(Member member){


        // Step 1)
        //- 'this.member != null'
        //  : 만약, 현재 특정된 고객 Customer의 회원 Member 정보가 비어져 있지 않은 상태(=깨끗하지 않은)이라면,
        //   이것은 즉슨 곧 이 현재 고객 Customer가 과거에 이미 회원가입을 했었다든지 어떤 경로 통해서였든지
        //   이 현재 고객 Customer가 이 쇼핑몰에서 기존에 자신의 회원 Member 정보를 가지고 있는 상태라는 것을 의미하므로,
        //   (= 현재 특정된 고객 Customer가 이미 어떤 다른 회원 Member 객체와 연결되어 있는 상태인 경우 라는 의미임)
        //- 'this.member.setCustomer(null)'
        //  : 현재 특정된 고객 Customer의 기존 회원 Member 정보 를 지워버린다.
        //    즉, 현재 특정된 고객 Customer가 가지고 있었던 기존 회원 Member 정보를
        //    현재 특정된 고객 Customer에서 떼어내어 제거시키는 것임.
        //    즉, 현재 Member와 기존 Customer 객체 간의 연관관계 매핑을 끊어버리고 해제시키는 것임.
        //   (= 현재 특정된 고객 Customer와 매핑되어 있는 회원 Member 객체의 고객 Customer 정보를 null로 해버리는 것임)
        if(this.member != null){
            this.member.setCustomer(null);
        }

        // Step 2)
        //- 현재 특정된 고객 Customer의 비어 있는 회원 Member 정보 칸에, 이제 자신이 입력한
        //  고객인 자신의 회원 Member 정보를 자신인 현재 특정된 고객 Customer의 정보로 주입시켜서,
        //  이제 이 고객 Customer가 고객으로서의 자신만의 회원 Member 정보를 갖도록 한다.
        this.member = member;


        // Step 3)
        //- 'member != null'
        //  : 혹시라도 만약에, 이 고객 Customer가 입력한 고객으로서의 자신의 회원 Member 정보
        //    (=외부에서 새롭게 들어온 회원 Member 정보)가 비어 있는 상태가 아니면서(='&&'),
        //    (그냥 의례적으로 NullPointerException을 방지하기 위해 입력한 부분.
        //     당연히, 외부에서 새롭게 들어온 회원 Member 정보가 비어 있지 않은 상태여야
        //     (물론, 당연히 비어있지 않기 때문에 이 비어 있지 않은 정보를 떼어내면서 현재 자신의 고객으로서의 정보로 대체해야
        //     하는 것임),
        //     바로 다음에 나오는 member.getCustomer() != this 이 부분에 대한 검사 검증이 진행될 수 있음. 당연한 소리임.)
        //- 'member.getCustomer() != this'
        //  : 자신이 입력한 회원 Member 정보에 이미 다른 고객 Customer 정보가 들어있던(붙어있던) 상황이라면
        //    (=다른 고객 Customer 정보와 매핑되어 있는 상황이라면),
        //    (=내가 작성해서 외부에서 새롭게 들어온 회원 Member 정보가 이미 다른 고객 Customer를 참조하고 있는 상황이라면)
        //- 'member.setCustomer(this)'
        //  : 그 과거의 다른 고객 Customer 정보를 자신이 입력한 회원 Member 정보에서 없애고 외부에서 새롭게 들어온
        //    고객으로서의 자신 회원 Member 정보로 대체해야 하기 때문에, 그 방법으로
        //    자신이 입력한 회원 Member 정보가 기존의 다른 고객 Customer 정보와 매핑되어 있는 연관관계를 해제시키고,
        //    이제 외부에서 들어온 자신이 입력한 회원 Member 정보에 현재 특정된이 고객 Customer 정보를 집어넣어서,
        //    이제 비로소 외부에서 들어온 자신이 입력한 회원 Member 정보와
        //    현재 자기 자신인 고객 Customer 를 매핑시키는 것임.
        if(member != null && member.getCustomer() != this){
            member.setCustomer(this);
        }
    }


//=============================================================================================================



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


//=============================================================================================================



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

//=============================================================================================================



    @Builder
    public Customer(Long customerId){
        this.customerId = customerId;
    }



}
