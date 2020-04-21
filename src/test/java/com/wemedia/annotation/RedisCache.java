package com.wemedia.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存自定义注解，对于查询使用缓存的方法加入该注解
 * @Target：描述注解的使用目标，作用于方法
 * @Retention：描述注解的生命周期，运行中可以处理
 * @Inherited：标记注解，使用@Inherited修饰的注解作用于一个类，则该注解将被用于该类的子类。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisCache {

    /**
     * redis缓存的value
     */
    String value() default "";

    /**
     * redis缓存的Key（默认类名-方法名-自定义key）
     */
    String key() default "";

    /**
     * 是否刷新缓存，默认false
     */
    boolean flush() default false;

    /**
     * 缓存失效时间，默认30
     */
    long expire() default 30L;

    /**
     * 缓存时间单位，默认天
     */
    TimeUnit unit() default TimeUnit.DAYS;
}
