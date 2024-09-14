package cn.odboy.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.odboy.constant.EmailSendStatusEnum;
import cn.odboy.constant.EmailTypeEnum;
import cn.odboy.domain.EmailLog;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.mapper.EmailLogMapper;
import cn.odboy.service.EmailService;
import cn.odboy.util.ThrowableUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl extends ServiceImpl<EmailLogMapper, EmailLog> implements EmailService {
    @Value("${spring.mail.username}")
    private String fromUsername;
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Override
    public void send(EmailTypeEnum emailType, EmailLog.SendContent request) {
        int size = request.getTos().size();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromUsername);
        simpleMailMessage.setTo(request.getTos().toArray(new String[size]));
        simpleMailMessage.setSubject(request.getSubject());
        simpleMailMessage.setText(request.getContent());
        ThreadUtil.execAsync(() -> {
            EmailLog record = new EmailLog();
            record.setEmailType(emailType.getCode());
            record.setFromUser(fromUsername);
            record.setToUser(String.join(",", request.getTos()));
            record.setTitle(request.getSubject());
            record.setContent(request.getContent());
            record.setSendStatus(EmailSendStatusEnum.SENDED.getCode());
            save(record);

            EmailLog updateRecord = new EmailLog();
            updateRecord.setId(record.getId());
            try {
                mailSender.send(simpleMailMessage);
                updateRecord.setSendStatus(EmailSendStatusEnum.SEND_SUCCESS.getCode());
                updateById(updateRecord);
            } catch (Exception e) {
                log.error("邮件发送失败", e);
                updateRecord.setSendStatus(EmailSendStatusEnum.SEND_ERROR.getCode());
                updateRecord.setErrMsg(ThrowableUtil.getStackTrace(e));
                updateById(updateRecord);
                throw new BadRequestException("邮件发送失败");
            }
        });
    }
}
