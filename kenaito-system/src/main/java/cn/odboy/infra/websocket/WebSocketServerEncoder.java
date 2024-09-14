package cn.odboy.infra.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * WebSocket编码器，用于发送请求的时候可以发送Object对象，实则是json数据
 *
 * @date 2024-06-07
 */
@Slf4j
public class WebSocketServerEncoder implements Encoder.Text<WebSocketMsgDTO> {

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig arg0) {
    }

    @Override
    public String encode(WebSocketMsgDTO socketMsg) throws EncodeException {
//        if (obj instanceof String) {
//            return (String) obj;
//        }
//        if (obj instanceof WebSocketMsgDTO) {
//            try {
//                return JSON.toJSONString(obj);
//            } catch (Exception e) {
//                log.warn("Fastjson编码异常", e);
//                return "";
//            }
//        }
//        return obj.toString();
        // 为了标准化不适配String
        try {
            return JSON.toJSONString(socketMsg);
        } catch (Exception e) {
            log.warn("Fastjson编码异常", e);
            return "";
        }
    }
}
