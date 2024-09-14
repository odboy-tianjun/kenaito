package cn.odboy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件发送状态
 * @author odboy
 * @date 2024-09-06
 */
@AllArgsConstructor
@Getter
public enum EmailSendStatusEnum {
    SENDED(1, "已发送"),
    SEND_SUCCESS(2, "发送成功"),
    SEND_ERROR(3, "发送失败");

    private final Integer code;
    private final String description;
}
