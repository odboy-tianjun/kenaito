package cn.odboy.modules.system.rest;

import cn.odboy.constant.CodeBiEnum;
import cn.odboy.constant.CodeEnum;
import cn.odboy.constant.EmailTypeEnum;
import cn.odboy.domain.EmailLog;
import cn.odboy.modules.system.service.VerifyService;
import cn.odboy.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @date 2018-12-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
@Api(tags = "系统：验证码管理")
public class VerifyController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public ResponseEntity<Object> resetEmail(@RequestParam String email) {
        EmailLog.SendContent emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(EmailTypeEnum.RESET_PASSWORD_CAPTCHA, emailVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity<Object> resetPass(@RequestParam String email) {
        EmailLog.SendContent emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(EmailTypeEnum.RESET_PASSWORD_CAPTCHA, emailVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity<Object> validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
