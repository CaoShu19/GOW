package com.gow.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.gow.backend.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午7:55
 * @Desc :
 */
@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/record/getlist/")
    JSONObject getList(@RequestParam Map<String,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return recordService.getRecordList(page);
    }
}
