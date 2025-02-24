package com.github.hbq969.code.sm.perm.advice;

import com.github.hbq969.code.common.spring.advice.handler.RestfulHandler;
import com.github.hbq969.code.sm.perm.api.SMRequiresPermissions;
import com.github.hbq969.code.sm.perm.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

@Slf4j
public class PermHandler implements RestfulHandler {

    @Autowired
    private CacheService cacheService;

    @Override
    public void before(ProceedingJoinPoint point) {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method m = ms.getMethod();
        SMRequiresPermissions srp = AnnotationUtils.findAnnotation(m, SMRequiresPermissions.class);
        if (srp == null || srp.ignore()) {
            return;
        }
        String menu = srp.menu();
        String apiKey = srp.apiKey();
        cacheService.permCheck(menu, apiKey);
    }

    @Override
    public void after(ProceedingJoinPoint point, Object result) {

    }

    @Override
    public void exception(ProceedingJoinPoint point, Throwable e) {

    }

    @Override
    public int order() {
        return 100;
    }
}
