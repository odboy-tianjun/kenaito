package cn.odboy.modules.system.service;

import cn.odboy.infra.response.PageResult;
import cn.odboy.modules.system.domain.DictDetail;
import cn.odboy.modules.system.domain.vo.DictDetailQueryCriteria;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DictDetailService extends IService<DictDetail> {

    /**
     * 创建
     *
     * @param resources /
     */
    void create(DictDetail resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(DictDetail resources);

    /**
     * 删除
     *
     * @param id /
     */
    void delete(Long id);

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<DictDetail> queryAll(DictDetailQueryCriteria criteria, Page<Object> page);

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */
    List<DictDetail> getDictByName(String name);
}