package com.gow.backend.service.impl.pk;

import com.gow.backend.consumer.WebSocketServer;
import com.gow.backend.consumer.utils.Game;
import com.gow.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午1:52
 * @Desc :
 */
@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {

        System.out.println("back-end receive bot move"+ userId + " -direction " + direction);

        if(WebSocketServer.usersToSock.get(userId) != null){
            Game game  =   WebSocketServer.usersToSock.get(userId).game;
          if (game != null)
            if(game.getPlayerA().getId().equals(userId)){
                game.setNextStepA(direction);
            }else if(game.getPlayerB().getId().equals(userId)){
                game.setNextStepB(direction);
            }
        }

        return "receive bot move success";
    }
}
