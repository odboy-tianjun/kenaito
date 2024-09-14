package cn.odboy.service;

import cn.odboy.constant.EmailTypeEnum;
import cn.odboy.domain.EmailLog;
import com.baomidou.mybatisplus.extension.service.IService;


public interface EmailService extends IService<EmailLog> {
    /**
     * 发送邮件
     *
     * @param emailType 邮件类型
     * @param request   邮件发送的内容
     */
    void send(EmailTypeEnum emailType, EmailLog.SendContent request);
}
