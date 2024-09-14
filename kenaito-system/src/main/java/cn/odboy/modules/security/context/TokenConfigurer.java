package cn.odboy.modules.security.context;

import cn.odboy.modules.security.config.SecurityProperties;
import cn.odboy.modules.security.repository.OnlineUserRepository;
import cn.odboy.modules.security.repository.UserCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final OnlineUserRepository onlineUserRepository;
    private final UserCacheManager userCacheManager;

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, properties, onlineUserRepository, userCacheManager);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
