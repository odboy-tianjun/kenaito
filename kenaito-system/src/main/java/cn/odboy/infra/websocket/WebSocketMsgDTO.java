package cn.odboy.infra.websocket;

import lombok.Data;

import java.util.List;

@Data
public class WebSocketMsgDTO {
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 内容: 普通字符串 / json字符串
     */
    private String content;
    /**
     * 是否发送给所有人
     */
    private boolean toAllUser = false;
    /**
     * 来源是谁
     */
    private String fromSid;
    /**
     * 具体发送给谁
     */
    private List<String> userIds;
    /**
     * 发送给哪个群组
     */
    private String groupId;
    /**
     * 异常信息
     */
    private String errMsg;
}
