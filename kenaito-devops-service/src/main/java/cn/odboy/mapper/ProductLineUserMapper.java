package cn.odboy.mapper;

import cn.odboy.domain.ProductLineUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品线、成员关联 Mapper 接口
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@Mapper
public interface ProductLineUserMapper extends BaseMapper<ProductLineUser> {

}
