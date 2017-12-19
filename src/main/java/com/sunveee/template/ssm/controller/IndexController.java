package com.sunveee.template.ssm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sunveee.template.ssm.util.LogUtil;

@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/")
    public String toIndex() {
        LogUtil.debug(logger, "访问首页Controller");

        return "index";
    }

}