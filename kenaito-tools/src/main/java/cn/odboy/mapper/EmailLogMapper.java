package cn.odboy.mapper;

import cn.odboy.domain.EmailLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * TOOL邮件发送记录 Mapper 接口
 * </p>
 *
 * @since 2024-06-05
 */
@Mapper
public interface EmailLogMapper extends BaseMapper<EmailLog> {

}
