package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.User;
import com.yjxxt.bean.UserRole;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.mapper.UserRoleMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserService extends BaseService<User,Integer> {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;


    public UserModel userLogin(String userName, String userPwd) {
        checkLoginParams(userName, userPwd);
        User user = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null == user, "用户不存在或已注销！");
        checkLoginPwd(userPwd, user.getUserPwd());
        return buildUserInfo(user);
    }
    public void updateUserPassword (Integer userId, String oldPassword, String newPassword, String confirmPassword ) {
        // 通过userId获取用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 1. 参数校验
        checkPasswordParams(user, oldPassword, newPassword, confirmPassword);
        // 2. 设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        // 3. 执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户密码更新失败！");
    }
    /**
     * 验证用户密码修改参数
     * 用户ID：userId 非空 用户对象必须存在
     * 原始密码：oldPassword 非空 与数据库中密文密码保持一致
     * 新密码：newPassword 非空 与原始密码不能相同
     * 确认密码：confirmPassword 非空 与新密码保持一致
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user, String oldPassword,String newPassword, String confirmPassword) {
        // user对象 非空验证
        AssertUtil.isTrue(null == user, "用户未登录或不存在！");
        // 原始密码 非空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "请输入原始密码！");
        // 原始密码要与数据库中的密文密码保持一致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确！");
        // 新密码 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "请输入新密码！");
        // 新密码与原始密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword), "新密码不能与原始密码相同！");
        // 确认密码 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "请输入确认密码！");
        // 新密码要与确认密码保持一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码与确认密码不一致！");
    }


    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        // 设置用户信息
        userModel.setUserId(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }
    private void checkLoginPwd(String userPwd, String upwd) {
        // 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作比较
        userPwd = Md5Util.encode(userPwd);
        // 比较密码
        AssertUtil.isTrue(!userPwd.equals(upwd), "用户密码不正确！");
    }
    private void checkLoginParams(String userName, String userPwd) {
        //用户非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        map.put("code",0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
    // 1. 参数校验
        checkParams(user);
    // 2. 设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
    // 3. 执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertSelective(user) == null, "用户添加失败！");
        relationUserRole(user.getId(), user.getRoleIds());
    }

    private void relationUserRole(Integer useId, String roleIds) {
        int count = userRoleMapper.countUserRoleByUserId(useId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(useId) != count, "用户角色分配失败!");
        }
        if (StringUtils.isNotBlank(roleIds)) {
            //重新添加新的角色
            List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(useId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) <
                    userRoles.size(), "用户角色分配失败!");
        }
    }

    /**
     * 参数校验
     *
     */
    private void checkParams(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        // 验证用户名是否存在
        User temp = userMapper.queryUserByUserName(user.getUserName());
        if (user.getId()==null){
            AssertUtil.isTrue(null != temp, "该用户已存在！");
        }else {
            AssertUtil.isTrue((null != temp)&&!(temp.getId().equals(user.getId())), "该用户已存在！");
        }
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "手机号码格式不正确！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 1. 参数校验
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 验证参数
        checkParams(user);
        // 2. 设置默认参数
        temp.setUpdateDate(new Date());
        // 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败！");
        Integer userId = userMapper.queryUserByUserName(user.getUserName()).getId();
        relationUserRole(userId, user.getRoleIds());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids) {
        AssertUtil.isTrue(null==ids || ids.length == 0,"请选择待删除的用户记录!");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length,"用户记录删除失败!");
    }

}
