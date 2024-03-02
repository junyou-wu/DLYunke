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
          <el-button type="primary" @click="login">登 录</el-button>
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
      }

    }
  },
  methods: {}

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