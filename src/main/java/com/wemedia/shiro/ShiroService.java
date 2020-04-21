package com.wemedia.shiro;

import com.wemedia.component.SpringContextHolder;
import com.wemedia.model.Permission;
import com.wemedia.service.PermissionService;
import com.wemedia.util.CoreConst;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//shiro就是自定义实现了spring mvc的filter，springmvc用addInterceptors直接拦截
@Service
public class ShiroService {
    @Autowired
    private PermissionService permissionService;
    /**
     * 初始化权限，配置shiro过滤器链，全部通过才算验证通过
     * 拦截器说明：
     * anon:不需要登录就能访问，一般用于静态资源
     * authc:需要登录认证才能访问的资源
     * logout:用户登出拦截器,主要属性:redirectURL退出登录后重定向的地址
     * perms:权限授权拦截器，验证用户是否拥有所有权限；属性和roles一样；示例“/user/**=perms["user:create"]”
     * user:用户拦截器，用户已经身份验证 / 记住我登录的都可；
     */
    public Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/blog/**", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/libs/**","anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/verificationCode", "anon");
        //其余url都是需要用户登录
        filterChainDefinitionMap.put("/**", "user");
        return filterChainDefinitionMap;
    }
    //权限在数据库增删改后都要把权限过滤链变化实时更新到服务器中，相当于多了url，重走过滤链
    /**
     * Shiro动态加载权限：重新加载权限，改变数据库动态加载
     * Service层方法，自动注入静态无效，使用SpringContextHolder注入:
     * 那些没有归入spring框架管理的类却要调用spring容器中的bean提供的工具类
     *
     * AbstractShiroFilter:shiro集成spring的ShiroFilterFactoryBean返回给spring context中的bean
     */
    public void updatePermission() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
        synchronized (shiroFilterFactoryBean) {

            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
                        .getObject();
            } catch (Exception e) {
                throw new RuntimeException(
                        "get ShiroFilter from shiroFilterFactoryBean error!");
            }
            /**
             * 使用ShiroFactoryBean创建shiroFilter时，默认使用PathMatchingFilterChainResolver进行
             * 解析，而它默认是根据当前请求的URL获取相应拦截器链，并与URL进行匹配。
             * 默认使用DefaultFilterChainManager进行拦截器链的管理
             */
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            // 设置新的权限控制
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean
                    .getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
        }
    }
}
