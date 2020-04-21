package com.wemedia.aspect;

import com.wemedia.annotation.RedisCache;
import com.wemedia.service.RedisService;
import com.wemedia.util.AspectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP缓存
 * 切面=切入点+通知/增强
 */
@Aspect
@Component
public class RedisCacheAspect {
    private static Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

    private static final String CACHE_PREFIX = "news_";

    @Autowired
    private RedisService redisService;

    //切入点:方法上带有@Cacheable注解的方法
    @Pointcut(value = "@annotation(com.wemedia.annotation.RedisCache)")
    public void pointcut() {
    }
    /**
     * 这里通知选用环绕通知（前后都执行，出现异常后面不执行）,由于首先要判断缓存中是否存在,
     * 存在则返回,不存在则放过查询数据库,查询完数据库就要放入缓存中
     * ProceedingJoinPoint point:获取当前执行的方法
     */
    @Around("pointcut()")
    public Object handle(ProceedingJoinPoint point) throws Throwable {
        //获取切面当前执行的方法
        Method currentMethod = AspectUtil.getMethod(point);
        //通过反射如果该方法对象存在指定类型的注解，则返回该注解，否则返回null
        //@com.wemedia.RedisCache(key=XX, value=XX,flush=XX,expire=XX,unit=XX)
        RedisCache cache = currentMethod.getAnnotation(RedisCache.class);
        boolean flush = cache.flush();
        //@RedisCache(flush=true) 清缓存，重执行，用于编辑修改
        if (flush) {
            //获取以类路径为前缀的键  news_com_wemedia_service_impl_SysConfigServiceImpl_selectAll()
            String classPrefix = AspectUtil.getKeyOfClassPrefix(point, CACHE_PREFIX);
            logger.info("清空缓存 - {}*", classPrefix);
            redisService.delBatch(classPrefix);
            return point.proceed();
        }
        //获取切面缓存的key
        String key = AspectUtil.getKey(point, cache.key(), CACHE_PREFIX);
        //判断该key是否存在
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            try {
                logger.info("{}从缓存中获取数据", key);
                return redisService.get(key);
            } catch (Exception e) {
                logger.error("从缓存中获取数据失败！", e);
            }
        }
        //先执行业务，查询数据库
        Object result = point.proceed();
        logger.info("{}从数据库中获取数据", key);
        //将db中查询结果键值放入redis缓存中
        redisService.set(key, result, cache.expire(), cache.unit());
        return result;
    }
}
