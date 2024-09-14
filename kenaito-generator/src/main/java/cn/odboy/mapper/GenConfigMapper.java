package cn.odboy.mapper;

import cn.odboy.domain.GenConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GenConfigMapper extends BaseMapper<GenConfig> {

    GenConfig findByTableName(@Param("tableName") String tableName);
}
