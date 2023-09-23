package yujong.ecommerce_yujong.global.exception;

import lombok.Getter;

public enum ExceptionCode {

    /* 상품, 판매 게시판 관련 예외 */
    BOARD_NOT_FOUND(404, "판매 게시판을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(404, "상품을 찾을 수 없습니다."),
    PRODUCT_SOLDOUT(404, "상품이 매진되었습니다."),
    PRODUCT_NOT_ENOUGH(404, "재고가 충분하지 않습니다."),
    COMMENT_NOT_FOUND(404, "문의를 찾을 수 없습니다."),

    /* 주문 Ord 관련 예외 */
    ORDER_NOT_FOUND(404, "주문을 찾을 수 없습니다."),


    /* 회원 Member 관련 예외 */
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),
    ROLE_ERROR(400, "잘못된 역할입니다."),
    PROVIDER_ERROR(400, "로컬 사용자인지 혹은 정확한 소셜 사용자인지 확인하세요."),
    MEMBER_EXISTS(409, "이미 존재하는 이메일입니다."),
    UNAUTHORIZED_MEMBER(403, "권한이 없는 사용자 입니다.");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
