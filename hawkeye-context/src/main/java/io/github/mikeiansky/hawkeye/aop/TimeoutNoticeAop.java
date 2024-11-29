package io.github.mikeiansky.hawkeye.aop;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import io.github.mikeiansky.hawkeye.annotation.TimeOutListener;
import io.github.mikeiansky.hawkeye.context.NoticeThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author mike ian
 * @date 2024/10/11
 * @desc
 **/
@Aspect
@Slf4j
public class TimeoutNoticeAop {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private final String appName;

    private final String noticeWechatHook;

    private final ThreadPoolExecutor threadPoolExecutor;

    public TimeoutNoticeAop(String appName, String noticeWechatHook) {
        this.appName = appName;
        this.noticeWechatHook = noticeWechatHook;
        threadPoolExecutor = NoticeThreadPool.getThreadPool();
    }

    @Before(value = "@within(listener) || @annotation(listener)")
    public void timeoutListenBefore(JoinPoint point, TimeOutListener listener) {
        startTime.set(System.currentTimeMillis());
    }

    @After(value = "@within(listener) || @annotation(listener)")
    public void timeoutListenAfter(JoinPoint joinPoint, TimeOutListener listener) {
        Long start = startTime.get();
        if (start == null) {
            return;
        }
        if (listener == null) {
            listener = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), TimeOutListener.class);
        }
        long duration = System.currentTimeMillis() - start;
        if (duration <= listener.time()) {
            return;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 请求地址
        String queryString = attributes.getRequest().getQueryString();
        String requestURL = attributes.getRequest().getRequestURL().toString() + (queryString != null ? queryString : "");
        // 类名
        String className = joinPoint.getSignature().getDeclaringTypeName();
        // 方法名
        String methodName = joinPoint.getSignature().getName();
        // 参数信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> paramsInfo = new HashMap<>();
        if (parameterNames != null && parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length; i++) {
                paramsInfo.put(parameterNames[i], args[i]);
            }
        }


        StringBuilder content = new StringBuilder();
        content.append(">服务名: <font color=\"info\"> ").append(appName).append("</font>\n");
        content.append(">请求地址: <font color=\"info\"> ").append(requestURL).append("</font>\n");
        content.append(">类名: <font color=\"info\"> ").append(className).append("</font>\n");
        content.append(">方法名: <font color=\"info\"> ").append(methodName).append("</font>\n");
        content.append(">请求参数: <font color=\"info\"> ").append(paramsInfo).append("</font>\n");
        content.append(">耗时: <font color=\"warning\"> ").append(duration).append("毫秒</font>\n");
        content.append(">产生时间: <font color=\"info\"> ").append(DateUtil.now()).append("</font>\n");
        Map<String, Object> data = new HashMap<>();
        data.put("msgtype", "markdown");
        data.put("markdown", Map.of("content", content.toString()));

        threadPoolExecutor.submit(() -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> requestEntity = new HttpEntity<>(JSONObject.toJSONString(data), headers);
            restTemplate.postForObject(noticeWechatHook, requestEntity, String.class);
        });
    }

}
