package cn.odboy.config;

import cn.odboy.infra.context.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限动态校验
 *
 * @date 2022-01-05
 */
@Service(value = "el")
public class AuthorityConfig {

    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtil.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
