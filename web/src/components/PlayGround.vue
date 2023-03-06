<template>
    <div class="playground">
        <GameMap></GameMap>
    </div>
</template>

<script>
import  GameMap  from './GameMap.vue';
export default{
    components:{
        GameMap,
    },
    setup(){
        
    }
    
}

</script>

<style scoped>
div.playground{
    width: 60vw;
    height: 70vh;
    background-color: aqua;
    margin: 50px auto;
}
</style>