package cn.odboy.mapper;

import cn.odboy.domain.ColumnConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ColumnInfoMapper extends BaseMapper<ColumnConfig> {

    IPage<ColumnConfig.QueryPage> getTables(@Param("tableName") String tableName, Page<Object> page);

    List<ColumnConfig> findByTableNameOrderByIdAsc(@Param("tableName") String tableName);

    List<ColumnConfig> getColumns(@Param("tableName") String tableName);
}
