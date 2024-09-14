package cn.odboy.service.impl;

import cn.odboy.domain.App;
import cn.odboy.mapper.AppMapper;
import cn.odboy.service.AppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用 服务实现类
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

}
