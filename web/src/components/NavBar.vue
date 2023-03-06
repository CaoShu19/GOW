<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <router-link class="navbar-brand" :to="{name:'home'}">GAMING WEB</router-link>
        
        <ul class="navbar-nav me-au:to mb-2 mb-lg-0">
            <li class="nav-item">
            <router-link :class="route_name == 'pk_index' ? 'nav-link active':'nav-link' " aria-current="page" :to="{name:'pk_index'}">STRAT</router-link>
            </li>
            <li class="nav-item">
            <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link' " :to="{name:'record_index'}">对局列表</router-link>
            </li>
            <li class="nav-item">
            <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link' " :to="{name:'ranklist_index'}">排行榜</router-link>
            </li>
        </ul>
        <ul class="navbar-nav">
            <li class="nav-item">
            <router-link class="nav-link " :to="{name:'home'}">Str2ke</router-link>
            </li>
            <li class="nav-item">
            <router-link class="nav-link " :to="{name:'user_bot_index'}">MyBot</router-link>
            </li>
            <li class="nav-item">
            <router-link class="nav-link " :to="{name:'home'}">Exit</router-link>
            </li>
        </ul>
        
        </div>
    
    </nav>
</template>

<script>

import { useRoute } from 'vue-router';
import {computed} from 'vue'


export default{
    setup(){
        const route = useRoute();
        //计算当前页面路径
        let route_name = computed(()=> route.name)

        return{
            route_name,
        }
    }
}

</script>

<style scoped>

</style>