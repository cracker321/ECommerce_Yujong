package yujong.ecommerce_yujong.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;

    private Long boardId;

    private Long memberId;

    private String name;

    private String context;

    private LocalDate createdAt;

    private LocalDate modifiedAt;

}
