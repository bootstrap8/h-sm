package com.github.hbq969.code.sm.login.model;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.StringUtils;

@Data
public class PasswordModify {
    private String username;
    private String newPassword;
    private String oldPassword;

    public void hash(BCryptPasswordEncoder encoder) {
        if (StringUtils.isNotEmpty(newPassword)) {
            this.newPassword = encoder.encode(this.newPassword);
        }
    }
}
