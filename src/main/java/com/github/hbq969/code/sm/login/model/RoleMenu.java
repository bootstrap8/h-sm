package com.github.hbq969.code.sm.login.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class RoleMenu {
    private List<Map> all;
    private Set<String> conf;
}
