package com.gow.backend.controller.user.account;

import com.gow.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/8 上午2:21
 * @Desc :
 */
@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/user/account/register/")
    public Map<String,String> register(@RequestParam Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        String confirmPassword = map.get("confirmedPassword");
        String photoUrl = map.get("photoUrl");
        return registerService.register(username,password,confirmPassword,photoUrl);
    }
}
