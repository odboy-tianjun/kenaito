package cn.odboy.modules.system.mapper;

import cn.odboy.modules.system.domain.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


@Mapper
public interface RoleDeptMapper {
    void insertData(@Param("roleId") Long roleId, @Param("depts") Set<Dept> depts);

    void deleteByRoleId(@Param("roleId") Long roleId);

    void deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
