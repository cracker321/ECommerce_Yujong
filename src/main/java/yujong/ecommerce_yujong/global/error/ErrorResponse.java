package yujong.ecommerce_yujong.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import yujong.ecommerce_yujong.global.exception.ExceptionCode;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

@Getter
public class ErrorResponse {
    private int status;
    private String message;
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> violationErrors;

    private ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolationError> violationErrors) {
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {

        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {

        return new ErrorResponse(exceptionCode.getStatus(), exceptionCode.getMessage());
    }

    public static ErrorResponse of(HttpStatus httpStatus) {

        return new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {

        return new ErrorResponse(httpStatus.value(), message);
    }
}