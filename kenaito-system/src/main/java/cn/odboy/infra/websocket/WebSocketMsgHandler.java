package cn.odboy.infra.websocket;

import javax.websocket.Session;

public interface WebSocketMsgHandler {
    void onMessage(String sid, Session session, WebSocketMsgDTO socketMsg);
}
