package yujong.ecommerce_yujong.member.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.ord.entity.Ord;

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




    /* Customer(1) : Member(1). 1:1 양방향 매핑. 주인객체: Customer 객체. 고객 - 회원 일대일 연관 관계 : 회원 참조 */
    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    /* Customer(1) : Member(1). 1:1 양방향 매핑. 주인객체: Customer 객체. 고객 - 회원 연관관계 편의 메서드 */
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    public void setMember(Member member){


        // Step 1)
        if(this.member != null){
            this.member.setCustomer(null);
        }

        // Step 2)
        this.member = member;


        // Step 3)
        if(member != null && member.getCustomer() != this){
            member.setCustomer(this);
        }
    }





    /* Order(N) : Customer(1). N:1 양방향 매핑. 주인객체: Ord 객체. 주문 - 고객 다대일 연관 관계 : 소비자 참조 */
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private List<Ord> ordList = new ArrayList<>();




    /* Order(N) : Customer(1). N:1 양방향 매핑. 주문 - 고객 연관관계 편의 메서드 */
    public void addOrd(Ord ord){

        ordList.add(ord);

        if(ord.getCustomer() != this){
            ord.setCustomer(this);
        }

    }



    @Builder
    public Customer(Long customerId){

        this.customerId = customerId;
    }

}
