package com.github.hbq969.code.sm.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.hbq969.code.sm.login.model.SMInfo;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spring.mvc.interceptors.login")
@Data
public class LoginConfig {
    /**
     * 是否启用登录功能
     */
    private boolean enabled;

    /**
     * cookie最大存活时间（秒）
     */
    private long cookieMaxAgeSec = 1800;

    /**
     * 无需校验会话的url，无需包含<code>server.servlet.context-path</code>
     */
    private List<String> excludeUrls = null;

    /**
     * 需要校验会话的url，无需包含<code>server.servlet.context-path</code>，默认除<code>excludeUrls</code>以外的所有
     */
    private List<String> includeUrls = Lists.newArrayList("/**");

    /**
     * 数据库dialect，默认mysql，可支持oracle
     */
    private String dialect = "mysql";

    /**
     * 会话id名称，需要和<code>server.servlet.session.cookie.name</code>保持一致
     */
    private String sessionKey = "JSESSIONID";

    /**
     * h-sm初始化脚本配置
     */
    private ServiceInitialScript smInitialScript = new ServiceInitialScript();

    /**
     * 服务初始化脚本配置
     */
    private ServiceInitialScript serviceInitialScript = new ServiceInitialScript();

    @JsonIgnore
    private SMInfo smInfo;

    @Data
    public static class ServiceInitialScript {
        /**
         * 脚本文件前缀
         */
        private String prefix = "initial";
        /**
         * 默认语言
         */
        private String language = "zh-CN";

        /**
         * 文件编码
         */
        private String charset = "utf-8";
    }
}
