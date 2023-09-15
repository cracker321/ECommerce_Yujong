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
    @OneToOne(mappedBy="customer", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Ord> ordList = new ArrayList<>();




}
