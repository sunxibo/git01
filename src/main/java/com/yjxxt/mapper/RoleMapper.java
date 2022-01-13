package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    @MapKey("")
    List<Map<String,Object>> queryAllRoles(Integer userId);

    Role queryRoleByRoleName(String roleName);
}