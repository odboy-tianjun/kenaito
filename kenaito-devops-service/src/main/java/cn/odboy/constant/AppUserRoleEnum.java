package cn.odboy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应用管理人员角色
 *
 * @author odboy
 * @date 2024-09-13
 */
@Getter
@AllArgsConstructor
public enum AppUserRoleEnum {
    DBA("dba", "数据库管理员"),
    NETWORK("network", "网络管理员"),
    CONFIG("config", "配置管理员"),
    DEV("dev", "开发人员"),
    DEPLOYER("deployer", "部署人员"),
    OWNER("owner", "应用负责人");

    private final String code;
    private final String desc;

    public static AppUserRoleEnum getByCode(String code) {
        for (AppUserRoleEnum role : AppUserRoleEnum.values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        return null;
    }
}
