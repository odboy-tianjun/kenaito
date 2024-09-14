package cn.odboy.constant;

/**
 * 常用静态常量
 */
public class SystemConst {
    /**
     * 管理员编码
     */
    public static final String ADMIN = "admin";
    /**
     * 集合初始化大小
     */
    public static final int COLL_INIT_SIZE = 2;
    /**
     * 分割符, 小数点
     */
    public static final String SEP_DOT = ".";
    /**
     * 基于国际会计标准初始化
     */
    public static final String BIG_DECIMAL_INIT = "0.0000";
    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";
    /**
     * win 系统
     */
    public static final String WIN = "win";
    /**
     * mac 系统
     */
    public static final String MAC = "mac";
    /**
     * 分割符, 逗号
     */
    public static final String SEP_COMMA = ",";
    /**
     * 系统内置名称
     */
    public static final String APP_NAME = "kenaito";

    /**
     * 常用接口
     */
    public static class Url {
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

    /**
     * 默认批量插入更新大小
     */
    public static final int BATCH_UPDATE_SIZE = 500;

    public static final String TIP_ILLEGAL_OPERATION = "非法操作, 请刷新后再试";
}
