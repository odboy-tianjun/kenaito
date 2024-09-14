package cn.odboy.modules.quartz.service;

import cn.odboy.base.model.SelectOption;
import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.quartz.domain.QuartzJob;
import cn.odboy.modules.quartz.domain.QuartzLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface QuartzJobService extends IService<QuartzJob> {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<QuartzJob> queryAll(QuartzJob.QueryArgs criteria, Page<Object> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<QuartzJob> queryAll(QuartzJob.QueryArgs criteria);

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<QuartzLog> queryAllLog(QuartzJob.QueryArgs criteria, Page<Object> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<QuartzLog> queryAllLog(QuartzJob.QueryArgs criteria);

    /**
     * 创建
     *
     * @param resources /
     */
    void create(QuartzJob resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(QuartzJob resources);

    /**
     * 删除任务
     *
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 更改定时任务状态
     *
     * @param quartzJob /
     */
    void updateIsPause(QuartzJob quartzJob);

    /**
     * 立即执行定时任务
     *
     * @param quartzJob /
     */
    void execution(QuartzJob quartzJob);

    /**
     * 导出定时任务
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<QuartzJob> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 导出定时任务日志
     *
     * @param queryAllLog 待导出的数据
     * @param response    /
     * @throws IOException /
     */
    void downloadLog(List<QuartzLog> queryAllLog, HttpServletResponse response) throws IOException;

    /**
     * 执行子任务
     *
     * @param tasks /
     * @throws InterruptedException /
     */
    void executionSubJob(String[] tasks) throws InterruptedException;

    List<SelectOption> queryQuartzJobModules();
}
