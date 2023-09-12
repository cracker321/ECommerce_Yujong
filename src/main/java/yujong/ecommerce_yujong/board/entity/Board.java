package yujong.ecommerce_yujong.board.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Auditable;

@Data
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

    //< 'Board 엔티티'와 'Product 엔티티' 간 일대일 연관 관계. 상품 참조 >
    private Product product;






















}
