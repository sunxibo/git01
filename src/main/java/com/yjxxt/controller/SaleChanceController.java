package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.service.SaleChanceService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import com.yjxxt.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;
    @Autowired
    private UserService userService;

/*    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        return saleChanceService.querySaleChanceByParams(query);
    }*/
    @RequestMapping("index")
    public String index () {
        return "saleChance/sale_chance";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query, Integer flag, HttpServletRequest request) {
        if (null != flag && flag == 1) {
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(query);
    }

    /**
     * 添加营销机会数据
     * @param request
     * @param saleChance
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest request, SaleChance saleChance){
        // 获取用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 获取用户的真实姓名
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        // 设置营销机会的创建人
        saleChance.setCreateMan(trueName);
        // 添加营销机会的数据
        saleChanceService.saveSaleChance(saleChance);
        return success("添加成功！");
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(HttpServletRequest request, SaleChance saleChance){
            // 更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("更新成功！");
    }

    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model) {
        // 如果id不为空，表示是修改操作，修改操作需要查询被修改的数据
        if (null != id) {
            // 通过主键查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            // 将数据存到作用域中
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 删除营销机会数据
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance (Integer[] ids) {
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("联系人删除成功！");
    }
}
