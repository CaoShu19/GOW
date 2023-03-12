package com.gow.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gow.backend.consumer.utils.Game;
import com.gow.backend.mapper.RecordMapper;
import com.gow.backend.mapper.UserMapper;
import com.gow.backend.pojo.User;
import com.gow.backend.utils.GetUserInfo;
import com.gow.backend.utils.GetUserInfoByJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {//一个socket服务对应一个用户的连接
    //就是socket连接
    private Session session = null;
    private User user;
    //在类的共享内存中存放socket连接和用户关系：用于找到用户对应的连接服务，然后返回数据
    public final static ConcurrentHashMap<Integer,WebSocketServer> usersToSock = new ConcurrentHashMap<>();
    //存放在线匹配的所有用户
    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();

    private static UserMapper userMapper;
    /*我们仅仅是在类里面注入，而不是对象中注入userMapper*/
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    public static RecordMapper recordMapper;
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    private Game game = null;

    /*每当访问 /websocket/{token} 连接的时候，就会接受对面传来的连接*/
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected "+session);

        int userId = GetUserInfoByJwt.GetUserInfo(token);
        if(userId == -1) {
            this.session.close();
        }
        this.user = userMapper.selectById(userId);
        if(this.user != null){
            usersToSock.put(user.getId(),this);
        }else{
            this.session.close();
        }


    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disConnected  "+session);
        if(this.user != null) {
            usersToSock.remove(this.user.getId());
            matchPool.remove(this.user);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {//做路由转接
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }
    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            System.out.println("A"+direction);
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            System.out.println("B"+direction);
            game.setNextStepB(direction);
        }
    }

    private void startMatching() {
        System.out.println("start-matching");
        matchPool.add(this.user);
        while(matchPool.size() >= 2){
            Iterator<User> it = matchPool.iterator();
            User a = it.next();
            User b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(13,14,20,a.getId(),b.getId());
            game.createMap();

            //将游戏判定赋给websocket连接
            usersToSock.get(a.getId()).game = game;
            usersToSock.get(b.getId()).game = game;

            //开启游戏线程，会执行线程判定
            game.start();

            //写入玩家id和坐标和地图信息的response
            JSONObject respGame = new JSONObject();
            respGame.put("a_id",game.getPlayerA().getId());
            respGame.put("a_sx",game.getPlayerA().getSx());
            respGame.put("a_sy",game.getPlayerA().getSy());
            respGame.put("b_id",game.getPlayerB().getId());
            respGame.put("b_sx",game.getPlayerB().getSx());
            respGame.put("b_sy",game.getPlayerB().getSy());
            respGame.put("map",game.getG());


            JSONObject respA = new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("game",respGame);
            WebSocketServer serverA = usersToSock.get(a.getId());
            //将对A的回复发送过去
            serverA.sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("game",respGame);
            WebSocketServer serverB = usersToSock.get(b.getId());
            //将对A的回复发送过去
            serverB.sendMessage(respB.toJSONString());
        }

    }

    private void stopMatching() {
        System.out.println("stop-matching");

        matchPool.remove(this.user);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();

    }

    public void sendMessage(String message){
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}