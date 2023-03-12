package com.gow.backend.service.impl.pk;

import com.gow.backend.consumer.WebSocketServer;
import com.gow.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @author : Str2ke
 * @date : 2023/3/10 下午8:05
 * @Desc :
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId,Integer aBotId, Integer bId,Integer bBotId) {
        System.out.println("backend startGame:"+aId+" "+aBotId +"||"+ bId+" "+bBotId);
        WebSocketServer.startGame(aId,aBotId,bId,bBotId);
        return "startGame success";
    }
}
