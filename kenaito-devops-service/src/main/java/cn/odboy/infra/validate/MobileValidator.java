package cn.odboy.infra.validate;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.constant.RegexConst;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验手机号
 *
 * @author odboy
 * @date 2024-09-13
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    private boolean required = false;

    @Override
    public void initialize(Mobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            if (StrUtil.isBlank(value)) {
                return false;
            }
        } else {
            if (StrUtil.isBlank(value)) {
                return true;
            }
        }
        return ReUtil.isMatch(RegexConst.PHONE_NUMBER, value);
    }
}
