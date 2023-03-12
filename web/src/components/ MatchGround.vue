<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model = "select_bot" aria-label="Default select example" class="form-select">
                        <option value = "-1" selected>玩家操作</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                    </select>
                </div>
                
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
                
            </div>
        </div>
 
            <div class="col-12" style="text-align: center;padding-top: 15vh;">
                <button @click="click_match_btn" type="button" class="btn btn-success match-btn">{{ match_btn_info }}</button>
            </div>

    </div>
</template>

<script>
import store from '@/store';
import{ref} from 'vue'

import $ from 'jquery'
export default{
    components:{
        
    },
    setup(){
        let match_btn_info = ref("开始匹配");
        let bots = ref([]);
        //双向数据绑定 MVVM
        let select_bot = ref("-1");

        const refresh_bots = () =>{
            $.ajax({
                url:"http://localhost:3000/user/bot/getlist/",
                type:"GET",
            
                headers:{
                    Authorization:"Bearer "+ localStorage.getItem("jwt_token"),
                },
                success:(resp)=>{
                   bots.value = resp;
                },
        })
        }
        refresh_bots();
        

        const click_match_btn = () =>{
            if(match_btn_info.value === "开始匹配"){
                match_btn_info.value = "取消匹配";
                console.log(select_bot.value,"bot id");
                //通过存放在前端的socket进行请求发送，将对象序列化后发送出去
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                    bot_id:select_bot.value,
                }))
            }else{
                match_btn_info.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event:"stop-matching",
                }))
            }
        }
        return{
            match_btn_info,
            click_match_btn,
            refresh_bots,
            bots,
            select_bot,
        }
    }
    
}

</script>

<style scoped>
div.matchground{
    width: 60vw;
    height: 70vh;
    background-color: rgba(7, 110, 13, 0.5);
    margin: 50px auto;
    border-radius: 15%;
    
}
div.user-photo{
    text-align: center;

}
div.user-photo>img{
    margin-top: 15%;
    border-radius: 50%;
    width: 25%;
    height: 25%;

}
div.user-username{
    margin-top: 10px;
    text-align: center;
    font-size: 30px;
    font-weight: 600;
    color: #fff;
}
button.match-btn{
    
    height: 500%;
    width: 40%;
    
}

div.user-select-bot{
    padding-top: 15vh;
}
div.user-select-bot > select{
    width: 80%;
    margin: 0 auto;
}

</style>