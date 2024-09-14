package cn.odboy.infra.websocket.impl;

import cn.hutool.core.util.StrUtil;
import cn.odboy.infra.websocket.WebSocketMsgDTO;
import cn.odboy.infra.websocket.WebSocketMsgHandler;
import cn.odboy.infra.websocket.WebSocketMsgType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

@Slf4j
@Component("WsMsgHandler#" + WebSocketMsgType.GROUP_CHAT)
public class GroupChatMsgHandlerImpl implements WebSocketMsgHandler {
    @Override
    public void onMessage(String sid, Session session, WebSocketMsgDTO socketMsg) {
        String content = socketMsg.getContent();
        String groupId = socketMsg.getGroupId();
        if (StrUtil.isBlank(groupId)) {
            try {
                socketMsg.setErrMsg("接收群组的id不能为空");
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
        // TODO 根据群组获取用户id
    }
}
