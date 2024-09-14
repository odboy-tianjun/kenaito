package cn.odboy.infra.gitlab.context;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Autowired;

public class GitlabAdmin {
    @Autowired
    private GitlabProperties properties;

    protected GitLabApi auth() {
        return new GitLabApi(properties.getUrl(), properties.getAccessToken());
    }
}
