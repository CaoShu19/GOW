package com.gow.backend.utils;

import io.jsonwebtoken.Claims;

/**
 * @author : Str2ke
 * @date : 2023/3/9 下午4:51
 * @Desc :
 */
public class GetUserInfoByJwt {
    public static Integer GetUserInfo(String token){
        Integer userid = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  userid;
    }
}
