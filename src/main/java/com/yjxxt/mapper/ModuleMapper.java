package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Module;
import com.yjxxt.dto.TreeDto;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    List<TreeDto> queryAllModules();
}