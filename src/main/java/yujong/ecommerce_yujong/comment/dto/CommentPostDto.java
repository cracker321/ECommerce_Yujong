package yujong.ecommerce_yujong.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
public class CommentPostDto {


    @NotNull
    private Long memberId;

    @NotNull
    private Long boardId;

    @NotNull
    @Length(min = 10, max = 65535, message = "댓글은 최소 10자를 입력하여야 합니다.")
    private String context;

}