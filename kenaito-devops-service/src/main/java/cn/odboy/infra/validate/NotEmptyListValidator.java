package cn.odboy.infra.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * 校验列表长度
 *
 * @author odboy
 * @date 2024-09-13
 */
public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List<?>> {
    private boolean required = false;

    @Override
    public void initialize(NotEmptyList constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(List<?> values, ConstraintValidatorContext context) {
        if (required) {
            return values != null && !values.isEmpty();
        } else {
            return true;
        }
    }
}
