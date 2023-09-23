package yujong.ecommerce_yujong.comment.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class CommentPatchDto {


    private Long memberId;

    private Long commentId;

    private Long boardId;

    @Length(max = 65535, message = "최대 글자 수를 초과하였습니다.")
    private String context;

}