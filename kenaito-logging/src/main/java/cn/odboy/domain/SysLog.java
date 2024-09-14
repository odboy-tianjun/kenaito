package cn.odboy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;


/**
 * 系统日志
 *
 * @author odboy
 * @date 2024-09-06
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("system_log")
public class SysLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 地址
     */
    private String address;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 异常详细
     */
    @JSONField(serialize = false)
    private String exceptionDetail;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }

    /**
     * 日志查询类
     */
    @Data
    public static class QueryArgs {
        private String blurry;
        private String username;
        private String logType;
        private List<Timestamp> createTime;
    }
}
