package yujong.ecommerce_yujong.ord.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.global.audit.Auditable;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.product.entity.Product;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Ord extends Auditable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordId;

    @Column(length=50, nullable=false)
    private String address;

    @Column(length=13, nullable=false)
    private String phone;

    @Column
    private int quantity;

    @Column
    private int totalPrice;

    @Column
    private String tid;

    //< Ord(N) : Customer(1). N:1 양방향 매핑. 주인객체: Ord >
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;


    //< Ord(1) : Product(1): N:1 양방향 매핑. 주인객체: Ord >
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Column
    @Enumerated(EnumType.STRING)
    private OrdStatus status = OrdStatus.ORD_REQUEST;



    //
    public enum OrdStatus{

        ORD_REQUEST(1, "주문 요청"),
        PAY_COMPLETTE(2, "결제 완료");



        private int number;
        private String description;

        OrdStatus(int number, String description){
            this.number = number;
            this.description = description;
        }

    }



}
