package cn.odboy.rest;

import cn.odboy.annotation.Log;
import cn.odboy.constant.EmailTypeEnum;
import cn.odboy.domain.EmailLog;
import cn.odboy.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送邮件
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/email")
@Api(tags = "工具：邮件管理")
public class EmailController {

    private final EmailService emailService;

    @Log("发送邮件")
    @PostMapping
    @ApiOperation("发送邮件")
    public ResponseEntity<Object> sendEmail(@Validated @RequestBody EmailLog.SendContent request) {
        emailService.send(EmailTypeEnum.TEST, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
