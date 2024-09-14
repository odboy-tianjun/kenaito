package cn.odboy.modules.system.service;

import cn.odboy.modules.system.domain.User;

import java.util.List;

/**
 * 数据权限 服务类
 */
public interface DataService {

    /**
     * 获取数据权限
     *
     * @param user /
     * @return /
     */
    List<Long> getDeptIds(User user);
}
