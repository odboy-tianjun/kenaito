package cn.odboy.service;

import cn.odboy.domain.GenConfig;
import com.baomidou.mybatisplus.extension.service.IService;


public interface GenConfigService extends IService<GenConfig> {

    /**
     * 查询表配置
     *
     * @param tableName 表名
     * @return 表配置
     */
    GenConfig find(String tableName);

    /**
     * 更新表配置
     *
     * @param tableName 表名
     * @param genConfig 表配置
     * @return 表配置
     */
    GenConfig update(String tableName, GenConfig genConfig);
}
