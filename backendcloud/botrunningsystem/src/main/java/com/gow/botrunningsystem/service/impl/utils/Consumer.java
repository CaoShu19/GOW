package com.gow.botrunningsystem.service.impl.utils;

import com.gow.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @author : Str2ke
 * @date : 2023/3/12 下午12:59
 * @Desc :
 */
@Component
public class Consumer extends Thread{
    private Bot bot;

    private final static String receiveBOtMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";
    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }
    public void startTimeout(long timeout,Bot bot){
        this.bot = bot;
        //开启一个线程去执行run函数中的代码
        this.start();

        //joinAPI：当线程执行start后，调用jion（time）就会让主线程阻塞，让调用线程执行time时间后，再唤醒主线程
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //让线程中断
            this.interrupt();
        }

    }

    private String addUid(String code, String uid) {  // 在code中的Bot类名后添加uid
        int k = code.indexOf(" implements com.gow.botrunningsystem.utils.BotInterface");
        String S =  code.substring(0, k) + uid + code.substring(k);
        System.out.println("add uid 后"+S);
        return S;
    }

//    @Override
//    public void run() {
//        UUID uuid = UUID.randomUUID();
//        String uid = uuid.toString().substring(0,8);
//        System.out.println(uid);
//        //通过传入的代码和名称实现的动态类对象，赋值给接口A对象，“tips：名称的代码必须实现接口A”
//        BotInterface botInterface = Reflect.compile(
//                "com.gow.botrunningsystem.utils.Bot" + uid,
//                addUid(bot.getBotCode(),uid)
//        ).create().get();
//
//        //编译好bot代码，将输入传入，得到bot的下一个方向
//        Integer direction = botInterface.nextMove(bot.getInput());
//
//        System.out.println("bot-move-direction: "+ bot.getUserId() + ":" + direction);
//        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
//        data.add("user_id",bot.getUserId().toString());
//        data.add("direction",direction.toString());
//
//        restTemplate.postForObject(receiveBOtMoveUrl,data,String.class);
//    }
        public void run() {
            UUID uuid = UUID.randomUUID();
            String uid = uuid.toString().substring(0, 8);
            String code = addUid(bot.getBotCode(), uid);
            //创建这个类
            createFile("Bot" + uid, code);
            try {
                BotInterface botInterface = CompilerUtil.generateClass("Bot" + uid, "com.gow.botrunningsystem.utils", code);

                Integer direction = botInterface.nextMove(bot.getInput());

                System.out.println("bot-move-direction: "+ bot.getUserId() + ":" + direction);
                MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
                data.add("direction", direction.toString());
                data.add("user_id", bot.getUserId().toString());

                deleteFile("Bot" + uid);
                restTemplate.postForObject(receiveBOtMoveUrl, data, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                deleteFile("Bot" + uid);
            }
        }

    private final String codeUrl = "/home/str2ke/MyProjection/GOW/backendcloud/botrunningsystem/src/main/java/com/gow/botrunningsystem/utils/";

    private void deleteFile(String name) {
        // 这个路径是自己写的，自己定义即可
        File file = new File(codeUrl + name + ".java");
        if (file.exists()) {
            System.out.println("----");
            file.delete();
        }
    }

    private void createFile(String name, String code) {
        try (FileWriter file = new FileWriter(codeUrl + name + ".java");) {
            file.write(code);
            file.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
