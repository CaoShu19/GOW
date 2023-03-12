package com.gow.matchingsystem.service;

/**
 * @author : Str2ke
 * @date : 2023/3/10 下午5:22
 * @Desc :
 */
public interface MatchingService {
    String addPlayer(Integer userId,Integer rating,Integer botId);
    String removePlayer(Integer userId);
}
