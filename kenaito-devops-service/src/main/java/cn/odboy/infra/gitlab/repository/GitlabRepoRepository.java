package cn.odboy.infra.gitlab.repository;

import cn.hutool.core.util.StrUtil;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.gitlab.context.GitlabAdmin;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.MergeRequestApi;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Gitlab仓库
 *
 * @author odboy
 * @date 2024-09-09
 */
@Slf4j
@Component
public class GitlabRepoRepository extends GitlabAdmin {
    @Autowired
    private GitlabProjectRepository projectRepository;

    /**
     * 根据projectId创建分支
     *
     * @param projectId  项目id
     * @param branchName 分支名称
     * @param ref        从什么分支创建
     * @return /
     */
    public Branch createBranchByProjectId(Long projectId, String branchName, String ref) {
        Project project = projectRepository.getByProjectId(projectId);
        if (project == null) {
            throw new BadRequestException("创建分支失败, 项目不存在");
        }
        try (GitLabApi client = auth()) {
            RepositoryApi repositoryApi = client.getRepositoryApi();
            if (StrUtil.isBlank(ref)) {
                ref = project.getDefaultBranch();
            }
            return repositoryApi.createBranch(projectId, branchName, ref);
        } catch (Exception e) {
            log.error("根据projectId创建分支失败", e);
            throw new BadRequestException("根据projectId创建分支失败");
        }
    }

    /**
     * 根据appName创建分支
     *
     * @param appName    应用名称
     * @param branchName 分支名称
     * @param ref        从什么分支创建
     * @return /
     */
    public Branch createBranchByAppName(String appName, String branchName, String ref) {
        Project project = projectRepository.getByAppName(appName);
        if (project == null) {
            throw new BadRequestException("创建分支失败, 项目不存在");
        }
        try (GitLabApi client = auth()) {
            RepositoryApi repositoryApi = client.getRepositoryApi();
            if (StrUtil.isBlank(ref)) {
                ref = project.getDefaultBranch();
            }
            // 根据path创建分支有问题
            return repositoryApi.createBranch(project.getId(), branchName, ref);
        } catch (Exception e) {
            log.error("根据appName创建分支失败", e);
            throw new BadRequestException("根据appName创建分支失败");
        }
    }

    public Branch getBranchByProjectId(Long projectId, String branchName) {
        try (GitLabApi client = auth()) {
            return client.getRepositoryApi().getBranch(projectId, branchName);
        } catch (Exception e) {
            log.error("根据projectId获取分支失败", e);
            throw new BadRequestException("根据projectId获取分支失败");
        }
    }

    public Branch getBranchByAppName(String appName, String branchName) {
        Project project = projectRepository.getByAppName(appName);
        if (project == null) {
            throw new BadRequestException("根据appName获取分支失败, 项目不存在");
        }
        try (GitLabApi client = auth()) {
            return client.getRepositoryApi().getBranch(project.getId(), branchName);
        } catch (Exception e) {
            log.error("根据appName获取分支失败", e);
            throw new BadRequestException("根据appName获取分支失败");
        }
    }

    public List<Branch> listBranchByProjectId(Long projectId, String search) {
        return listBranches(projectId, search);
    }

    public List<Branch> listBranchByAppName(String appName, String search) {
        Project project = projectRepository.getByAppName(appName);
        if (project == null) {
            throw new BadRequestException("根据appName获取分支失败, 项目不存在");
        }
        return listBranches(project.getId(), search);
    }

    private List<Branch> listBranches(Long projectId, String searchKey) {
        List<Branch> list = new ArrayList<>();
        try (GitLabApi client = auth()) {
            Pager<Branch> pager = client.getRepositoryApi().getBranches(projectId, searchKey, 100);
            while (pager.hasNext()) {
                list.addAll(pager.next());
            }
        } catch (Exception e) {
            log.error("获取分支列表失败", e);
        }
        return list;
    }

    public void deleteBranchByProjectId(Long projectId, String branchName) {
        try (GitLabApi client = auth()) {
            client.getRepositoryApi().deleteBranch(projectId, branchName);
        } catch (Exception e) {
            log.error("根据projectId删除分支失败", e);
        }
    }

    public void deleteBranchByAppName(String appName, String branchName) {
        Project project = projectRepository.getByAppName(appName);
        if (project == null) {
            throw new BadRequestException("根据appName删除分支失败, 项目不存在");
        }
        try (GitLabApi client = auth()) {
            client.getRepositoryApi().deleteBranch(project.getId(), branchName);
        } catch (Exception e) {
            log.error("根据appName删除分支失败", e);
        }
    }

    /**
     * 根据projectId比较分支(合并分支之前, 展示改动的内容)
     *
     * @param projectId  项目id
     * @param fromBranch 分支1
     * @param toBranch   分支2
     * @return /
     */
    public CompareResults compareByProjectId(Long projectId, String fromBranch, String toBranch) {
        try (GitLabApi client = auth()) {
            return client.getRepositoryApi().compare(projectId, fromBranch, toBranch);
        } catch (Exception e) {
            log.error("根据projectId比较分支差异失败", e);
            return null;
        }
    }

    /**
     * 根据appName比较分支(合并分支之前, 展示改动的内容)
     *
     * @param appName      应用名称
     * @param sourceBranch 源分支
     * @param targetBranch 目标分支
     * @return /
     */
    public CompareResults compareByAppName(String appName, String sourceBranch, String targetBranch) {
        Project project = projectRepository.getByAppName(appName);
        if (project == null) {
            throw new BadRequestException("根据projectId比较分支差异失败, 项目不存在");
        }
        try (GitLabApi client = auth()) {
            // 比较两个分支
            CompareResults compareResults = client.getRepositoryApi().compare(project.getId(), sourceBranch, targetBranch);
            // 输出差异信息
            System.out.println("Commits:");
            for (Commit commit : compareResults.getCommits()) {
                System.out.println("  " + commit.getId() + " - " + commit.getMessage());
            }
            System.out.println("Diffs:");
            for (Diff diff : compareResults.getDiffs()) {
                System.out.println("  " + diff.getOldPath() + " -> " + diff.getNewPath());
            }
            return compareResults;
        } catch (Exception e) {
            log.error("根据projectId比较分支差异失败", e);
            return null;
        }
    }

    public MergeRequest createMergeRequest(Long projectId, String sourceBranch, String targetBranch, String title, String description) {
        if (projectId == null) {
            throw new BadRequestException("参数 projectId 必填");
        }
        if (StrUtil.isBlank(sourceBranch)) {
            throw new BadRequestException("参数 sourceBranch 必填");
        }
        if (StrUtil.isBlank(targetBranch)) {
            throw new BadRequestException("参数 targetBranch 必填");
        }
        if (StrUtil.isBlank(title)) {
            title = String.format("%s 合并到 %s", sourceBranch, targetBranch);
        }
        try (GitLabApi client = auth()) {
            MergeRequestParams params = new MergeRequestParams();
            params.withTargetProjectId(projectId);
            params.withSourceBranch(sourceBranch);
            params.withTargetBranch(targetBranch);
            params.withTitle(title);
            params.withDescription(description);
            MergeRequestApi mergeRequestApi = client.getMergeRequestApi();
            return mergeRequestApi.createMergeRequest(projectId, params);
        } catch (Exception e) {
            log.error("创建分支合并请求失败", e);
            throw new BadRequestException("创建分支合并请求失败");
        }
    }


    public MergeRequest getMergeRequestByProjectId(Long projectId, Long mergeRequestId) {
        try (GitLabApi client = auth()) {
            return client.getMergeRequestApi().getMergeRequest(projectId, mergeRequestId);
        } catch (Exception e) {
            log.error("根据projectId获取分支详情", e);
            return null;
        }
    }

    public MergeRequest getMergeRequestByAppName(String appName, Long mergeRequestId) {
        Project project = projectRepository.getByAppName(appName);
        if (project == null) {
            throw new BadRequestException("根据appName获取分支详情失败, 项目不存在");
        }
        try (GitLabApi client = auth()) {
            return client.getMergeRequestApi().getMergeRequest(project.getId(), mergeRequestId);
        } catch (Exception e) {
            log.error("根据appName获取分支详情失败", e);
            return null;
        }
    }

    public MergeRequest acceptMergeRequest(Long projectId, Long mergeRequestIid) {
        try (GitLabApi client = auth()) {
            MergeRequest mergeRequest = client.getMergeRequestApi().getMergeRequest(projectId, mergeRequestIid);
            AcceptMergeRequestParams params = new AcceptMergeRequestParams();
            params.withShouldRemoveSourceBranch(false);
            params.withMergeCommitMessage(mergeRequest.getTitle() == null ? mergeRequest.getDescription() : mergeRequest.getTitle());
            return client.getMergeRequestApi().acceptMergeRequest(projectId, mergeRequestIid, params);
        } catch (Exception e) {
            log.error("接受合并请求失败", e);
            throw new BadRequestException("接受合并请求失败");
        }
    }
}
