<template>
  <NavBar></NavBar>
  
  <router-view/>
</template>

<script>

import {ref} from 'vue';
import NavBar from '@/components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"


export default{
  components:{
        NavBar,
    },
  name:"App",
  setup:()=>{
    let bot_name = ref("");
    let bot_rating = ref("");

    return {
      bot_name,
      bot_rating
    }
  }
}


</script>
<style>
body{
  /* background-image: url("@/assets/images/background.png"); */
  background:  linear-gradient(to right, green, purple);
  background-size: cover;
}
</style>
