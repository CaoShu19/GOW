<template>
    <div class="container">
        <div class="row">
            <div class="col-9">
                <div class="card"  style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 150%;">My Bot</span>
                        <button type="button" class="btn btn-primary float-end create-btn" data-bs-target="#add-bot-btn" data-bs-toggle="modal">创建BOT</button>
                        
                        
                        <div class="modal fade" id="add-bot-btn" aria-hidden="true"  tabindex="-1">
                        <div class="modal-dialog modal-dialog-centered modal-xl">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5">Create Bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">

                                <div class="mb-3">
                                    <label for="add-bot-title" class="form-label">BOT NAME</label>
                                    <input v-model="botadd.title" type="text" id="add-bot-title" class="form-control" placeholder="请输入BOT名称">
                                </div>
                                <div class="mb-3">
                                    <label for="add-bot-description" class="form-label">BOT DESCRIPTION</label>
                                    <textarea v-model="botadd.description" type="text" id="add-bot-description" class="form-control" placeholder="请输入BOT介绍"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label for="add-bot-code" class="form-label">BOT CODE</label>
                                    <VAceEditor
                                        v-model:value="botadd.content"
                                        @init="editorInit"
                                        lang="c_cpp"
                                        theme="textmate"
                                        style="height: 300px" />
                                </div>
                               
                            </div>
                            
                            <div class="modal-footer">
                                <div class="error-message">{{ botadd.error_message }}</div>
                                <button @click="addBot" style="width: 10%;" class="btn btn-primary" data-bs-target="add-bot-btn" data-bs-toggle="modal">确定</button>
                                <button style="width: 10%;" class="btn btn-warning" data-bs-target="add-bot-btn" data-bs-toggle="modal">取消</button>
                            </div>
                            </div>
                        </div>
                        </div>
                       
                        

                    </div>
                    <div class="card-body">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>CreateTime</th>
                                    <th>Operate</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-warning btn-bot" style="margin-right: 10px;" :data-bs-target="'#update-bot-modal-'+bot.id" data-bs-toggle="modal">Update</button>
                                        <div class="modal fade" :id="'update-bot-modal-'+bot.id" aria-hidden="true" aria-labelledby="exampleModalToggleLabel" tabindex="-1">
                                        <div class="modal-dialog modal-dialog-centered modal-xl">
                                            <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5">Create Bot</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">

                                                <div class="mb-3">
                                                    <label for="add-bot-title" class="form-label">BOT NAME</label>
                                                    <input v-model="bot.title" type="text" id="add-bot-title" class="form-control" placeholder="请输入BOT名称">
                                                </div>
                                                <div class="mb-3">
                                                    <label for="add-bot-description" class="form-label">BOT DESCRIPTION</label>
                                                    <textarea v-model="bot.description" type="text" id="add-bot-description" class="form-control" placeholder="请输入BOT介绍"></textarea>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="add-bot-code" class="form-label">BOT CODE</label>
                                                    <VAceEditor
                                                        v-model:value="bot.content"
                                                        @init="editorInit"
                                                        lang="c_cpp"
                                                        theme="textmate"
                                                        style="height: 300px" />
                                                </div>
                                            
                                            </div>
                                            
                                            <div class="modal-footer">
                                                <div class="error-message">{{ botadd.error_message }}</div>
                                                <button @click="update_bot(bot)" style="width: 10%;" class="btn btn-primary" :data-bs-target="'#update-bot-modal-'+bot.id" data-bs-toggle="modal">修改</button>
                                                <button style="width: 10%;" class="btn btn-warning" :data-bs-target="'#update-bot-modal-'+bot.id" data-bs-toggle="modal">取消</button>
                                            </div>
                                            </div>
                                        </div>
                                        </div>


                                        <button @click="remove_bot(bot)" type="button" class="btn btn-danger btn-bot">Delete</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-3">
                <div class="card"  style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
// import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex';
import { ref,reactive } from 'vue';
import $ from 'jquery'
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default{
    components:{
        VAceEditor,
    },
    setup(){

        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const botadd = reactive({
            title:"",
            description:"",
            content:"",
            error_message:"",
        })
        let bots = ref([]);
        const store = useStore();
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
        
        const addBot = () =>{
            botadd.error_message = "";

            $.ajax({
                url:"http://localhost:3000/user/bot/add/",
                type:"POST",
                
                data:{
                  title: botadd.title,
                  description: botadd.description,
                  content: botadd.content,
                },
                headers:{
                    Authorization:"Bearer "+ localStorage.getItem("jwt_token"),
                },
                success:(resp)=>{
                   if(resp.error_message === "success"){
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                   }else{
                        botadd.error_message = resp.error_message;
                   }
                },
             })
        }
        console.log(store);
        
        const remove_bot = (bot) =>{
            $.ajax({
                url:"http://localhost:3000/user/bot/remove/",
                type:"POST",
                
                data:{
                  bot_id:bot.id,
                },
                headers:{
                    Authorization:"Bearer "+ localStorage.getItem("jwt_token"),
                },
                success:(resp)=>{
                    if(resp.error_message === "success"){
                        refresh_bots();
                   }
                },
               
        })
        }

        const update_bot = (bot) =>{

            $.ajax({
                url:"http://localhost:3000/user/bot/update/",
                type:"POST",
                
                data:{
                  bot_id:bot.id,
                  title: bot.title,
                  description: bot.description,
                  content: bot.content,
                },
                headers:{
                    Authorization:"Bearer "+ localStorage.getItem("jwt_token"),
                },
                success:(resp)=>{
                   if(resp.error_message === "success"){
                        Modal.getInstance('#update-bot-modal-'+bot.id).hide();
                        refresh_bots();
                   }else{
                        botadd.error_message = resp.error_message;
                   }
                },
               
        })
        }

        

          return{
                refresh_bots,
                bots,
                botadd,
                addBot,
                remove_bot,
                update_bot,
            }
    }
}

</script>

<style scope>
button.create-btn{
    width: 25%;
    
    float: left;
}
button.btn-bot{
    width: 35%;
}
div.error-message{
    color: red;
}
</style>