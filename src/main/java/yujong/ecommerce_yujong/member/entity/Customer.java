package yujong.ecommerce_yujong.member.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.ord.entity.Ord;

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


    //Customer <--> Member 연관관계 편의 메소드
    //기본적으로 Setter와 형식 같음
    public void setMember(Member member){
        this.member = member;

        if(member.getCustomer() != this){
            member.setCustomer(this);
        }
    }




    //< Customer(1) : Order(N). N:1 양방향 매핑. 주인객체: Ord 객체 >
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Ord> ordList = new ArrayList<>();


    //< Customer(1) : Order(N). N:1 양방향 매핑. 연관관계 편의 메소드 >
    //- 이 Customer 클래스로 만든 어떤 Customer가 있는데, 그 Customer가 새로운 주문을 한 경우,
    //  그 Customer의 객체 정보에 그 Customer가 새로운 주문을 했다는 정보를 넣어야 하고,
    //  이 때 그 새로운 주문 정보를 아래 메소드 addOrd를 외부 다른 클래스에서 사용하여 그 Customer 객체 내부에 넣어주는 것임.
    public void addOrd(Ord ord){
        ordList.add(ord); //그 Customer가 기존에 주문한 과거 주문 내역 리스트들에, 이제 이번에 새롭게 발생시킨 주문도 그 주문이력에 넣음.

        if(ord.getCustomer() != this){
            ord.setCustomer(this);
        }

    }





    //< Customer(1) : Review(N). N:1 양방향 매핑. 주인객체: Review >



}
