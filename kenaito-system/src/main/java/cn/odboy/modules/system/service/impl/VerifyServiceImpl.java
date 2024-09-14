package cn.odboy.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.odboy.config.AppProperties;
import cn.odboy.domain.EmailLog;
import cn.odboy.infra.redis.RedisUtil;
import cn.odboy.modules.system.service.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.util.Collections;

/**
 * @date 2018-12-26
 */
@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {
    private final RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmailLog.SendContent sendEmail(String email, String key) {
        EmailLog.SendContent emailVo;
        String content;
        String redisKey = key + email;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("CaptchaTemplate.ftl");
        Object oldCode = redisUtil.get(redisKey);
        if (oldCode == null) {
            String code = RandomUtil.randomNumbers(6);
            // 存入缓存
            if (!redisUtil.set(redisKey, code, AppProperties.captchaEmailExpireTime)) {
                throw new cn.odboy.infra.exception.BadRequestException("服务异常，请联系网站负责人");
            }
            content = template.render(Dict.create().set("code", code));
            emailVo = new EmailLog.SendContent(Collections.singletonList(email), "Kenaito", content);
            // 存在就再次发送原来的验证码
        } else {
            content = template.render(Dict.create().set("code", oldCode));
            emailVo = new EmailLog.SendContent(Collections.singletonList(email), "Kenaito", content);
        }
        return emailVo;
    }

    @Override
    public void validated(String key, String code) {
        Object value = redisUtil.get(key);
        if (value == null || !value.toString().equals(code)) {
            throw new BadRequestException("无效验证码");
        } else {
            redisUtil.del(key);
        }
    }
}
