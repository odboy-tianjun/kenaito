package cn.odboy;


import cn.odboy.util.GenCmdHelper;

import java.util.List;

/**
 * 代码生成入口
 *
 * @date 2024-04-27
 */
public class GenCode {
    public static void main(String[] args) {
        GenCmdHelper generator = new GenCmdHelper();
        generator.setDatabaseUrl(String.format("jdbc:mysql://%s:%s/%s", "192.168.235.101", 13306, "kenaito"));
        generator.setDatabaseUsername("root");
        generator.setDatabasePassword("root");
        genCareer(generator);
    }

    private static void genCareer(GenCmdHelper generator) {
        generator.gen("devops_", List.of(
                "devops_app",
                "devops_app_user",
                "devops_product_line"
        ));
    }
}
