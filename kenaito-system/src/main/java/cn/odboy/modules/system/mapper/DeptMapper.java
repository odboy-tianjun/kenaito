package cn.odboy.modules.system.mapper;

import cn.odboy.modules.system.domain.Dept;
import cn.odboy.modules.system.domain.vo.DeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;


@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    List<Dept> findAll(@Param("criteria") DeptQueryCriteria criteria);

    List<Dept> findByPid(@Param("pid") Long pid);

    List<Dept> findByPidIsNull();

    Set<Dept> findByRoleId(@Param("roleId") Long roleId);

    @Select("select count(*) from system_dept where pid = #{pid}")
    int countByPid(@Param("pid") Long pid);

    @Select("update system_dept set sub_count = #{count} where dept_id = #{id}")
    void updateSubCntById(@Param("count") Integer count, @Param("id") Long id);
}