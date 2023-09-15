package yujong.ecommerce_yujong.member.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

@Data
@NoArgsConstructor
@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column
    private String introduction;

    @Column
    private String imageUrl;

    //< Seller(1) : Member(1). 1:1 양방향 매핑. 주인객체: Seller >
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    //< Seller(1) : Member(1). 1:1 양방향 매핑. 연관관계 편의 메소드 >
    public void setMember(Member member){

        this.member = member;

        if(member.getSeller() != this){
            member.setSeller(this);
        }
    }


    

}
