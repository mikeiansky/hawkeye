package io.github.mikeiansky.hawkeye.sample.controller;

import io.github.mikeiansky.hawkeye.annotation.ExceptionListener;
import io.github.mikeiansky.hawkeye.annotation.TimeOutListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mike ian
 * @date 2024/11/29
 * @desc
 **/
@TimeOutListener
@ExceptionListener
@RequestMapping("hawkeye/sample")
@RestController
public class HawkeyeSampleController {

    @GetMapping("exception")
    public String testException() {
        throw new RuntimeException("sample exception");
    }

    @GetMapping("timeout")
    public String testTimeout() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "sample timeout";
    }

}
