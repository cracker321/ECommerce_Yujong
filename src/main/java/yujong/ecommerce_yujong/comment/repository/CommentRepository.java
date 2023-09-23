package yujong.ecommerce_yujong.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    /* 쿼리메소드 */
    public Page<Comment> findByBoard_BoardId(Long boardId, Pageable pageable);

}