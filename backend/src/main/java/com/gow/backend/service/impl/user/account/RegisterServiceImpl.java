package com.gow.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gow.backend.mapper.UserMapper;
import com.gow.backend.pojo.User;
import com.gow.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/8 上午12:45
 * @Desc :
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        HashMap<String, String> map = new HashMap<>();
        if(username == null){
            map.put("error_message","username is null");
            return map;
        }
        if(password == null || confirmPassword == null){
            map.put("error_message","password is null");
            return map;
        }
        username = username.trim();
        if(username.length() == 0){
            map.put("error_message","username is null");
            return  map;
        }
        if(password.length() == 0 || confirmPassword.length() == 0){
            map.put("error_message","password is null");
            return  map;
        }
        if(username.length() > 100){
            map.put("error_message","username is too long ,do not than 100");
            return map;
        }
        if(password.length() > 100){
            map.put("error_message","password is too long ,do not than 100");
            return map;
        }

        if(!password.equals(confirmPassword)){
            map.put("error_message","double input is not equals");
            return  map;
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        List<User> users = userMapper.selectList(wrapper);
        if(!users.isEmpty()){
            map.put("error_message","user having existed");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/253232_lg_7900d48b65.jpg";
        User user = new User(null,username,encodedPassword,photo);
        userMapper.insert(user);
        map.put("error_message","success");
        return map;
    }
}
