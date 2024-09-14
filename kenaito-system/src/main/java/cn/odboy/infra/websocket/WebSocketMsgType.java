package cn.odboy.infra.websocket;

import java.util.LinkedList;
import java.util.List;

/**
 * Socket消息类型
 *
 * @date 2024-06-07
 */
public class WebSocketMsgType {
    public static List<String> supportMsgType = new LinkedList<>() {{
        /**
         * 单聊
         */
        add("sc");
        /**
         * 群聊
         */
        add("gc");
        /**
         * 系统公告
         */
        add("sn");
    }};
    public final static String SINGLE_CHAT = "sc";
    public final static String GROUP_CHAT = "gc";
    public final static String SYSTEM_NOTICE = "sn";
}
