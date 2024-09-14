package cn.odboy.mapper;

import cn.odboy.domain.MinioStorage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Minio存储 Mapper 接口
 * </p>
 *
 * @since 2024-05-30
 */
@Mapper
public interface MinioStorageMapper extends BaseMapper<MinioStorage> {
    IPage<MinioStorage.QueryPage> findAll(@Param("criteria") MinioStorage.QueryArgs criteria, Page<Object> page);

    List<MinioStorage.QueryPage> findAll(@Param("criteria") MinioStorage.QueryArgs criteria);
}
