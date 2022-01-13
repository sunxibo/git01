package com.yjxxt.interceptors;

import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (null == userId || null == userService.selectByPrimaryKey(userId)) {
        // 抛出未登录异常
            throw new NoLoginException();
        }

        return true;
    }
}
