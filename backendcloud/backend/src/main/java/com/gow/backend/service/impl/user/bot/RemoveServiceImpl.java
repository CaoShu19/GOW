package com.gow.backend.service.impl.user.bot;

import com.gow.backend.mapper.BotMapper;
import com.gow.backend.pojo.Bot;
import com.gow.backend.pojo.User;
import com.gow.backend.service.user.bot.RemoveService;
import com.gow.backend.utils.GetUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/9 上午1:20
 * @Desc :
 */
@Service
public class RemoveServiceImpl implements RemoveService {

    @Autowired
    private BotMapper botMapper;


    @Override
    public Map<String, String> remove(Map<String, String> data) {
        User user = GetUserInfo.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);
        Map<String,String> map = new HashMap<>();
        if(bot == null){
            map.put("error_message","bot 不存在");
            return map;
        }
        if(!bot.getUserId().equals(user.getId()) ){
            map.put("error_message","此bot对于你无权限");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("error_message","success");
        return map;
    }
}
