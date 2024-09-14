package cn.odboy.mapper;

import cn.odboy.domain.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date 2023-06-12
 **/
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    List<SysLog> queryAll(@Param("criteria") SysLog.QueryArgs criteria);

    IPage<SysLog> queryAll(@Param("criteria") SysLog.QueryArgs criteria, Page<SysLog> page);

    String getExceptionDetails(@Param("id") Long id);

    void deleteByLevel(@Param("logType") String logType);
}
