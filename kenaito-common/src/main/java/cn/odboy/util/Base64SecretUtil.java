package cn.odboy.util;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.IdUtil;

import java.nio.charset.StandardCharsets;

/**
 * 加密令牌生成工具
 *
 * @date 2022-11-16
 */
public class Base64SecretUtil {
    private Base64SecretUtil() {
    }

    public static String build() {
        // 128位uuid
        return Base64Encoder.encode(getLongUuid(4).getBytes(StandardCharsets.UTF_8));
    }

    public static String getLongUuid(int bit) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bit; i++) {
            result.append(IdUtil.simpleUUID());
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(build());
    }
}
