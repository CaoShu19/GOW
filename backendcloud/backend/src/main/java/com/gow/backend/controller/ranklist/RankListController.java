package com.gow.backend.controller.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.gow.backend.service.impl.ranklist.RankListServiceImpl;
import com.gow.backend.service.ranklist.Ranklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午11:42
 * @Desc :
 */
@RestController
public class RankListController {

    @Autowired
    private Ranklist ranklist;

    @GetMapping("/ranklist/getlist/")
    public JSONObject getRankList(@RequestParam Map<String,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return ranklist.getList(page);
    }
}
