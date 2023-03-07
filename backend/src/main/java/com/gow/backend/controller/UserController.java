package com.gow.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gow.backend.mapper.UserMapper;
import com.gow.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Str2ke
 * @date : 2023/3/7 下午1:11
 * @Desc :
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/all")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/add/{userId}/{username}/{password}")
    public String addUser(@PathVariable int userId,
                          @PathVariable String username,
                          @PathVariable String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);
        User user = new User(userId, username, encodePassword,"avater");
        userMapper.insert(user);
        return "add success";
    }
}
