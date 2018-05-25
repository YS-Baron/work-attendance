package com.coder520.login.controller;


import com.coder520.common.util.SerurituUtils;
import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
    * @ Author Yusui
    * @ Date 2018/5/21 21:09
    * @ Description 登入页面
    */
    @RequestMapping
    public String login() {

        return "login";
    }


    /**
    *@Author Yusui
    *@Date 2018/5/21 21:10
    *@Description 校验登录
    */
    @RequestMapping("/check")
    @ResponseBody
    public String checkLogin(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String username = request.getParameter("username");
        String pwd = request.getParameter("password");

        //查数据库 如果查到调用md5加密对比
        User user=userService.findUserByName(username);

        if(user!=null){
            if(SerurituUtils.checkPassword(pwd,user.getPassword())){
                //校验成功 设置session
                request.getSession().setAttribute("userinfo",user);
                return "login_succ";
            }else{
                //校验失败 返回校验失败的signal
                return "login_fail";
            }
        }else{
            //校验失败 返回校验失败的signal
            return "login_fail";
        }
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        userService.createUser(user);
        return "succ";
    }
}
