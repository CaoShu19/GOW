<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { GameMap } from '../assets/scripts/GameMap';
import { ref,onMounted } from 'vue';

export default{
    setup(){
        
        let parent = ref(null);
        let canvas = ref(null);

        //onMounted函数会在引用挂载在标签上完毕后，执行里面的函数参数
        onMounted(()=>{
            
            new GameMap(canvas.value.getContext('2d'),parent.value)
            console.log(parent.value);
        })

        return {
            parent,
            canvas,
        }
    }
}
</script>

<style scoped>
div.gamemap{
    height: 100%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>