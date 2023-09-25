package yujong.ecommerce_yujong.product.mapper;


import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.product.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product boardPostDtoToProduct(BoardPostDto boardPostDto);

}

