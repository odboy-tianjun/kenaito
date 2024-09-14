package cn.odboy.modules.quartz.mapper;

import cn.odboy.base.model.SelectOption;
import cn.odboy.modules.quartz.domain.QuartzJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    IPage<QuartzJob> findAll(@Param("criteria") QuartzJob.QueryArgs criteria, Page<Object> page);

    List<QuartzJob> findAll(@Param("criteria") QuartzJob.QueryArgs criteria);

    List<QuartzJob> findByIsPauseIsFalse();

    List<SelectOption> findQuartzJobModuleOptions();
}
