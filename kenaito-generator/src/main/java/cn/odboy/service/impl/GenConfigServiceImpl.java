package cn.odboy.service.impl;

import cn.odboy.domain.GenConfig;
import cn.odboy.mapper.GenConfigMapper;
import cn.odboy.service.GenConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "all"})
public class GenConfigServiceImpl extends ServiceImpl<GenConfigMapper, GenConfig> implements GenConfigService {

    private final GenConfigMapper genConfigMapper;

    @Override
    public GenConfig find(String tableName) {
        GenConfig genConfig = genConfigMapper.findByTableName(tableName);
        if (genConfig == null) {
            return new GenConfig(tableName);
        }
        return genConfig;
    }

    @Override
    public GenConfig update(String tableName, GenConfig genConfig) {
        String separator = File.separator;
        String[] paths;
        String symbol = "\\";
        if (symbol.equals(separator)) {
            paths = genConfig.getPath().split("\\\\");
        } else {
            paths = genConfig.getPath().split(File.separator);
        }
        StringBuilder api = new StringBuilder();
        for (String path : paths) {
            api.append(path);
            api.append(separator);
            if ("src".equals(path)) {
                api.append("api");
                break;
            }
        }
        genConfig.setApiPath(api.toString());
        saveOrUpdate(genConfig);
        return genConfig;
    }
}
