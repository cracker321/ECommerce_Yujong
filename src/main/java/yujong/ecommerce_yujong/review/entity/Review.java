package yujong.ecommerce_yujong.review.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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



}
