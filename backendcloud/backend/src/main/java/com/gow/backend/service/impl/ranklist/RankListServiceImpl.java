package com.gow.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gow.backend.mapper.UserMapper;
import com.gow.backend.pojo.User;
import com.gow.backend.service.ranklist.Ranklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午11:41
 * @Desc :
 */

@Service
public class RankListServiceImpl implements Ranklist {

    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        IPage<User> userIPage = new Page<>(page,3);
        queryWrapper.orderByDesc("rating");
        List<User> userList = userMapper.selectPage(userIPage,queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for(User user: userList){
            user.setPassword("");
        }
        resp.put("users",userList);
        resp.put("users_count",userMapper.selectCount(null));
        return resp;
    }
}
