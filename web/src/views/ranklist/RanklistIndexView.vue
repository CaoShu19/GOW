<template>
        <ContentField>
        <table class="table table-striped" style="text-align: center;">
            <thead>
                <tr>
                    <th>玩家</th>
                   
                    <th>天梯积分</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ user.username }}</span>
                    </td>
                    <td>
                        {{user.rating }}
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item disabled"  @click="click_page(-2)" >
            <span class="page-link" href="#">上一页</span>
            </li>
            <li @click="click_page(page.number)" :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number">
                <a class="page-link" href="#">{{ page.number }}</a>
            </li>
            <li class="page-item"  @click="click_page(-1)" >
            <a class="page-link" href="#">下一页</a>
            </li>
        </ul>
        </nav>
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import $ from 'jquery'
import { ref } from 'vue';


export default{
    components:{
        ContentField,
    },
    setup(){
        let currentPage = 1;
        let users = ref([]);
        let total_users = 0;
        let pages = ref([]);

        const click_page = page =>{
            if(page === -2) page = currentPage - 1;
            else if(page === -1) page = currentPage + 1;
            let max_pages = parseInt(Math.ceil(total_users/3));
            if(page >= 1 && page <= max_pages){
                pull_page(page);
            }
        }

        const update_pages = () =>{
            let max_pages = parseInt(Math.ceil(total_users/3));
            let new_pages = [];
            for(let i = currentPage - 2; i <= currentPage + 2; i++){
                if(i >= 1 && i <= max_pages){
                    new_pages.push({
                        number:i,
                        is_active: i === currentPage ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page =>{
            currentPage = page;
            $.ajax({
                url:"http://127.0.0.1:3000/ranklist/getlist/",
                type:"GET",
                data:{
                    page,
                },
                headers:{
                    Authorization:"Bearer "+ localStorage.getItem("jwt_token"),
                },
                success:(resp)=>{
                    console.log(resp);
                    users.value = resp.users;
                    total_users = resp.users_count;
                    update_pages();
                },
            })
        }
        pull_page(currentPage);

        
        return{
            users,
            pages,
            update_pages,
            click_page,
        }
    },
}

</script>

<stylte scope>

</stylte>