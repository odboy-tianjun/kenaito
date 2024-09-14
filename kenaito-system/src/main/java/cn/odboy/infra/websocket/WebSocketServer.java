package cn.odboy.infra.websocket;

import cn.hutool.core.util.StrUtil;
import cn.odboy.infra.context.SpringContextHolder;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;

@Data
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket/{sid}", encoders = {WebSocketServerEncoder.class})
public class WebSocketServer {
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        // 如果存在就先删除一个，防止重复推送消息
        WebSocketServer cacheClient = WebSocketClientPool.getBySid(sid);
        if (cacheClient != null) {
            if (cacheClient.session.isOpen()) {
                try {
                    cacheClient.session.close();
                } catch (IOException ignore) {
                    // 关闭失败忽略
                }
            }
            WebSocketClientPool.remove(cacheClient);
        }
        WebSocketClientPool.add(this);
        this.sid = sid;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WebSocketClientPool.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来" + sid + "的信息:" + message);
        WebSocketMsgDTO socketMsg = JSON.parseObject(message, WebSocketMsgDTO.class);
        if (StrUtil.isBlank(socketMsg.getMsgType())) {
            log.warn("不包含消息类型字段的消息, 直接丢弃");
            return;
        }
        if (!WebSocketMsgType.supportMsgType.contains(socketMsg.getMsgType())) {
            log.warn("不支持的消息类型, 直接丢弃");
            return;
        }
        WebSocketMsgHandler msgHandler = SpringContextHolder.getBean("WsMsgHandler#" + socketMsg.getMsgType());
        msgHandler.onMessage(sid, session, socketMsg);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

//    /**
//     * 实现服务器主动推送
//     */
//    public void sendMessage(String message) throws Exception {
//        this.session.getBasicRemote().sendText(message);
//    }

    /**
     * 实现服务器主动推送
     */
    public void sendObject(Object message) throws Exception {
        this.session.getBasicRemote().sendObject(message);
    }


//    /**
//     * 群发自定义消息
//     */
//    public void sendInfo(WebSocketMsgDTO webSocketMsg, @PathParam("sid") String sid) throws IOException {
//        String message = JSON.toJSONString(webSocketMsg);
//        log.info("推送消息到" + sid + "，推送内容:" + message);
//        for (WebSocketServer item : clientPool.listAll()) {
//            try {
//                // 这里可以设定只推送给这个sid的，为null则全部推送
//                if (sid == null) {
//                    item.sendMessage("Hello WebSocket");
//                } else if (item.sid.equals(sid)) {
//                    item.sendMessage("Hello WebSocket");
//                }
//            } catch (IOException e) {
//                log.error("推送消息到客户端失败", e);
//            }
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }
}
