package com.gow.backend.service.record;

import com.alibaba.fastjson.JSONObject;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午7:52
 * @Desc :
 */
public interface RecordService {


    JSONObject getRecordList(Integer page);
}
