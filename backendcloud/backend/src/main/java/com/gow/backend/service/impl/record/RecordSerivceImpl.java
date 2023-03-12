package com.gow.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gow.backend.mapper.RecordMapper;
import com.gow.backend.mapper.UserMapper;
import com.gow.backend.pojo.Record;
import com.gow.backend.pojo.User;
import com.gow.backend.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午7:54
 * @Desc :
 */
@Service
public class RecordSerivceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getRecordList(Integer page) {
        IPage<Record> recordIPage = new Page<>(page,10);
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage,recordQueryWrapper).getRecords();

        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Record record:records){
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo",userA.getPhoto());
            item.put("a_username",userA.getUsername());
            item.put("b_photo",userB.getPhoto());
            item.put("b_username",userB.getUsername());
            String result = "平局";
            if("A".equals(record.getLoser())){
                result = "B胜利";
            }else if("B".equals(record.getLoser())){
                result = "A胜利";
            }
            item.put("result",result);
            item.put("record",record);
            items.add(item);
        }
        resp.put("records",items);
        resp.put("records_count",recordMapper.selectCount(null));
        return resp;
    }
}
