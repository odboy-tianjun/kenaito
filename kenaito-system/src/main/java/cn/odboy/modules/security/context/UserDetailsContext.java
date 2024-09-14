package cn.odboy.modules.security.context;

import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.exception.EntityNotFoundException;
import cn.odboy.modules.security.domain.JwtUserDto;
import cn.odboy.modules.security.repository.UserCacheManager;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.service.DataService;
import cn.odboy.modules.system.service.RoleService;
import cn.odboy.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsContext implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;
    private final UserCacheManager userCacheManager;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (jwtUserDto == null) {
            User user;
            try {
                user = userService.getLoginData(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException(username, e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("账号不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活");
                }
                jwtUserDto = new JwtUserDto(
                        user,
                        dataService.getDeptIds(user),
                        roleService.mapToGrantedAuthorities(user)
                );
                // 添加缓存数据
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
