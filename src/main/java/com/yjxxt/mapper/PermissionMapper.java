package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    int countPermissionByRoleId(Integer roleId);


    int deletePermissionsByRoleId(Integer roleId);


    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);

    List<String> queryUserHasRolesHasPermissions(Integer userId);
}
