package cn.odboy.infra.mybatis;

import cn.hutool.core.date.DateTime;
import cn.odboy.infra.context.SecurityUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mp自动填充字段
 *
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = DateTime.now();
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);
        String username = this.getUsername();
        this.strictInsertFill(metaObject, "createBy", String.class, username);
        this.strictInsertFill(metaObject, "updateBy", String.class, username);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = DateTime.now();
        this.strictUpdateFill(metaObject, "updateTime", Date.class, now);
        this.strictUpdateFill(metaObject, "updateBy", String.class, this.getUsername());
    }

    private String getUsername() {
        try {
            return SecurityUtil.getCurrentUsername();
        } catch (Exception e) {
            return "anonymous";
        }
    }
}
