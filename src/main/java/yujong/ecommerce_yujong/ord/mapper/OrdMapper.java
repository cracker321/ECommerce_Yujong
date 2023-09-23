package yujong.ecommerce_yujong.ord.mapper;

import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.ord.dto.OrdCustomerResponseDto;
import yujong.ecommerce_yujong.ord.dto.OrdPostDto;
import yujong.ecommerce_yujong.ord.dto.OrdResponseDto;
import yujong.ecommerce_yujong.ord.entity.Ord;
import yujong.ecommerce_yujong.product.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdMapper {

    default Ord ordPostDtoToOrd(Customer client, Product product, OrdPostDto ordPostDto){
        if( ordPostDto == null){
            return null;
        }

        Ord ord = new Ord();
        ord.setCustomer(client);
        ord.setProduct(product);
        ord.setAddress(ordPostDto.getAddress());
        ord.setPhone(ordPostDto.getPhone());
        ord.setTotalPrice(ordPostDto.getTotalPrice());
        ord.setQuantity(ordPostDto.getQuantity());

        return ord;
    }

    default OrdResponseDto ordToOrdResponseDto(Ord ord){
        if( ord == null){
            return null;
        }

        OrdResponseDto ordResponseDto = new OrdResponseDto();
        ordResponseDto.setOrdId(ord.getOrdId());
        ordResponseDto.setCustomerId(ord.getCustomer().getCustomerId());
        ordResponseDto.setBoardId(ord.getProduct().getBoard().getBoardId());
        ordResponseDto.setName(ord.getCustomer().getMember().getName());
        ordResponseDto.setAddress(ord.getAddress());
        ordResponseDto.setPhone(ord.getPhone());
        ordResponseDto.setTotalPrice(ord.getTotalPrice());
        ordResponseDto.setQuantity(ord.getQuantity());
        ordResponseDto.setOrdStatus(ord.getStatus());
        ordResponseDto.setCreatedAt(ord.getCreatedAt());

        return ordResponseDto;

    }
    default OrdCustomerResponseDto ordToOrdCustomerResponseDto(Ord ord){
        if( ord == null){
            return null;
        }

        OrdCustomerResponseDto ordResponseDto = new OrdCustomerResponseDto();
        ordResponseDto.setOrdId(ord.getOrdId());
        ordResponseDto.setBoardId(ord.getProduct().getBoard().getBoardId());
        ordResponseDto.setTitle(ord.getProduct().getBoard().getTitle());
        ordResponseDto.setName(ord.getCustomer().getMember().getName());
        ordResponseDto.setAddress(ord.getAddress());
        ordResponseDto.setPhone(ord.getPhone());
        ordResponseDto.setQuantity(ord.getQuantity());

        return ordResponseDto;

    }
    List<OrdCustomerResponseDto> ordToOrdCustomerResponseDtos(List<Ord> ords);
}