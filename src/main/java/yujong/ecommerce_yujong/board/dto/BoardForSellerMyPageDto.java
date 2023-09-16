package yujong.ecommerce_yujong.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BoardForSellerMyPageDto {

    private Long boardId;

    private String title;

    private int leftStock;

    private LocalDate createdAt;
}
