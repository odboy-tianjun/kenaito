package cn.odboy.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * TOOL邮件发送记录
 *
 * @author odboy
 * @date 2024-09-06
 */
@Getter
@Setter
@TableName("system_tool_email_log")
public class EmailLog {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 邮件类型
     */
    @TableField("email_type")
    private String emailType;

    /**
     * 谁发的
     */
    @TableField("from_user")
    private String fromUser;

    /**
     * 发给谁
     */
    @TableField("to_user")
    private String toUser;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 异常信息
     */
    @TableField("err_msg")
    private String errMsg;

    /**
     * 发送状态(1已发送2发送成功3发送失败)
     */
    @TableField("send_status")
    private Integer sendStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class SendContent extends MyObject {
        /**
         * 收件人，支持多个收件人
         */
        @NotEmpty
        private List<String> tos;
        @NotBlank
        private String subject;
        @NotBlank
        private String content;
    }
}
