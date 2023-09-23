package yujong.ecommerce_yujong.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PageInfo {

    /* 현재 페이지 번호 */

    /* 페이지당 댓글 수 */

    /* 전체 데이터 수 */
    private long totalElements;

    /* 전체 페이지 수 */
    private int totalPages;

}
