package yujong.ecommerce_yujong.member.mapper;

import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberDto.Post memberToMemberDto(Member member);
    Member memberDtoToEntity(MemberDto.Post dto);
}
