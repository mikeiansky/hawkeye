package io.github.mikeiansky.hawkeye.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mike ian
 * @date 2024/8/13
 * @desc 线程池配置参数
 **/
@Data
@ConfigurationProperties(prefix = "io.github.mikeiansky.hawkeye")
public class NoticeExceptionProperties {

    private String packagePrefix;

    private String noticeWechatHook;

}
