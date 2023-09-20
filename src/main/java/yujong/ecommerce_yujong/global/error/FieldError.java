package yujong.ecommerce_yujong.global.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FieldError {
    private String field;
    private Object rejectedValue;
    private String reason;

    private FieldError(String field, Object rejectedValue, String reason) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.reason = reason;
    }
    public static List<FieldError> of(BindingResult bindingResult) {
        final List<org.springframework.validation.FieldError> fieldErrorList = bindingResult.getFieldErrors();

        return fieldErrorList.stream()
                .map(error -> new FieldError(error.getField(),
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()
                )).collect(Collectors.toList());
    }
}