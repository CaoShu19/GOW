package com.gow.backend.service.impl.user.account;

import com.gow.backend.pojo.User;
import com.gow.backend.service.impl.utils.UserDetailsImpl;
import com.gow.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/8 上午12:45
 * @Desc :
 */
@Service
public class InfoServiceImpl implements InfoService {



    @Override
    public Map<String, String> getInfo() {
        //从上下问中取到用户的信息，token解密后得到，并存放
        UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        HashMap<String, String> map = new HashMap<>();
        map.put("error_message","success");
        map.put("id",user.getId().toString());
        map.put("username", user.getUsername().toString());
        map.put("photo", user.getPhoto());
        return map;
    }
}
