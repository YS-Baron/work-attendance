package com.coder520.user.controller;


import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    /**
    *@Author Yusui
    *@Date 2018/5/22 19:13
    *@Description 获取用户信息
    */
    @RequestMapping("/userinfo")
    @ResponseBody
    public User getUser(HttpSession session){

        User user = (User) session.getAttribute("userinfo");
        return user;
    }

    /**
    *@Author Yusui
    *@Date 2018/5/22 19:48
    *@Description 退出界面
    */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }
}
