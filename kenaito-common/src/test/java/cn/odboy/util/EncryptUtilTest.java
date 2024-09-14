package cn.odboy.util;

import org.junit.jupiter.api.Test;

import static cn.odboy.util.EncryptUtil.desDecrypt;
import static cn.odboy.util.EncryptUtil.desEncrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptUtilTest {

    /**
     * 对称加密
     */
    @Test
    public void testDesEncrypt() {
        try {
            assertEquals("7772841DC6099402", desEncrypt("123456"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对称解密
     */
    @Test
    public void testDesDecrypt() {
        try {
            assertEquals("123456", desDecrypt("7772841DC6099402"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
