
package cn.odboy.constant;

/**
 * @date: 2020/6/11 15:49
 * 关于缓存的Key集合
 */
public interface CacheKey {

    /**
     * 用户
     */
    String USER_ID = "user::id:";
    /**
     * 数据
     */
    String DATA_USER = "data::user:";
    /**
     * 菜单
     */
    String MENU_ID = "menu::id:";
    String MENU_USER = "menu::user:";
    /**
     * 角色授权
     */
    String ROLE_AUTH = "role::auth:";
    /**
     * 角色信息
     */
    String ROLE_ID = "role::id:";
    /**
     * 部门
     */
    String DEPT_ID = "dept::id:";
    /**
     * 岗位
     */
    String JOB_ID = "job::id:";
    /**
     * 数据字典
     */
    String DICT_NAME = "dict::name:";
}
