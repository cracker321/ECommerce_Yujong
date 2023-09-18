package yujong.ecommerce_yujong.참고사항;

import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.board.dto.BoardPostDto;
import yujong.ecommerce_yujong.product.entity.Product;

public class MapStruct {



    @Mapper(componentModel = "spring")
    public interface ProductMapper {

        Product boardPostDtoToProduct(BoardPostDto boardPostDto);
        //'BoardPostDto 객체'를 '상품 Product 엔티티 객체'로 변환시키는 역할
    }


    /*
    [ MapStruct ]

    - Service 클래스 내부에서, Dto를 Entity로 변환하거나, Entity를 Dto로 변환시킬 때 주로 사용됨.

    e.g)

    @Mapper(componentModel = "spring")
    public interface UserMapper {

          @Mapping(source="email", target="username")
          UserDto userToUserDto(User user);

          User userDtoToUser(UserDto userDto);

    }

    - '@Mapper(componentModel = "spring")
      : 이 부분은 MapStruct에게 이 인터페이스 ProductMapper가 '매퍼'라는 것을 알려주는 어노테이션.
        여기서 'spring'이라고 지정한 것은, 생성되는 구현체가 Spring 프레임워크의 컴퍼넌트로 등록되어야 함을 의미함.
    - '@Mapping(source="email", target="username")
      : '필드 매핑'.
        원본객체인 User 객체의 내부 필드 중 하나인 email 필드(컬럼)의 세부 데이터(행)들을,
        대상객체인 UserDto 객체의 내부 필드 중 하나인 usrname 필드(컬럼)의 세부 데이터(행)로 입력, 저장, 복붙하겠다 라는 의미임.
    - 'UserDto userToUserDto(User user)'
      : 외부로부터 User 객체를 인자로 전달받아서 이것을 UserDto 객체로 변환하는 역할
    - 'User userDtoToUser(UserDto userDto)'
      : 외부로부터 UserDto 객체를 인자로 전달받아서 이것을 User 객체로 변환하는 역할

     */




}
