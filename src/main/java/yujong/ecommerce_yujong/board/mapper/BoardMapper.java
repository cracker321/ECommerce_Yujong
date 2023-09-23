package yujong.ecommerce_yujong.board.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.board.dto.BoardResponseDto;
import yujong.ecommerce_yujong.board.dto.BoardTotalResponseDto;
import yujong.ecommerce_yujong.board.entity.Board;
import yujong.ecommerce_yujong.product.entity.Product;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    Board boardPostDtoToBoard(BoardPostDto boardPostDto);

    BoardResponseDto boardToBoardResponseDto(Board board);


    @Mapping(target="status", expression="java(product.getStatus())")
    @Mapping(target="sellerId", expression="java(board.getSeller().getSellerId())")
    @Mapping(target="name", expression="java(board.getSeller().getMember().getName())")
    BoardResponseDto productToBoardResponseDto(Product product, Board board);



    @Mapping(target="sellerId", expression="java(board.getSeller().getSellerId())")
    @Mapping(target = "name", expression = "java(board.getSeller().getMember().getName())")
    BoardTotalResponseDto productToBoardTotalResponseDto(Product product, Board board);
}
