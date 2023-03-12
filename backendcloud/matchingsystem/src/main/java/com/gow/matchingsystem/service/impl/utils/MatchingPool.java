package com.gow.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Str2ke
 * @date : 2023/3/10 下午6:59
 * @Desc :
 */
@Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    //匹配模块包括多个县城：单独处理异步匹配的线程，将将玩家信息放入匹配模块的线程
    //这里涉及到players的读写并发，竞争问题,需要将数据加锁
    private ReentrantLock lock = new ReentrantLock();


    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";
    public void addPlayer(Integer userId,Integer rating,Integer botId){
        lock.lock();
        try{
            players.add(new Player(userId,rating,0,botId));
        }finally {
            lock.unlock();
        }
    }
    public void removePlayer(Integer userId){
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<>();
            for(Player player:players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        }finally {
            lock.unlock();
        }
    }


    //将所有在列表内的等待匹配的玩家的匹配时间加一
    private void increaseWaitingTime(){
        for(Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }

    /*
    匹配玩家：
    从队头取出一个开始往后面匹配，满足匹配条件后，就将其标记并返回
     */
    private void matchPlayers(){
        //System.out.println("match players:"+ players.toString());
        boolean[] used =new boolean[players.size()];
        for(int i = 0 ; i < players.size(); i++){
            if(used[i]) continue;
            for(int j = i + 1; j  < players.size(); j++){
                if(used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if(checkMatched(a,b)){
                    used[i] = used[j] = true;
                    sendResult(a,b);
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    //判断两名玩家是否匹配
    private boolean checkMatched(Player a, Player b){
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    //将匹配的两名玩家返回
    private void sendResult(Player a, Player b){
        System.out.println("send result:"+ a +":"+ b);
        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_id",b.getUserId().toString());
        data.add("b_bot_id",b.getBotId().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }


    }
}
