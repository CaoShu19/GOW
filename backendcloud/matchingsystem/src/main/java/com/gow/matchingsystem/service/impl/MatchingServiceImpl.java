package com.gow.matchingsystem.service.impl;

import com.gow.matchingsystem.service.MatchingService;
import com.gow.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

/**
 * @author : Str2ke
 * @date : 2023/3/10 下午5:23
 * @Desc :
 */
@Service
public class MatchingServiceImpl implements MatchingService {

    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating,Integer botId) {
        System.out.println("matching add player"+userId + ":" + rating + ":"+botId);
        matchingPool.addPlayer(userId,rating,botId);

        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player"+userId);
        matchingPool.removePlayer(userId);
        return "add player success";
    }
}
