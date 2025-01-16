package com.github.hbq969.code.sm.login.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ResetPassword {
    private String username;
    private String password1;
    private String password2;

    public boolean same() {
        return StringUtils.equals(password1, password2);
    }
}
