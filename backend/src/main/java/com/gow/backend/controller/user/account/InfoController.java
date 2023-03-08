package com.gow.backend.controller.user.account;

import com.gow.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/8 上午1:41
 * @Desc :
 */

@RestController
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info/")
    public Map<String,String> getInfo(){
        return infoService.getInfo();
    }
}
