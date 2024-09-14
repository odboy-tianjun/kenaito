package cn.odboy.config;

import cn.odboy.constant.SystemConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Slf4j
@Configuration
public class MultipartConfig {

    /**
     * 文件上传临时路径
     */
    @Bean
    public MultipartConfigElement createMultipartConfig() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.home") + "/." + SystemConst.APP_NAME + "/file/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            if (!tmpFile.mkdirs()) {
                log.error("创建目录失败");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
