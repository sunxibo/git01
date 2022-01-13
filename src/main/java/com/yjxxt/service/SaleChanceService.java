package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.mapper.SaleChanceMapper;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance) {
        // 1.参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(),saleChance.getLinkPhone());
        // 2.设置相关参数默认值
        // 未选择分配人
        saleChance.setState(0);
        saleChance.setDevResult(0);
        // 选择分配人
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());
        // 3.执行添加 判断结果
        AssertUtil.isTrue(insertSelective(saleChance) < 1, "营销机会数据添加失败！");
    }
    /**
     * 基本参数校验
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone)
    {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "请输入客户名！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "请输入联系人！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "请输入手机号！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance (SaleChance saleChance) {
        // 1.参数校验
        // 通过id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断是否为空
        AssertUtil.isTrue(null == temp, "待更新记录不存在！");
        // 校验基础参数
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
            // 2. 设置相关参数值
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(temp.getAssignMan())
                && StringUtils.isNotBlank(saleChance.getAssignMan())) {
            // 如果原始记录未分配，修改后改为已分配
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        } else if (StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())) {
            // 如果原始记录已分配，修改后改为未分配
            saleChance.setAssignMan("");
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setDevResult(0);
        }
            // 3.执行更新 判断结果
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "营销机会数据更新失败！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance (Integer[] ids) {
        // 判断要删除的id是否为空
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择需要删除的数据！");
        // 删除数据
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) < 0, "营销机会数据删除失败！");
    }

}
