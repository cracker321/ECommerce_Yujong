package yujong.ecommerce_yujong.member.mapper;

import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.member.dto.CustomerPatchDto;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {


    MemberDto.Post memberToMemberDto(Member member);
    Member memberDtoToEntity(MemberDto.Post dto);

    CustomerPatchDto memberToCustomerPatchDto(Member member);

//    default CustomerPatchrDto memberToCustomerPatchDto(Member member) {
//        return new CustomerPatchDto(member.getName(), member.getPhone()); // 예시: 이름과 전화번호를 Dto로 변환
//    }


}
