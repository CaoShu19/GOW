package com.gow.botrunningsystem.service.impl;

import com.gow.botrunningsystem.service.BotRunningService;
import com.gow.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

/**
 * @author : Str2ke
 * @date : 2023/3/11 下午4:26
 * @Desc :
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("addBot of userId:"+userId + ":" + botCode + " "+ input);
        botPool.addBot(userId,botCode,input);
        return "add bot success";
    }
}
