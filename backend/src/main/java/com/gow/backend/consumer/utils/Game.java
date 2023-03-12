package com.gow.backend.consumer.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSONObject;
import com.gow.backend.consumer.WebSocketServer;
import com.gow.backend.consumer.utils.Player;
import com.gow.backend.pojo.Record;

/**
 * @author : Str2ke
 * @date : 2023/3/9 下午6:54
 * @Desc :
 */
public class Game extends Thread{
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    private int[][] g;
    final private static int[] dx = {-1,0,1,0}, dy = {0,1,0,-1};


    //一张地图上有两名玩家
    private final Player playerA;
    private final Player playerB;

    //玩家的下一步操作
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    //解决两个客户线程对nextStep的读写并发问题
    private ReentrantLock lock = new ReentrantLock();
    //表示游戏的状态 playing -> finished
    private String status = "playing";
    //输赢状态 A（A输） B（B输） all（平局）
    private String loser = "";


    public Game(Integer rows,Integer cols,Integer inner_walls_count,Integer idA, Integer idB){
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        //两名玩家的初始化
        playerA = new Player(idA,rows-2,1,new ArrayList<>());
        playerB = new Player(idB,1,cols-2,new ArrayList<>());
    }

    public Player getPlayerA(){
        return this.playerA;
    }
    public Player getPlayerB(){
        return this.playerB;
    }

    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }
    }
    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }
    public int[][] getG(){
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }

    /*
    等待两名玩家的下一步操作,并执行逻辑判定,
    如果8秒内，双方都有输入，并存入操作，那么返回有下一步，否则返回无下一步
     */
    private boolean nextStep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            //会在8秒内进行判断，下一步
            for(int i = 0; i < 80; i++){
                Thread.sleep(100);
                lock.lock();//要读一个共享数据，必须先加锁后再读，否则容易出现并发逻辑冲突
                try {
                    if(nextStepA != null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    private String getStringFromMap(){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                buffer.append(g[i][j]);
            }
        }
        return buffer.toString();
    }

    private void saveToDataBase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerB.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStringFromSteps(),
                playerB.getStringFromSteps(),
                getStringFromMap(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    /*
    向玩家广播结果
     */
    private void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDataBase();
        sendAllMessage(resp.toJSONString());
    }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n = cellsA.size();
        Cell cellA_end = cellsA.get(n-1);
        //尾部碰墙检测
        if(g[cellA_end.x][cellA_end.y] == 1) return false;
        //尾部碰撞身体检测
        for(int i = 0; i < n - 1; i++){
            if(cellsA.get(i).x == cellA_end.x && cellsA.get(i).y == cellA_end.y)
                return false;
        }
        //尾部和另一条蛇的碰撞检测
        for(int i = 0; i < n - 1; i++){
            if(cellsB.get(i).x == cellA_end.x && cellsB.get(i).y == cellA_end.y){
                return false;
            }
        }
        return true;
    }


    /*
    判断两名玩家下一步操作是否合法
     */
    private void judge(){
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA,cellsB);
        boolean validB = check_valid(cellsB,cellsA);

        if(!validA || !validB){
            status = "finished";
            if(!validA && !validB){
                loser = "all";
            } else if (!validA) {
                loser = "A";
            }else {
                loser = "B";
            }
        }
    }
    /*
    向玩家传递移动信息
     */
    private void sendMove(){

        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;

        }finally {
            lock.unlock();
        }
    }

    private void sendAllMessage(String message){
        WebSocketServer.usersToSock.get(playerA.getId()).sendMessage(message);
        WebSocketServer.usersToSock.get(playerB.getId()).sendMessage(message);
    }
    @Override
    public void run() {
        for(int i = 0; i < 1000; i++){
            if(nextStep()){ //获取到下一步操作
                judge();
                if("playing".equals(status)){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }else{
                //游戏状态进入判决结束
                status = "finished";
                lock.lock();
                try {
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    }else if(nextStepA == null){
                        loser = "A";
                    }else{
                        loser = "B";
                    }
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
