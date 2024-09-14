package cn.odboy.service;

import cn.odboy.domain.SysLog;
import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询
     *
     * @param criteria 查询条件
     * @param page     分页参数
     * @return /
     */
    PageResult<SysLog> queryAll(SysLog.QueryArgs criteria, Page<SysLog> page);

    /**
     * 查询全部数据
     *
     * @param criteria 查询条件
     * @return /
     */
    List<SysLog> queryAll(SysLog.QueryArgs criteria);

    /**
     * 查询用户日志
     *
     * @param criteria 查询条件
     * @param page     分页参数
     * @return -
     */
    PageResult<SysLog> queryAllByUser(SysLog.QueryArgs criteria, Page<SysLog> page);

    /**
     * 保存日志数据
     *
     * @param username  用户
     * @param browser   浏览器
     * @param ip        请求IP
     * @param joinPoint /
     * @param sysLog    日志实体
     */
    @Async
    void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, SysLog sysLog);

    /**
     * 查询异常详情
     *
     * @param id 日志ID
     * @return Object
     */
    Object findByErrDetail(Long id);

    /**
     * 导出日志
     *
     * @param sysLogs  待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<SysLog> sysLogs, HttpServletResponse response) throws IOException;

    /**
     * 删除所有错误日志
     */
    void delAllByError();

    /**
     * 删除所有INFO日志
     */
    void delAllByInfo();
}
