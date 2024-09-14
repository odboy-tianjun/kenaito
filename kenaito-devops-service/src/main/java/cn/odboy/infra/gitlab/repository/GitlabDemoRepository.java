package cn.odboy.infra.gitlab.repository;

import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.gitlab.context.GitlabAdmin;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GitlabDemoRepository extends GitlabAdmin {
    public void test() {
        try (GitLabApi client = auth()) {
        } catch (Exception e) {
            log.error("xxx失败", e);
            throw new BadRequestException("xxx失败");
        }
    }
}
