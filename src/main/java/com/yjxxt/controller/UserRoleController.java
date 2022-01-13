package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("userRole")
public class UserRoleController extends BaseController {
    @Autowired
    private UserRoleService userRoleService;
}
