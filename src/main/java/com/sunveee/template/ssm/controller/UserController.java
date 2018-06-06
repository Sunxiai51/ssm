package com.sunveee.template.ssm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sunveee.template.ssm.entity.mbg.User;
import com.sunveee.template.ssm.service.UserService;
import com.sunveee.template.ssm.util.LogUtil;
import com.sunveee.template.ssm.util.PageHandler;

/**
 * 用户列表分页查询
 * 
 * @author 51
 * @version $Id: UserController.java, v 0.1 2018年6月6日 下午2:54:11 51 Exp $
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService         userService;

    @RequestMapping(value = "/list")
    public String toUserList(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo, @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize, Model model) {
        LogUtil.debug(logger, "访问用户列表Controller");

        List<User> userPage = userService.getUserPage(pageNo, pageSize);
        model.addAttribute("userPage", userPage);
        int infoCount = userService.getAllUserCount();
        PageHandler.handlePage(pageNo, pageSize, infoCount, model);
        return "user/user-list";
    }

    @ResponseBody
    @RequestMapping(value = "/id/{userId}", produces = "text/plain; charset=UTF-8")
    public String toUserInfo(@PathVariable("userId") Integer userId) {
        LogUtil.debug(logger, "根据用户id访问用户信息Controller");

        User user = userService.getUserById(userId);
        return JSON.toJSONString(user);
    }

}