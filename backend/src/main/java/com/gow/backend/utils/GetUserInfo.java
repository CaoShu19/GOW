package com.gow.backend.utils;

import com.gow.backend.pojo.User;
import com.gow.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author : Str2ke
 * @date : 2023/3/9 上午12:27
 * @Desc : 从SpringSecurity上下文中获得用户信息
 */

public class GetUserInfo {
    /*
    原理：从加密的token中获取到用户信息，由SpringSecurity包装实现
     */
    public static User getUser(){
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Object principal = authenticationToken.getPrincipal();
        UserDetailsImpl loginUser = (UserDetailsImpl) principal;
        return loginUser.getUser();
    }
}
