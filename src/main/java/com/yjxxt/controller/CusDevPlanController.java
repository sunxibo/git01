package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

}
