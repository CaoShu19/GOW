package com.gow.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午11:40
 * @Desc :
 */
public interface Ranklist {
    JSONObject getList(Integer page);
}
