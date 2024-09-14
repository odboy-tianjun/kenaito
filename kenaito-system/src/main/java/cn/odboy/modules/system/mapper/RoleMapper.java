package cn.odboy.modules.system.mapper;

import cn.odboy.modules.system.domain.Role;
import cn.odboy.modules.system.domain.vo.RoleQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;


@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> queryAll();

    Role findById(@Param("roleId") Long roleId);

    Role findByName(@Param("name") String name);

    List<Role> findByUserId(@Param("userId") Long userId);

    Long countAll(@Param("criteria") RoleQueryCriteria criteria);

    List<Role> findAll(@Param("criteria") RoleQueryCriteria criteria);

    int countByDepts(@Param("deptIds") Set<Long> deptIds);

    @Select("SELECT role.role_id as id FROM system_role role, system_roles_menus rm " +
            "WHERE role.role_id = rm.role_id AND rm.menu_id = #{menuId}")
    List<Role> findByMenuId(@Param("menuId") Long menuId);
}
