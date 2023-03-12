package com.gow.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午12:21
 * @Desc :
 */
public class BotPool extends Thread{

    //可重入锁
    private final ReentrantLock lock = new ReentrantLock();
    //线程调度资源模型
    private final Condition condition = lock.newCondition();
    //bot队列
    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot(Integer userId,String botCode,String input){
        lock.lock();
        try {
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }


    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);
    }



    @Override
    public void run() {
        while(true){
            lock.lock();
            //如果队列为空，那么把当前线程阻塞
            if(bots.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else{
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot);//消耗时间，因为可能编译我们的bot代码
            }
        }
    }
}
