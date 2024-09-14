package cn.odboy;

import cn.odboy.infra.context.EasyBootApplication;
import cn.odboy.infra.context.SpringContextHolder;
import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class AppRun extends EasyBootApplication {

    public static void main(String[] args) {
        // fastjson 升级到 1.2.83 后需要指定序列化白名单
        ParserConfig.getGlobalInstance().addAccept("cn.odboy.domain");
        ParserConfig.getGlobalInstance().addAccept("cn.odboy.infra.websocket");
        ParserConfig.getGlobalInstance().addAccept("cn.odboy.modules.quartz.domain");
        ParserConfig.getGlobalInstance().addAccept("cn.odboy.modules.security.domain");
        ParserConfig.getGlobalInstance().addAccept("cn.odboy.modules.system.domain");
        SpringApplication springApplication = new SpringApplication(AppRun.class);
        initd(springApplication.run(args));

    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
