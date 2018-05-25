package com.coder520.user.service;

import com.coder520.common.util.SerurituUtils;
import com.coder520.user.dao.UserMapper;
import com.coder520.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
    *@Author Yusui
    *@Date 2018/5/21 21:11
    *@Description 根据用户名查询用户
    */
    @Override
    public User findUserByName(String username) {
        User user = userMapper.selectByUserName(username);
        return user;
    }

    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        user.setPassword(SerurituUtils.encrptyPassword(user.getPassword()));
        userMapper.insertSelective(user);
    }
}
