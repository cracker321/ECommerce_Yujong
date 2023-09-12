package yujong.ecommerce_yujong.board.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Auditable;

@Data
@NoArgsConstructor
@Table(name="board")
public class Board extends Auditable {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long boardId;

    @Column(length=100, nullable=false)

}
