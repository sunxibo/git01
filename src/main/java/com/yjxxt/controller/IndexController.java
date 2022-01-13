package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.bean.User;
import com.yjxxt.mapper.PermissionMapper;
import com.yjxxt.service.PermissionService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        // 通过工具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用对应Service层的方法，通过userId主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        // 将用户对象设置到request作用域中
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        List<String> permissions = permissionService.queryUserHasRolesHasPermissions(userId);
        request.getSession().setAttribute("permissions",permissions);
        request.setAttribute("user", user);
        return "main";
    }

}