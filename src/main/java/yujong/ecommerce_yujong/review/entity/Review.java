package yujong.ecommerce_yujong.review.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.member.entity.Customer;

@Data
@NoArgsConstructor
@Entity
public class Review extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable=false)
    private String context;

//    @Column
//    private String image;

    @Column(nullable=false)
    private int star;



    //< Review(N) : Customer(1). N:1 양방향 매핑. 주인객체 Review >
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    //*****중요*****
    //리뷰의 특성 상, 따로 그 Review와 Customer 간 연관관계 메소드를 작성해줄 정도로
    //리뷰의 속성에 넣어줄 그런 중요한 건 없기 때문에,
    //Reivew와 Customer 간 연관관계 메소드를 Review 객체의 정보 내부에 넣거나 그러지 않음.




    //< Review(N) : Board(1). N:1 양방향 매핑. 주인객체: Review >
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board_id")
    @ToString.Exclude
    private Board board;




}
