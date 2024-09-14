package ${package}.mapper;

import ${package}.domain.${className};
import ${package}.domain.vo.${className}QueryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author ${author}
* @date ${date}
**/
@Mapper
public interface ${className}Mapper extends BaseMapper<${className}> {

IPage<${className}> findAll(@Param("criteria") ${className}QueryCriteria criteria, Page
<Object> page);

    List<${className}> findAll(@Param("criteria") ${className}QueryCriteria criteria);
    }
