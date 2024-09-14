package cn.odboy.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用配置
 *
 * @date 2024-06-05
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    public static String privateKey;
    public static Long captchaEmailExpireTime;

    @Value("${app.private-key}")
    public void setPrivateKey(String privateKey) {
        AppProperties.privateKey = privateKey;
    }

    @Value("${app.captcha.email.expire-time}")
    public void setCaptchaEmailExpireTime(Long captchaEmailExpireTime) {
        AppProperties.captchaEmailExpireTime = captchaEmailExpireTime;
    }

    private MinioProp minio;

    @Data
    public static class MinioProp {
        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String region;
        private Integer shareExpireTime;
    }
}
