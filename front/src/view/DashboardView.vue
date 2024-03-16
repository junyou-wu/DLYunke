<template>
  <el-container>
<!--左侧-->
    <el-aside width="isCollapse ? 56px : 200px">
      <div class="menuTitle">
        <el-icon class="TitleIcon"><Monitor /></el-icon>
        <span id="title">Manage System</span>
      </div>

      <el-menu
          active-text-color="#ffd04b"
          background-color="#1c4d69"
          class="el-menu-vertical-demo"
          text-color="#fff"
          style="border-right: solid 0px"
          :default-active='currentRoutePath'
          :collapse="isCollapse"
          :unique-opened="true"
          :collapse-transition="false"
          :router="true"
          @open="handleOpen"
          @close="handleClose"
      >
        <el-sub-menu :index="index" v-for="(menuPermission, index) in user.menuPermissionList" :key="menuPermission.id">
          <template #title>
            <el-icon><component :is="menuPermission.icon"></component></el-icon>
            <span> {{menuPermission.name}} </span>
          </template>
          <el-menu-item v-for="subPermission in menuPermission.subPermissionList" :key="subPermission.id" :index="subPermission.url">
            <el-icon><component :is="subPermission.icon"></component></el-icon>
            {{subPermission.name}}
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
<!--右侧-->
    <el-container class="rightContent">
      <!--      顶部-->
      <el-header>
        <el-icon class="show" @click="showMenu()"><Fold /></el-icon>
        <el-dropdown :hide-on-click="false" class="userMenu" trigger="click">
          <span class="el-dropdown-link" >
          欢迎您，{{ this.user.name }}用户
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>用户信息</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!-- 内容部分-->
      <el-main>
        <router-view></router-view>
      </el-main>
<!--      底部-->
      <el-footer>
        Footer
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import {doGet} from "../http/httpRequest";
import {messageConfirm, messageTip, removeToken} from "../utils/utils.js";


export default {
  name: "DashboardView",
  data(){
    return {
      isCollapse:false,
      user:{},
      currentRoutePath: ''
    }
  },
  mounted() {
    this.getUserInfo();
    this.getCurrentRoutePath();
  },
  methods:{
      showMenu(){
        this.isCollapse = !this.isCollapse

        document.getElementById("title").style.display = this.isCollapse ? "none": ""
      },
      getUserInfo(){
        doGet("api/login/info",{}).then((resp)=>{
          console.log(this.user)
          this.user = resp.data.data
        })
      },
    logout() {
      doGet("/api/logout", {}).then(resp => {
        if (resp.data.code === 200) {
          removeToken();
          messageConfirm("确定退出吗").then(()=>{
            messageTip("退出成功", "success");
            removeToken();
            //跳到登录页
            this.$router.push("/")
          }).catch(()=> {
            messageTip("取消操作", "error")
          })
        } else {
          messageConfirm("是否强制退出？").then(() => { //用户点击“确定”按钮就会触发then函数
            //后端验证token未通过,那没必要存储在浏览器中，直接删除一下
            removeToken();
            messageTip("强制退出成功", "success");
            //跳到登录页
            this.$router.push("/")
          }).catch(() => { //用户点击“取消”按钮就会触发catch函数
            messageTip("取消强制退出", "warning");
          })
        }
      })
    },
    getCurrentRoutePath() {
      let path = this.$route.path;
      let pathArr = path.split("/");

      if(pathArr.length > 3){
        this.currentRoutePath = "/" + pathArr[1] + "/" + pathArr[2];
      }else{
        this.currentRoutePath = path;
      }
      console.log(this.currentRoutePath);
    }
    },

}
</script>

<style scoped>
.el-aside{
  background: black;
}

.rightContent{
  height: calc(100vh);
}

.el-header{
  background: aqua;
  height: 30px;
}

.el-footer{
  background: aqua;
  height: 30px;
}

.menuTitle{
  color: aliceblue;
  text-align: center;
  height: 40px;
  line-height: 40px;
  background-color: #1c4d69;
  box-shadow:10px 0px 5px rgb(0, 0, 0, 30%);
}

.TitleIcon{
}

.show{
  cursor: pointer;
}

#title{
  text-align: center;
  font-size: 16px;
}

.userMenu{
  float: right;
  line-height: 30px;
}
.el-dropdown-link{
  background-color: aqua;
}
</style>