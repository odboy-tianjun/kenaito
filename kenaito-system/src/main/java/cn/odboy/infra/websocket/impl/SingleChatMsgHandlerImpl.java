package cn.odboy.infra.websocket.impl;

import cn.hutool.core.util.StrUtil;
import cn.odboy.infra.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.List;

@Slf4j
@Component("WsMsgHandler#" + WebSocketMsgType.SINGLE_CHAT)
public class SingleChatMsgHandlerImpl implements WebSocketMsgHandler {
    @Override
    public void onMessage(String sid, Session session, WebSocketMsgDTO socketMsg) {
        String content = socketMsg.getContent();
        List<String> userIds = socketMsg.getUserIds();
        if (userIds == null || userIds.isEmpty()) {
            try {
                socketMsg.setErrMsg("接收者的用户userid列表不能为空");
                session.getBasicRemote().sendObject(socketMsg);
            } catch (Exception e) {
                log.error("推送消息到客户端失败", e);
            }
            return;
        }
        if (StrUtil.isBlank(content)) {
            try {
                socketMsg.setErrMsg("发送的消息内容不能为空");
                session.getBasicRemote().sendObject(socketMsg);
            } catch (Exception e) {
                log.error("推送消息到客户端失败", e);
            }
            return;
        }
        socketMsg.setFromSid(sid);
        for (String userId : userIds) {
            WebSocketServer client = WebSocketClientPool.getBySid(userId);
            if (client != null) {
                try {
                    client.sendObject(socketMsg);
                } catch (Exception e) {
                    log.error("推送消息到客户端失败", e);
                }
            }
        }
    }
}
