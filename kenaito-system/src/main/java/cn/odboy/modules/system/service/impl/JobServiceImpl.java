package cn.odboy.modules.system.service.impl;

import cn.odboy.constant.CacheKey;
import cn.odboy.infra.exception.EntityExistException;
import cn.odboy.infra.redis.RedisUtil;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.response.PageUtil;
import cn.odboy.modules.system.domain.Job;
import cn.odboy.modules.system.domain.vo.JobQueryCriteria;
import cn.odboy.modules.system.mapper.JobMapper;
import cn.odboy.modules.system.mapper.UserMapper;
import cn.odboy.modules.system.service.JobService;
import cn.odboy.util.MyFileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "job")
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final JobMapper jobMapper;
    private final RedisUtil redisUtils;
    private final UserMapper userMapper;

    @Override
    public PageResult<Job> queryAll(JobQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(jobMapper.findAll(criteria, page));
    }

    @Override
    public List<Job> queryAll(JobQueryCriteria criteria) {
        return jobMapper.findAll(criteria);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public Job findById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Job resources) {
        Job job = jobMapper.findByName(resources.getName());
        if (job != null) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        save(resources);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Job job = getById(resources.getId());
        Job old = jobMapper.findByName(resources.getName());
        if (old != null && !old.getId().equals(resources.getId())) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        resources.setId(job.getId());
        saveOrUpdate(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除缓存
        redisUtils.delByKeys(CacheKey.JOB_ID, ids);
    }

    @Override
    public void download(List<Job> jobs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Job job : jobs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", job.getName());
            map.put("岗位状态", job.getEnabled() ? "启用" : "停用");
            map.put("创建日期", job.getCreateTime());
            list.add(map);
        }
        MyFileUtil.downloadExcel(list, response);
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userMapper.countByJobs(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }
}