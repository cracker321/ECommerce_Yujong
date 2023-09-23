package yujong.ecommerce_yujong.comment.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.global.audit.Auditable;
import yujong.ecommerce_yujong.member.entity.Member;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable=false)
    private String context;

    /* Comment(N) : Member(1). N:1 양방향 매핑. 주인객체: Comment. 댓글 - 회원 다대일 연관 관계 : 소비자 참조 */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id") //이게 Member 테이블의 PK키. https://boomrabbit.tistory.com/217
    @ToString.Exclude
    private Member member;




    /* Comment(N) : Board(1). N:1 양방향 매핑. 주인객체: Comment. 댓글 - 게시판 다대일 연관 관계 : 판매자 참조 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board_id") //이게 Board 테이블의 PK키. https://boomrabbit.tistory.com/217
    @ToString.Exclude
    private Board board;


}
