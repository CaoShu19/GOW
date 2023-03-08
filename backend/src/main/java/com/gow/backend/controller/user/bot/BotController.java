package com.gow.backend.controller.user.bot;

import com.gow.backend.pojo.Bot;
import com.gow.backend.service.user.bot.AddService;
import com.gow.backend.service.user.bot.GetListService;
import com.gow.backend.service.user.bot.RemoveService;
import com.gow.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author : Str2ke
 * @date : 2023/3/9 上午12:47
 * @Desc :
 */
@RestController
public class BotController {

    @Autowired
    private AddService addService;
    @Autowired
    private GetListService getListService;
    @Autowired
    private RemoveService removeService;
    @Autowired
    private UpdateService updateService;

    @PostMapping("/user/bot/add/")
    public Map<String,String> add(@RequestParam Map<String,String> data){
        return addService.add(data);
    }

    @PostMapping("/user/bot/remove/")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return removeService.remove(data);
    }

    @PostMapping("/user/bot/update/")
    public Map<String,String> update(@RequestParam Map<String,String> data){
        return updateService.update(data);
    }

    @GetMapping("/user/bot/getlist/")
    public List<Bot> getList(){
        return getListService.getList();
    }

}
