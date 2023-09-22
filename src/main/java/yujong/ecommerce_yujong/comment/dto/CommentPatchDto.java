package yujong.ecommerce_yujong.comment.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
//@NoArgsConstructor  <<--- 수정 Patch 하는 것이기 때문에, 어차피 비어있는 Comment 객체는 안 올 것이고, 따라서 @NoArgs..도 불필요!
public class CommentPatchDto {


    private Long memberId;

    private Long commentId;

    private Long boardId;

    @Length(max = 65535, message = "최대 글자 수를 초과하였습니다.")
    private String context;

}