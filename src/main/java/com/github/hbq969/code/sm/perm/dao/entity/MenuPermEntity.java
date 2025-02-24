package com.github.hbq969.code.sm.perm.dao.entity;

import lombok.Data;

@Data
public class MenuPermEntity {
    private String app;
    private String menuName;
    private String apiKey;
    private String apiDesc;

    public String apiInfo() {
        return String.join(":", apiKey, apiDesc);
    }
}
