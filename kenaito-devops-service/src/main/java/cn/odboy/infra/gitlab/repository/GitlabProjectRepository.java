package cn.odboy.infra.gitlab.repository;

import cn.hutool.core.util.StrUtil;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.gitlab.context.GitlabAdmin;
import cn.odboy.infra.gitlab.context.GitlabConst;
import cn.odboy.infra.gitlab.model.GitlabProject;
import cn.odboy.util.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Namespace;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Visibility;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Gitlab项目
 *
 * @author odboy
 * @date 2024-09-09
 */
@Slf4j
@Component
public class GitlabProjectRepository extends GitlabAdmin {

    /**
     * 创建项目 -> ok
     *
     * @param args /
     * @return /
     */
    public GitlabProject.CreateResp create(GitlabProject.CreateArgs args) {
        String appName = args.getAppName();
        if (StrUtil.isBlank(appName)) {
            throw new BadRequestException("项目名称必填");
        }
        String newAppName = appName.trim();
//        字符串必须以小写字母开头。
//        字符串中间可以包含小写字母和数字。
//        - 符号可以出现多次，并且 - 的左右两边都必须是小写字母或数字
        if (!Pattern.compile(GitlabConst.REGEX_APP_NAME).matcher(newAppName).matches()) {
            throw new BadRequestException("项目名称格式不正确, 只能由小写字母、数字与符号-组成");
        }
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            Optional<Project> loadProjectOptional = projectApi.getProjectsStream().filter(f -> f.getPath().equals(args.getAppName())).findFirst();
            if (loadProjectOptional.isPresent()) {
                throw new BadRequestException("项目名称已存在");
            }
            Project project = new Project();
            project.setName(StrUtil.isBlank(args.getName()) ? newAppName : args.getName());
            project.setPath(newAppName);
            project.setDescription(args.getDescription());
            project.setVisibility(Visibility.PRIVATE);
            project.setInitializeWithReadme(true);
            project.setDefaultBranch(GitlabConst.PROJECT_DEFAULT_BRANCH);
            Namespace namespace = client.getNamespaceApi().getNamespace(1L);
            project.setNamespace(namespace);
            Project newProject = projectApi.createProject(GitlabConst.ROOT_NAMESPACE_ID, project);
            return transformCreateResp(args, newProject, newAppName);
        } catch (Exception e) {
            log.error("创建项目失败", e);
            throw new BadRequestException("创建项目失败");
        }
    }

    private GitlabProject.CreateResp transformCreateResp(GitlabProject.CreateArgs args, Project newProject, String newAppName) {
        GitlabProject.CreateResp createResp = new GitlabProject.CreateResp();
        createResp.setCreatorId(newProject.getCreatorId());
        createResp.setCreatedAt(newProject.getCreatedAt());
        createResp.setDefaultBranch(newProject.getDefaultBranch());
        createResp.setHttpUrlToRepo(newProject.getHttpUrlToRepo());
        createResp.setProjectId(newProject.getId());
        createResp.setVisibility(newProject.getVisibility().name());
        createResp.setHomeUrl(newProject.getWebUrl());
        createResp.setName(args.getName());
        createResp.setAppName(newAppName);
        createResp.setDescription(args.getDescription());
        return createResp;
    }

    /**
     * 新增项目成员 -> ok
     *
     * @param projectId   项目id
     * @param userId      用户id
     * @param accessLevel 访问级别
     */
    public void addMember(Long projectId, Long userId, AccessLevel accessLevel) {
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            projectApi.addMember(projectId, userId, accessLevel);
        } catch (Exception e) {
            log.error("新增项目成员失败", e);
            throw new BadRequestException("新增项目成员失败");
        }
    }

    /**
     * 批量新增项目成员 -> ok
     *
     * @param projectId   项目id
     * @param userIds     用户id列表
     * @param accessLevel 访问级别
     */
    public void batchAddMembers(Long projectId, List<Long> userIds, AccessLevel accessLevel) {
        if (CollUtil.isNotEmpty(userIds)) {
            for (Long userId : userIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList())) {
                try {
                    this.addMember(projectId, userId, accessLevel);
                } catch (Exception e) {
                    log.error("新增项目成员失败", e);
                }
            }
        }
    }

    /**
     * 移除项目成员 -> ok
     *
     * @param projectId 项目id
     * @param userId    用户id
     */
    public void removeMember(Long projectId, Long userId) {
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            projectApi.removeMember(projectId, userId);
        } catch (Exception e) {
            log.error("移除项目成员失败", e);
            throw new BadRequestException("移除项目成员失败");
        }
    }

    /**
     * 批量移除项目成员 -> ok
     *
     * @param projectId 项目id
     * @param userIds   用户id列表
     */
    public void batchRemoveMembers(Long projectId, List<Long> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            for (Long userId : userIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList())) {
                try {
                    this.removeMember(projectId, userId);
                } catch (Exception e) {
                    log.error("移除项目成员失败", e);
                }
            }
        }
    }

    /**
     * 通过projectId查项目 -> ok
     *
     * @param projectId /
     * @return /
     */
    public Project getByProjectId(Long projectId) {
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            return projectApi.getProjectsStream().filter(f -> f.getId().equals(projectId)).findFirst().orElse(null);
        } catch (Exception e) {
            log.error("通过projectId查项目失败", e);
            return null;
        }
    }

    /**
     * 通过appName查项目 -> ok
     *
     * @param appName /
     * @return /
     */
    public Project getByAppName(String appName) {
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            return projectApi.getProjectsStream().filter(f -> f.getPath().equals(appName)).findFirst().orElse(null);
        } catch (Exception e) {
            log.error("通过appName查项目失败", e);
            return null;
        }
    }

    /**
     * 分页获取项目 -> ok
     *
     * @param page 当前页
     * @return /
     */
    public List<Project> pageProjects(int page) {
        int newPage = page <= 0 ? 1 : page;
        List<Project> list = new ArrayList<>();
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            return projectApi.getProjects(newPage, 100);
        } catch (Exception e) {
            log.error("分页获取项目失败", e);
            return list;
        }
    }

    /**
     * 根据projectId删除项目
     *
     * @param projectId /
     */
    public void deleteByProjectId(Long projectId) {
        try (GitLabApi client = auth()) {
            ProjectApi projectApi = client.getProjectApi();
            projectApi.deleteProject(projectId);
        } catch (Exception e) {
            log.error("根据projectId删除项目失败", e);
            throw new BadRequestException("根据projectId删除项目失败");
        }
    }

    public void batchDeleteByProjectId(List<Long> projectIds) {
        if (CollUtil.isNotEmpty(projectIds)) {
            for (Long projectId : projectIds) {
                try {
                    this.deleteByProjectId(projectId);
                } catch (Exception e) {
                    log.error("根据projectId删除项目失败", e);
                }
            }
        }
    }

    /**
     * 根据appName删除项目
     *
     * @param appName /
     */
    public void deleteByAppName(String appName) {
        try (GitLabApi client = auth()) {
            Project localProject = this.getByAppName(appName);
            if (localProject == null) {
                throw new BadRequestException("项目不存在");
            }
            ProjectApi projectApi = client.getProjectApi();
            projectApi.deleteProject(localProject.getId());
        } catch (Exception e) {
            log.error("根据appName删除项目失败", e);
            throw new BadRequestException("根据appName删除项目失败");
        }
    }

    public void batchDeleteByAppName(List<String> appNames) {
        if (CollUtil.isNotEmpty(appNames)) {
            for (String appName : appNames) {
                try {
                    this.deleteByAppName(appName);
                } catch (Exception e) {
                    log.error("根据appName删除项目失败", e);
                }
            }
        }
    }
}
