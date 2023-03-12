<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status === 'matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser !='none'"></ResultBoard>
</template>

<script>

import PlayGround from '../../components/PlayGround.vue'
//当组件被挂载好后，onMounted会自动执行
//当组件被卸载好后，onUnmounted会自动执行
import { onMounted,onUnmounted } from 'vue';
import { useStore } from 'vuex';
import ResultBoard from '../../components/ResultBoard.vue'
import MatchGround from '../../components/ MatchGround.vue'


export default{
    components:{
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup(){
        const store = useStore();
        
        store.commit("updateOpponent",{
            username:"opponent",
            photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
        })
        const socketUrl = `ws://127.0.0.1:3000/websocket/${localStorage.getItem("jwt_token")}/`;


        store.commit("updateLoser", "none");
        store.commit("updateIsRecord",false);
        let socket = null;
        onMounted(() =>{
            socket = new WebSocket(socketUrl);
            
            //当socket链接成功后会执行onopen函数
            socket.onopen = () =>{
                console.log("connected");
                store.commit("updateSocket",socket);
            }

            //当socket接受到数据后会执行onmessage函数
            socket.onmessage = msg =>{
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching"){
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo:data.opponent_photo,
                    });
                    setTimeout(()=>{
                        store.commit("updateStatus","playing");
                    },200);
                    store.commit("updateGame",data.game);                 
                }else if(data.event === "move"){
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    //得到地图中的两条蛇
                    const [snake0,snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                }else if(data.event === "result"){
                    const game = store.state.pk.gameObject;
                    //得到地图中的两条蛇
                    const [snake0,snake1] = game.snakes;
                    if(data.loser === "all" || data.loser === "A"){
                        snake0.status = "die";
                    }
                    if(data.loser === "all" || data.loser === "B"){
                        snake1.status = "die";
                    }
                    store.commit("updateLoser",data.loser);
                }
                
            }

            //当socket链接关闭后会执行onclose函数
            socket.onclose =()=>{
                console.log("disconnected");
            }
        })


        onUnmounted(() =>{
            socket.close();
            store.commit("updateStatus","matching");
        })

    }
}

</script>

<stylte scope>

</stylte>