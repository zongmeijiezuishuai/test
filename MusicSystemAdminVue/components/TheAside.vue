
<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#334256"
            text-color="#ffffff"
            active-text-color="#20a0ff"
            router
         >
            <template v-for="item in items">
                <template>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon" style="font-size: 26px;">
                            <b style="font-size: 20px;">
                                 {{item.title}}
                            </b>
                            </i>
                        
                    </el-menu-item>
                </template>
            </template>
            
        </el-menu>
    </div>
</template>

<script>
import bus from "../assets/js/bus"
export default {
    data(){
        return{
            collapse: false,
            items:[
                {
                    icon: 'iconfont icon-r-home',
                    index: 'Info',
                    title: '系统首页'
                },
                {
                    icon: 'iconfont icon-r-user1',
                    index: 'Consumer',
                    title: '用户管理'
                },
                {
                    icon: 'iconfont icon-r-love',
                    index: 'Singer',
                    title: '歌手管理'
                },
                {
                    icon: 'iconfont icon-r-list',
                    index: 'SongList',
                    title: '歌单管理'
                },
                
            ]
        }
    },
    computed:{
        onRoutes(){
            return this.$route.path.replace('/','');
        }
    },
    created(){
        //通过Bus进行组件间的通信，来折叠侧边栏
        bus.$on('collapse',msg =>{
            this.collapse = msg
        })
    }
}
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    background-color: #334256;
    overflow-y: scroll;
}

.sidebar::-webkit-scrollbar{
    width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse){
    width: 150px;
}

.sidebar >ul {
    height: 100%;
}
</style>