package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.bean.Module;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.mapper.ModuleMapper;
import com.yjxxt.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModuleService extends BaseService <Module,Integer>{
    @Autowired(required = false)
    private ModuleMapper moduleMapper;
    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }
    public List<TreeDto> findModulesByRoleId(Integer roleId) {
        List<TreeDto> treeDtos=moduleMapper.queryAllModules();
        // 根据角色id 查询角色拥有的菜单id List<Integer>
        List<Integer> roleHasMids=permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        for (TreeDto treeDto:treeDtos) {
            if (roleHasMids.contains(treeDto.getId())){
                treeDto.setChecked(true);
            }
            
        }
        return treeDtos;
    }
}
