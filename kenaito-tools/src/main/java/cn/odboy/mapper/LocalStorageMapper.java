package cn.odboy.mapper;

import cn.odboy.domain.LocalStorage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface LocalStorageMapper extends BaseMapper<LocalStorage> {

    IPage<LocalStorage> findAll(@Param("criteria") LocalStorage.QueryArgs criteria, Page<Object> page);

    List<LocalStorage> findAll(@Param("criteria") LocalStorage.QueryArgs criteria);
}
