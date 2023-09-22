package yujong.ecommerce_yujong.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


@Data
@AllArgsConstructor
public class MultiResponseDto<T> {
    private List<T> data; //응답 데이터 목로을 담는 필드
    private PageInfo pageInfo; //페이지 정보를 담는 필드


    //기본값으로는 페이지 번호가 0부터 시작하기 때문에, 그것을 1부터 시작하는 것으로 변경
    public MultiResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber()+1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}