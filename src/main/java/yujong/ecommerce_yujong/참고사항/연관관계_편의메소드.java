package yujong.ecommerce_yujong.참고사항;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import yujong.ecommerce_yujong.member.entity.Customer;

public class 연관관계_편의메소드 {



    //=============================================================================================================


    //< Member(1) : Customer(1). 1:1 양뱡향 매핑. 주인객체: Customer 객체 >
    //- 왜냐하면, Customer 클래스 내부에는 아래처럼 이미 정의되어 있기 때문!
    //  @OneToOne(fetch=FetchType.LAZY)
    //  @JoinColumn(name="member_id")
    //  private Member member;
    @OneToOne(mappedBy="member", cascade= CascadeType.ALL)
    private Customer customer;




    //< Member(1) : Customer(1). 1:1 양방향 매핑. 연관관계 편의 메소드 > 교재 p190~
    //- set이 붙는 연관관계 편의 메소드는 기본적으로 Setter 세터와 형식이 같음!!
    //*****중요*****
    //연관관계 편의 메소드에서 setXXX은 원래 기존 세터 setXXX와 같은 형식이고,
    //e.g)
    //< 기존 세터 setCustomer >
    //public void setCustomer(Customer customer){
    //
    //      this.customer = customer      // Step 2) 이와 같이 기존의 setter는 연관관계 메소드의 step 2) 만 있음.
    //}

    //< 연관관계 메소드 세터 setCustomer >
    //public void setCustoomer(Customer cusotmer){
    //
    //      if(this.customer != null){    // Step 1)
    //          this.customer.setMember(null);
    //          }
    //
    //      this.customer = customer      // Step 2)
    //
    //      if(customer != null && customer.member != this){    // Step3
    //          custmer.setMember(this)
    //      }

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


    
}
