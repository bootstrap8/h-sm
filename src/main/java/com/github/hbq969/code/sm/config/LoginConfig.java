package com.github.hbq969.code.sm.config;

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
     * 初始化脚本文件，如果是oracle gbk格式的请选择 sm-initial-gbk.sql
     */
    private String initScriptFile = "sm-initial.sql";

    /**
     * 初始化脚本文件编码格式
     */
    private String initScriptFileCharset = "UTF-8";
}
