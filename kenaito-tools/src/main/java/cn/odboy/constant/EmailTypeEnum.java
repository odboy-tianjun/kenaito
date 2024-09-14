package cn.odboy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件类型
 *
 * @author odboy
 * @date 2024-09-06
 */
@Getter
@AllArgsConstructor
public enum EmailTypeEnum {
    TEST("Test", "邮件服务联通性测试"),
    LOGIN_CAPTCHA("Login", "用户登录"),
    RESET_PASSWORD_CAPTCHA("ResetPassword", "重置密码"),
    RESET_Email_CAPTCHA("ResetEmail", "重置邮箱"),
    REGISTER_CAPTCHA("Register", "用户注册"),
    TASK_ALARM("TaskAlarm", "任务告警");

    private final String code;
    private final String description;

    public static EmailTypeEnum getByCode(String code) {
        for (EmailTypeEnum emailTypeEnum : EmailTypeEnum.values()) {
            if (emailTypeEnum.getCode().equals(code)) {
                return emailTypeEnum;
            }
        }
        return null;
    }
}
