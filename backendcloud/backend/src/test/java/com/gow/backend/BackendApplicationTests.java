package com.gow.backend;

import com.gow.backend.consumer.utils.Game;
import com.gow.backend.mapper.BotMapper;
import com.gow.backend.mapper.UserMapper;
import com.gow.backend.pojo.Bot;
import com.gow.backend.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private BotMapper botMapper;

    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println(bCryptPasswordEncoder.encode("123"));

    }

}
