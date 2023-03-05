package com.gow.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author : Str2ke
 * @date : 2023/3/5 下午11:53
 * @Desc :
 */

@RestController
@RequestMapping("pk/")
public class index {
    @RequestMapping("/index")
    public HashMap<String, String> index(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name","test");
        hashMap.put("rating","1900");
        return hashMap;
    }
}
