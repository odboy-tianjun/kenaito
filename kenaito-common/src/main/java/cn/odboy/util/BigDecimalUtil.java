package cn.odboy.util;

import cn.hutool.core.convert.Convert;
import cn.odboy.infra.exception.BadRequestException;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 大数格式化工具
 *
 * @date 2022-11-09
 */
public class BigDecimalUtil {
    private static final String UNIT_STRING_WAN = "万";
    private static final String UNIT_STRING_YI = "亿";
    /**
     * 加入千分位, 保留两位小数, 自动补零, #-是否是数字, 不存在显示为空；0-是否是数字, 不存在补零
     */
    private static final String DECIMAL_PATTERN_AUTO_ZERO = "###,##0.00 元";

    /**
     * 装换为万或亿为结尾
     *
     * @param amount 结果
     * @param scale  保留几位小数
     * @return /
     */
    public static String format(@NotNull BigDecimal amount, int scale) {
        if (amount == null) {
            return "0";
        }
        if (amount.compareTo(new BigDecimal(10000)) < 0) {
            // 如果小于1万
            return amount.stripTrailingZeros().toPlainString();
        }
        if (amount.compareTo(new BigDecimal(10000000)) < 0) {
            // 如果大于1万小于1亿
            return amount.divide(new BigDecimal(10000), scale, RoundingMode.DOWN).stripTrailingZeros().toPlainString() + UNIT_STRING_WAN;
        }
        return amount.divide(new BigDecimal(100000000), scale, RoundingMode.DOWN).stripTrailingZeros().toPlainString() + UNIT_STRING_YI;
    }

    /**
     * 格式化为百分比
     *
     * @param amount /
     * @return /
     */
    public static String toPercent(@NotNull BigDecimal amount) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        return percent.format(amount.doubleValue());
    }

    /**
     * 当前值是否等于0
     *
     * @param amount /
     * @return /
     */
    public static boolean isZero(@NotNull BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * a > b 为 true
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static boolean bt(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return a.compareTo(b) > 0;
    }

    /**
     * a < b 为 true
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static boolean lt(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return a.compareTo(b) < 0;
    }

    /**
     * a == b 为 true
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static boolean eq(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return a.compareTo(b) == 0;
    }

    /**
     * a + b
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static BigDecimal add(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return a.add(b);
    }

    /**
     * a - b
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static BigDecimal sub(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return a.subtract(b);
    }

    /**
     * a * b
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static BigDecimal mul(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return a.multiply(b);
    }

    /**
     * a / b <br>
     * 接近负无穷大的舍入模式.
     * 如果 BigDecimal 为正, 则舍入行为与 ROUND_DOWN 相同;
     * 如果为负, 则舍入行为与 ROUND_UP 相同.
     * 注意, 此舍入模式始终不会增加计算值.
     *
     * @param a /
     * @param b /
     * @return /
     */
    public static BigDecimal div(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        if (isZero(b)) {
            throw new BadRequestException("被除数不能为0");
        }
        return a.divide(b, 6, RoundingMode.FLOOR);
    }

    /**
     * 绝对值
     *
     * @param a /
     * @return /
     */
    public static BigDecimal abs(@NotNull BigDecimal a) {
        return a.abs();
    }

    /**
     * 金额加千分位格式化
     *
     * @param bigDecimal 需要格式化的数据
     * @return 格式化好的结果
     */
    public static String format(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_PATTERN_AUTO_ZERO);
        return decimalFormat.format(bigDecimal);
    }

    /**
     * 四舍五入, 保留2为小数
     *
     * @param num 需要格式化的数据
     * @return 保留2为小数的结果
     */
    public static BigDecimal halfUp(BigDecimal num) {
        return num.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 金额转成中文繁体,依赖hutool包
     *
     * @param num 需要格式化的数据
     * @return 转换后的中文繁体
     */
    public static String convertChinese(BigDecimal num) {
        return Convert.digitToChinese(num);
    }

    public static void main(String[] args) {
        System.out.println(format(BigDecimal.valueOf(1888L), 2));  // 1888
        System.out.println(format(BigDecimal.valueOf(18888L), 2));  // 1.88万
        System.out.println(format(BigDecimal.valueOf(188888888L), 2));  // 1.88亿
        System.out.println(format(BigDecimal.valueOf(188888888L)));  // 188,888,888.00 元
        System.out.println(halfUp(new BigDecimal("1.225")));  // 1.23
        System.out.println(convertChinese(BigDecimal.valueOf(18888L)));  // 壹万捌仟捌佰捌拾捌元整
    }
}
