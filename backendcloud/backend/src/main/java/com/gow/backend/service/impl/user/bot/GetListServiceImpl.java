package com.gow.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gow.backend.mapper.BotMapper;
import com.gow.backend.pojo.Bot;
import com.gow.backend.pojo.User;
import com.gow.backend.service.user.bot.GetListService;
import com.gow.backend.utils.GetUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Str2ke
 * @date : 2023/3/9 上午2:09
 * @Desc :
 */
@Service
public class GetListServiceImpl implements GetListService {

    @Autowired
    private BotMapper botMapper;
    @Override
    public List<Bot> getList() {
        User user = GetUserInfo.getUser();
        QueryWrapper<Bot> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",user.getId());
        List<Bot> bots = botMapper.selectList(wrapper);
        return bots;
    }
}
