package cn.odboy.infra.gitlab.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gitlab")
public class GitlabProperties {
    /**
     * gitlab地址
     */
    private String url;
    /**
     * root用户的令牌
     */
    private String accessToken;
}
