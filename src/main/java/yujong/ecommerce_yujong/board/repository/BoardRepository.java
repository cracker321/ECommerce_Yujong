package yujong.ecommerce_yujong.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yujong.ecommerce_yujong.board.dto.BoardForSellerMyPageDto;
import yujong.ecommerce_yujong.board.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT new yujong.ecommerce_yujong.board.dto" +
            ".BoardForSellerMyPageDto( b.boardId, b.title, b.product.stock, b.product.leftStock, b.createdAt) "
            + "FROM Board as b  WHERE b.seller.sellerId = :sellerId ORDER BY b.boardId DESC")
    List<BoardForSellerMyPageDto> sellerBoard(@Param("sellerId") Long sellerId);

}
