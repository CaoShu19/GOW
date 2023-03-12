package com.gow.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Str2ke
 * @date : 2023/3/9 下午9:32
 * @Desc :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;//-1 表示人工操作，否则表示bot
    private String botCode;
    // sx sy 是起点坐标
    private Integer sx;
    private Integer sy;
    //存放玩家的坐标记录（存放每次玩家的输入指令）
    private List<Integer> steps;

    /*
    检验当前回合蛇的长度是否增加
     */
    public boolean check_tail_increasing(int steps){
        if(steps <= 10) return true;
        return steps % 3 == 1;
    }

    public String getStringFromSteps(){
        StringBuffer buffer = new StringBuffer();
        for(int d : steps){
            buffer.append(d);
        }
        return buffer.toString();
    }
    //返回蛇的身体
    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1,0,1,0};
        int[] dy = {0,1,0,-1};

        int x = sx;
        int y = sy;

        res.add(new Cell(x,y));
        int step = 0;
        for(int d : steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing( ++ step)){
                res.remove(0);
            }
        }
        return res;
    }

}
