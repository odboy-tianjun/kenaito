package cn.odboy.modules.quartz.rest;

import cn.odboy.annotation.Log;
import cn.odboy.infra.context.SpringContextHolder;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.modules.quartz.domain.QuartzJob;
import cn.odboy.modules.quartz.service.QuartzJobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
@Api(tags = "系统：定时任务管理")
public class QuartzJobController {
    private final QuartzJobService quartzJobService;

    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<Object> queryQuartzJob(QuartzJob.QueryArgs criteria, Page<Object> page) {
        return new ResponseEntity<>(quartzJobService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('timing:list')")
    public void exportQuartzJob(HttpServletResponse response, QuartzJob.QueryArgs criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria), response);
    }

    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('timing:list')")
    public void exportQuartzJobLog(HttpServletResponse response, QuartzJob.QueryArgs criteria) throws IOException {
        quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria), response);
    }

    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<Object> queryQuartzJobLog(QuartzJob.QueryArgs criteria, Page<Object> page) {
        return new ResponseEntity<>(quartzJobService.queryAllLog(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询任务模块类型")
    @GetMapping(value = "/modules")
    @PreAuthorize("@el.check('timing:list')")
    public ResponseEntity<Object> queryQuartzJobModules() {
        return new ResponseEntity<>(quartzJobService.queryQuartzJobModules(), HttpStatus.OK);
    }

    @Log("新增定时任务")
    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('timing:add')")
    public ResponseEntity<Object> createQuartzJob(@Validated @RequestBody QuartzJob resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + QuartzJob.class.getSimpleName() + " cannot already have an ID");
        }
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        quartzJobService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改定时任务")
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> updateQuartzJob(@Validated(QuartzJob.Update.class) @RequestBody QuartzJob resources) {
        // 验证Bean是不是合法的，合法的定时任务 Bean 需要用 @Service 定义
        checkBean(resources.getBeanName());
        quartzJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> updateQuartzJobStatus(@PathVariable Long id) {
        quartzJobService.updateIsPause(quartzJobService.getById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("执行定时任务")
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResponseEntity<Object> executionQuartzJob(@PathVariable Long id) {
        quartzJobService.execution(quartzJobService.getById(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('timing:del')")
    public ResponseEntity<Object> deleteQuartzJob(@RequestBody Set<Long> ids) {
        quartzJobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkBean(String beanName) {
        // 避免调用攻击者可以从SpringContextHolder获得控制jdbcTemplate类
        // 并使用getDeclaredMethod调用jdbcTemplate的queryForMap函数，执行任意sql命令。
        if (!SpringContextHolder.getAllServiceBeanName().contains(beanName)) {
            throw new BadRequestException("非法的 Bean，请重新输入！");
        }
    }
}
