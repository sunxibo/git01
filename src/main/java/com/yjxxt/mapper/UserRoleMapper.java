package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    int countUserRoleByUserId(Integer userId);
    int deleteUserRoleByUserId(Integer userId);
}