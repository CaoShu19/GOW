<template>
    <ContentField>
        <div class="row justify-content-center">
            <div class="col-3">
                <form @submit.prevent = "register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="输入账户">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密  码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">确认密码</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="再次输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <div class="mb-3">
                        <label for="photo" class="form-label">头像地址</label>
                        <input v-model="photoUrl" type="text" class="form-control" id="photo" placeholder="输入你的头像的网络地址哦">
                    </div>
                    <button type="submit" class="btn btn-primary">Primary</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import { ref } from 'vue';

// import router from '../../../router/index';
import $ from 'jquery';
import router from '@/router';


export default{

    components:{
        ContentField,
    },
    setup(){
        
        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');
        let photoUrl = ref('');

        const register = () =>{
            $.ajax({
                url:"http://localhost:3000/user/account/register/",
                type:"POST",
                
                data:{
                  username:username.value,
                  password:password.value,
                  confirmedPassword:confirmedPassword.value,
                  photoUrl:photoUrl.value,
                },
                success:(resp)=>{
                    
                    if(resp.error_message === "success"){
                        router.push({name:"user_account_login"});
                    }else{
                        error_message.value = resp.error_message;
                    }
                }
              })
        }

        return {
            username,
            password,
            confirmedPassword,
            photoUrl,
            error_message,
            register,
        }
    }
}

</script>

<style scope>
div.error-message{
    color: red;
}
</style>