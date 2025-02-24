package com.github.hbq969.code.sm.perm.api;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SMRequiresPermissions {
    /**
     * 菜单名称，h_menus.name
     *
     * @return
     */
    String menu();

    /**
     * 接口标识，如果不填默认取方法名称
     *
     * @return
     */
    String apiKey();

    /**
     * 接口描述
     *
     * @return
     */
    String apiDesc();

    /**
     * 是否忽略权限校验
     *
     * @return
     */
    boolean ignore() default false;
}
