package cn.odboy.modules.security.config;

import cn.odboy.infra.exception.BadConfigurationException;
import cn.odboy.modules.security.constant.LoginCode;
import cn.odboy.modules.security.constant.LoginCodeEnum;
import cn.odboy.util.StringUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Objects;

/**
 * 登录配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "login")
public class LoginProperties {
    public static final String CACHE_KEY = "user-login-cache:";

    /**
     * 账号单用户 登录
     */
    private boolean singleLogin = false;

    private LoginCode loginCode;

    public boolean isSingleLogin() {
        return singleLogin;
    }

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(loginCode)) {
            loginCode = new LoginCode();
            if (Objects.isNull(loginCode.getCodeType())) {
                loginCode.setCodeType(LoginCodeEnum.ARITHMETIC);
            }
        }
        return switchCaptcha(loginCode);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCode 验证码配置信息
     * @return /
     */
    private Captcha switchCaptcha(LoginCode loginCode) {
        Captcha captcha;
        switch (loginCode.getCodeType()) {
            case ARITHMETIC:
                // 算术类型 https://gitee.com/whvse/EasyCaptcha
                captcha = new FixedArithmeticCaptcha(loginCode.getWidth(), loginCode.getHeight());
                // 几位数运算，默认是两位
                captcha.setLen(loginCode.getLength());
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(loginCode.getWidth(), loginCode.getHeight());
                captcha.setLen(loginCode.getLength());
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                captcha.setLen(loginCode.getLength());
                break;
            case GIF:
                captcha = new GifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                captcha.setLen(loginCode.getLength());
                break;
            case SPEC:
                captcha = new SpecCaptcha(loginCode.getWidth(), loginCode.getHeight());
                captcha.setLen(loginCode.getLength());
                break;
            default:
                throw new BadConfigurationException("验证码配置信息错误！正确配置查看 LoginCodeEnum ");
        }
        if (StringUtil.isNotBlank(loginCode.getFontName())) {
            captcha.setFont(new Font(loginCode.getFontName(), Font.PLAIN, loginCode.getFontSize()));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}
