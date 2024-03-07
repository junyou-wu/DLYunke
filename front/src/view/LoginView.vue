<template>
  <el-container>
    <el-aside width="200px">
      <img src="../assets/OIP.jpeg">
    </el-aside>

    <el-main>
      <div class="loginTitle">欢迎登录</div>

      <!--有规则限制的表单
      1，在el-form标签中写入:rules
      2，el-form-item标签中写入参数prop
      3，在data()中添加数据（规则应为数组）
       https://element-plus.org/zh-CN/component/form.html
       -->
      <el-form ref="loginRefForm" :model="user" label-width="120px" :rules="loginRules">
        <el-form-item label="账号" prop="loginAct">
          <el-input v-model="user.loginAct" />
        </el-form-item>

        <el-form-item label="密码" prop="loginPwd">
          <el-input type="password" v-model="user.loginPwd" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="login()">登 录</el-button>
        </el-form-item>

        <el-form-item>
          <el-checkbox label="记住我" v-model="user.rememberMe" />
        </el-form-item>
      </el-form>

    </el-main>
  </el-container>
</template>

<script>
import {defineComponent} from 'vue'
import {doGet, doPost} from "../http/httpRequest.js";
import {getTokenName, messageTip, removeToken} from "../utils/utils.js";
export default defineComponent({
  name: "LoginView",

  data() {
    return {
      user : {},
      loginRules: {
        loginAct: [
          { required: true, message: '账号不能为空', trigger: 'blur' },
        ],
        loginPwd: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 16, message: '密码长度应为6至16位', trigger: 'blur' },
        ]
      },
      rememberMe:false
    }
  },
  mounted() {
    this.freeLogin();
  },
  methods: {
    login(){
      this.$refs.loginRefForm.validate((isValid) =>{
        if(isValid){
          let formData = new FormData();
          formData.append("loginAct",this.user.loginAct);
          formData.append("loginPwd",this.user.loginPwd);
          formData.append("rememberMe",this.user.rememberMe);
         // console.log(formData.get("loginAct"),formData.get("loginPwd"));
          doPost("/api/login",formData).then((resp)=>{
            if(resp.data.code === 200){
              messageTip("登录成功","success");
              removeToken()
              let str
              //前端存储token
              if (this.user.rememberMe === true) {
                window.localStorage.setItem(getTokenName(), resp.data.data);

              } else {
                window.sessionStorage.setItem(getTokenName(), resp.data.data);

              }

              this.$router.push({
                path:'/dashboard',
              });
            }else{
              messageTip("登录失败","error");
            }
          });
        }
      })
    },
    freeLogin() {
      let token = window.localStorage.getItem(getTokenName());
      if (token) { //表示token有值，token不是空，token存在
        doGet("/api/login/free", {}).then(resp => {
          if (resp.data.code === 200)  {
            //token验证通过了，那么可以免登录
            window.location.href = "/dashboard";
          }
        })
      }
    }
  }

})
</script>

<style scoped>
.el-aside {
  background: darkgray;
  width: 40%;
  text-align: center;
}
.el-main {
  height: calc(100vh);
}
img {
 height: 400px;
  width: 100%;
}

.el-button {
  width: 100%;
}

.loginTitle {
  text-align: center;
  margin-top: 50px;
  margin-bottom: 50px;
  font-size: 25px;
}
</style>