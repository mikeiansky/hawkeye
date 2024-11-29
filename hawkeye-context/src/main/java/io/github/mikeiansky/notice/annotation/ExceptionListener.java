package io.github.mikeiansky.notice.annotation;

import java.lang.annotation.*;

/**
 * @author mike ian
 * @date 2024/10/11
 * @desc
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ExceptionListener {
}
