package com.gow.backend.service.user.account;

import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/8 上午12:44
 * @Desc :
 */
public interface RegisterService {
    public Map<String,String> register(String username,String password,String confirmPassword,String photoUrl);
}
