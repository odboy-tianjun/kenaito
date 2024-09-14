package cn.odboy.infra.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {NotEmptyListValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyList {
    boolean required() default false;

    String message() default "参数列表长度必须大于1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}