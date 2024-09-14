package cn.odboy.service.impl;

import cn.odboy.domain.AppUser;
import cn.odboy.mapper.AppUserMapper;
import cn.odboy.service.AppUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用、成员关联 服务实现类
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

}
