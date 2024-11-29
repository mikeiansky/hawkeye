package io.github.mikeiansky.hawkeye.autoconfigure;

import io.github.mikeiansky.notice.aop.ExceptionNoticeAop;
import io.github.mikeiansky.notice.aop.TimeoutNoticeAop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author mike ian
 * @date 2024/8/7
 * @desc 线程池配置，并且开启异步工具
 **/
@Configuration
@EnableConfigurationProperties(NoticeExceptionProperties.class)
public class NoticeAutoConfiguration {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Bean
    public ExceptionNoticeAop exceptionNoticeAop(NoticeExceptionProperties noticeExceptionProperties) {
        return new ExceptionNoticeAop(
                applicationName,
                noticeExceptionProperties.getPackagePrefix(),
                noticeExceptionProperties.getNoticeWechatHook());
    }

    @Bean
    public TimeoutNoticeAop timeoutNoticeAop(NoticeExceptionProperties noticeExceptionProperties) {
        return new TimeoutNoticeAop(
                applicationName,
                noticeExceptionProperties.getNoticeWechatHook());
    }

}
