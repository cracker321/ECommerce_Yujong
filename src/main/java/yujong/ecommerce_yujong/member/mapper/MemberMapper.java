package yujong.ecommerce_yujong.member.mapper;

import org.mapstruct.Mapper;
import yujong.ecommerce_yujong.member.dto.CustomerPatchDto;
import yujong.ecommerce_yujong.member.dto.MemberDto;
import yujong.ecommerce_yujong.member.dto.SellerPatchDto;
import yujong.ecommerce_yujong.member.entity.Customer;
import yujong.ecommerce_yujong.member.entity.Member;
import yujong.ecommerce_yujong.member.entity.Seller;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    /* 등록 */
    Member memberDtoToMember(MemberDto.Post post);

    /* 응답 */
    default MemberDto.SellerResponseDto memberToSellerDto(Member member) {
        if (member == null) {
            return null;
        }

        MemberDto.SellerResponseDto response =
                MemberDto.SellerResponseDto.builder()
                        .memberId(member.getMemberId())
                        .sellerId(member.getSeller().getSellerId())
                        .email(member.getEmail())
                        .name(member.getName())
                        .address(member.getAddress())
                        .role(member.getRole())
                        .introduce(member.getSeller().getIntroduction())
                        .build();

        return response;
    }

    default MemberDto.CustomerResponseDto memberToCustomerDto(Member member) {
        if (member == null) {
            return null;
        }
        MemberDto.CustomerResponseDto response =
                MemberDto.CustomerResponseDto.builder()
                        .memberId(member.getMemberId())
                        .customerId(member.getCustomer().getCustomerId())
                        .email(member.getEmail())
                        .name(member.getName())
                        .address(member.getAddress())
                        .role(member.getRole())
                        .build();

        return response;
    }

    /* 수정 */
    Member sellerPatchDtoToMember(SellerPatchDto sellerPatchDto);

    Member customerPatchDtoToMember(CustomerPatchDto customerPatchDto);

    Seller sellerPatchDtoToSeller(SellerPatchDto sellerPatchDto);

    Customer customerPatchDtoToCustomer(CustomerPatchDto customerPatchDto);

}
