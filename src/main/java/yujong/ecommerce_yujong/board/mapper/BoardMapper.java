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
    //- '@Mapping(target="status", expression="java(product.getStatus())")'
    //  : '상품 Product 객체의 필드(컬럼) Status의 값(행)'을 BoardResponseDto 객체의 '필드 status'에 복붙, 입력, 저장시킴.
    //    그 아래 두 개 @Mapping도 동일한 원리로 작동함.
    //  ****중요****
    //- 이렇게 인자로 상품 Product, 게시글 Board와 같이 2개 이상이 들어온 경우,
    //  만약, @Mapping(..) 어노테이션을 사용하지 않을 경우,
    //  상품 Product 엔티티와 게시글 Board 엔티티의 내부 필드들 중에서 BoardResponseDto 클래스의 내부 필드와
    //  동일한 이름을 가진 필드가 있다면, 그 필드(컬럼)의 값(행)들은 자동으로 BoardRespoonseDto의 동일 필드(컬럼)의 값(행)으로
    //  복사 붙여넣기 된다!
    //- 만약, 상품 Product, 게시글 Board 의 내부 필드들과 동일한 이름을 가진 필드가 BoardResponseDto 클래스의 내부 필드들 중에
    //  정말 아무것도 일치하는 필드가 없다면,
    //  기본적으로 아무 작업도 수행되지 않는다!
    //  그리고, BoardResponseDto 객체의 모든 필드는 null 또는 해당 타입의 기본값으로 자동 설정된다!
    //  (숫자형 - 0, 참조형 - null)
    //- 비유&실생활
    //  : 상점(Board)과 제품(Product)에서 모든 필요한 정보(판매자 ID, 제품 상태 등등)를 모아서
    //    새로운 팜플렛(BoardResponseDto)를 만드는 것과 같음.




    @Mapping(target="sellerId", expression="java(board.getSeller().getSellerId())")
    @Mapping(target = "name", expression = "java(board.getSeller().getMember().getName())")
    BoardTotalResponseDto productToBoardTotalResponseDto(Product product, Board board);
}
