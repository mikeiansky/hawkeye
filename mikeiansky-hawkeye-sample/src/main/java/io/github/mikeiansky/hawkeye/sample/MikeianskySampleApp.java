package io.github.mikeiansky.hawkeye.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mike ian
 * @date 2024/11/29
 * @desc
 **/
@EnableAspectJAutoProxy
@SpringBootApplication
public class MikeianskySampleApp {

    public static void main(String[] args) {
        SpringApplication.run(MikeianskySampleApp.class, args);
    }

}
