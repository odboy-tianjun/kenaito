package cn.odboy.modules.system.mapper;

import cn.odboy.modules.system.domain.Menu;
import cn.odboy.modules.system.domain.vo.MenuQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findAll(@Param("criteria") MenuQueryCriteria criteria);

    LinkedHashSet<Menu> findByRoleIdsAndTypeNot(@Param("roleIds") Set<Long> roleIds, @Param("type") Integer type);

    List<Menu> findByPidIsNullOrderByMenuSort();

    List<Menu> findByPidOrderByMenuSort(@Param("pid") Long pid);

    @Select("SELECT menu_id id FROM system_menu WHERE title = #{title}")
    Menu findByTitle(@Param("title") String title);

    @Select("SELECT menu_id id FROM system_menu WHERE name = #{name}")
    Menu findByComponentName(@Param("name") String name);

    @Select("SELECT count(*) FROM system_menu WHERE pid = #{pid}")
    int countByPid(@Param("pid") Long pid);

    @Select("update system_menu set sub_count = #{count} where menu_id = #{menuId} ")
    void updateSubCntById(@Param("count") int count, @Param("menuId") Long menuId);
}
