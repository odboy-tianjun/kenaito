package cn.odboy.infra.websocket;

import java.util.LinkedList;
import java.util.List;

public class WebSocketClientPool {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final List<WebSocketServer> CACHE = new LinkedList<>();


    public static WebSocketServer getBySid(String sid) {
        return CACHE.parallelStream().filter(f -> f.getSid().equals(sid)).findFirst().orElse(null);
    }

    public static void add(WebSocketServer webSocketServer) {
        CACHE.add(webSocketServer);
    }

    public static void remove(WebSocketServer webSocketServer) {
        CACHE.remove(webSocketServer);
    }

    public static List<WebSocketServer> listAll() {
        return CACHE;
    }
}
