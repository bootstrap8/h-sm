package com.github.hbq969.code.sm.login.dao.entity;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.FormatTime;
import com.github.hbq969.code.dict.service.api.DictAware;
import com.github.hbq969.code.dict.service.api.DictModel;
import com.github.hbq969.code.sm.login.model.UserInfo;
import com.github.hbq969.code.sm.login.session.UserContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : hbq969@gmail.com
 * @description : 用户信息
 * @createTime : 2025/1/2 11:13
 */
@Data
public class UserEntity implements DictModel, DictAware {
    private String app;
    private String username;
    private String password;
    private String roleName;
    private Long createdAt;
    private String fmtCreatedAt;
    private Long updatedAt;
    private String fmtUpdatedAt;

    @Override
    public void convertDict(SpringContext context) {
        DictAware.super.convertDict(context);
        if (createdAt != null) {
            this.fmtCreatedAt = FormatTime.YYYYMMDDHHMISS.withSecs(createdAt.longValue());
        }
        if (updatedAt != null) {
            this.fmtUpdatedAt = FormatTime.YYYYMMDDHHMISS.withSecs(updatedAt.longValue());
        }
    }

    public void initial() {
        this.createdAt = FormatTime.nowSecs();
    }

    public void update() {
        this.updatedAt = FormatTime.nowSecs();
    }

    public void hash(BCryptPasswordEncoder encoder) {
        if (StringUtils.isNotEmpty(password)) {
            this.password = encoder.encode(this.password);
        }
    }

    public void permit() {
        UserInfo ui = UserContext.get();
        if (null == ui || !StringUtils.equals("ADMIN", ui.getRoleName())) {
            throw new UnsupportedOperationException("此操作只允许ADMIN角色");
        }
    }
}
