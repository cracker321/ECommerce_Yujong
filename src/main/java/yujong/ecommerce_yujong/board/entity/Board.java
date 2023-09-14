package yujong.ecommerce_yujong.board.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yujong.ecommerce_yujong.product.entity.Product;

@Data //그러나, 사실 엔티티에 '@Setter'를 넣어주는 것은 지양해야 함
@NoArgsConstructor
@Table(name="board")
@Entity
public class Board extends Auditable {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long boardId;

    @Column(length=100, nullable=false)
    private String title;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;

    @Column(nullable=false)
    private int reviewNum;

    @Column(nullable=false)
    private double reviewAvg; //double은 정수형 int보다 훨~~씬 넓은 범위의 15~16자리까지의 실수를 표현할 수 있음

    //< Board(1) : Product(1). 1:1 양방향 매핑. 주인객체 - Board 객체 >
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id") //이렇게 @JoinColumn이 있는 필드의 상위 엔티티 클래스가 주인객체! 여기서는 Board객체가 주인!
    private Product product;






















}
