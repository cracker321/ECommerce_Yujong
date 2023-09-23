package yujong.ecommerce_yujong.global.response;

import lombok.Data;
import yujong.ecommerce_yujong.member.dto.MemberDto;

import java.util.List;


@Data
public class MultiSellerResponseDto<T> {
    private List<T> dataList;
    private MemberDto.SellerResponseDto data;

    public MultiSellerResponseDto(MemberDto.SellerResponseDto data ,List<T> dataList ){
        this.dataList = dataList;
        this.data = data;
    }
}
