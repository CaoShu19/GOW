<template>
  <div>
    <div>name:{{ bot_name }}</div>
    <div>stan:{{ bot_rating }}</div>
  </div>
  
  <router-view/>
</template>

<script>
import $ from 'jquery';
import {ref} from 'vue';

export default{
  name:"App",
  setup:()=>{
    let bot_name = ref("");
    let bot_rating = ref("");

    $.ajax({
      url:"http://localhost:3000/pk/index",
      type:"GET",
      success:(resp)=>{
        
        bot_name.value = resp.name;
        bot_rating.value = resp.rating;
        console.log(resp);
      }
    })

    return {
      bot_name,
      bot_rating
    }
  }
}


</script>
<style>
body{
  background-image: url("@/assets/background.png");
  background-size: cover;
}
</style>
