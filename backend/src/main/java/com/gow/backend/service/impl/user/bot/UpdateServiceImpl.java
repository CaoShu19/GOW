package com.gow.backend.service.impl.user.bot;

import com.gow.backend.mapper.BotMapper;
import com.gow.backend.pojo.Bot;
import com.gow.backend.pojo.User;
import com.gow.backend.service.user.bot.UpdateService;
import com.gow.backend.utils.GetUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/9 上午1:50
 * @Desc :
 */
@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        HashMap<String, String> map = new HashMap<>();
        User user = GetUserInfo.getUser();
        int bot_id = Integer.parseInt(data.get("bot_id"));
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Bot bot = botMapper.selectById(bot_id);
        if(bot == null){
            map.put("error_message","bot 不存在");
            return map;
        }
        if(!bot.getUserId().equals(user.getId()) ){
            map.put("error_message","此bot对于你无权限");
            return map;
        }
        if(title == null || title.length() == 0){
            map.put("error_message","标题为空");
            return map;
        }
        if(title.length() > 100){
            map.put("error_message","标题长度不能大于100");
            return map;
        }
        if(description.length() == 0 || description == null){
            description = "未填描述";
        }
        if(description.length() > 300){
            map.put("error_message","Bot描述长度不能大于100");
            return map;
        }
        if(content == null || content.length() == 0){
            map.put("error_message","内容代码不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("error_message","内容代码不能超过10000");
            return map;
        }
        Date date = new Date();
        Bot newBot = new Bot(bot.getId(), user.getId(), title, description, content, 1500, bot.getCreatetime(), date);
        botMapper.updateById(newBot);
        map.put("error_message","success");
        return map;
    }
}
