package yujong.ecommerce_yujong.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yujong.ecommerce_yujong.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
