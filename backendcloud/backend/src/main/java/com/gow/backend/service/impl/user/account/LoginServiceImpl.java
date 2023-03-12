package com.gow.backend.service.impl.user.account;

import com.gow.backend.pojo.User;
import com.gow.backend.service.impl.utils.UserDetailsImpl;
import com.gow.backend.service.user.account.LoginService;
import com.gow.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/8 上午12:45
 * @Desc :
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        //将用户名和密码得到token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);
        //吧token给权限管理去验证，验证不通过那么就自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //验证通过那么就会将用户包装成token返回
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());
        HashMap<String, String> map = new HashMap<>();

        map.put("error_message","success");
        map.put("token",jwt);

        return map;
    }
}
