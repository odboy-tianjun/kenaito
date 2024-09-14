package cn.odboy.modules.system.mapper;

import cn.odboy.modules.system.domain.Dict;
import cn.odboy.modules.system.domain.vo.DictQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    List<Dict> findAll(@Param("criteria") DictQueryCriteria criteria);

    Long countAll(@Param("criteria") DictQueryCriteria criteria);
}