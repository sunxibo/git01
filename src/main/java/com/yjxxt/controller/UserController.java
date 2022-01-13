package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.User;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo say(User user) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(user.getUserName(), user.getUserPwd());
        resultInfo.setResult(userModel);
        return resultInfo;
    }
    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo setting(User user) {
        ResultInfo resultInfo = new ResultInfo();

        userService.updateByPrimaryKeySelective(user);

        return resultInfo;
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPwd) {
        ResultInfo resultInfo = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            // 调用Service层的密码修改方法
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPwd);
        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/user/password";
    }
    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest request){
        //获取ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user",user);
        return "user/setting";
    }

    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String,Object>> findSales(){
        List<Map<String,Object>> list = userService.queryAllSales();
        return list;
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryUserByParams(userQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }

    @RequestMapping("addOrUpdatePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model) {
            // 如果id不为空，表示是修改操作，修改操作需要查询被修改的数据
        if (null != id) {
            // 通过主键查询营销机会数据
            // 将数据存到作用域中
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteBatch(ids);
        return success("用户记录删除成功");
    }



}
