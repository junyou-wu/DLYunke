<template>
  <el-button type="primary" @click="add">添加用户</el-button>
  <el-button type="danger" @click="batchDel">批量删除</el-button>

  <el-table
      ref="multipleTableRef"
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55" />
    <el-table-column type="index" label="序号" width="100"/>
    <el-table-column property="loginAct" label="账号" width="150" />
    <el-table-column property="name" label="姓名" width="150" />
    <el-table-column property="phone" label="手机号" width="150" />
    <el-table-column property="email" label="邮箱" width="200" />
    <el-table-column property="createTime" label="创建时间" width="200" />
    <el-table-column label="操作" show-overflow-tooltip >
      <template #default="scope">
        <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
        <el-button type="success" @click="edit(scope.row.id)">编辑</el-button>
        <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination
      background
      layout="total,prev, pager, next, jumper"
      :page-size=pageSize
      :total=total
      :hide-on-single-page="false"
      @prev-click="toPage"
      @current-change="toPage"
      @next-click="toPage"
  />
<!--编辑/新增弹窗-->
  <el-dialog v-model="userDialogVisible" :title="userQuery.id > 0 ? '编辑用户' : '新增用户'" width="55%" center draggable>

    <el-form ref="userRefForm" :model="userQuery" label-width="110px" :rules="userRules">
      <el-form-item label="账号" prop="loginAct">
        <el-input v-model="userQuery.loginAct" />
      </el-form-item>

      <el-form-item label="密码" v-if="userQuery.id > 0"><!--编辑-->
        <el-input type="password" v-model="userQuery.loginPwd" />
      </el-form-item>

      <el-form-item label="密码" prop="loginPwd" v-else><!--新增-->
        <el-input type="password" v-model="userQuery.loginPwd" />
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input v-model="userQuery.name" />
      </el-form-item>

      <el-form-item label="手机" prop="phone">
        <el-input v-model="userQuery.phone" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userQuery.email" />
      </el-form-item>

      <el-form-item label="账号未过期" prop="accountNoExpired">
        <el-radio-group v-model="userQuery.accountNoExpired" class="ml-4">
          <el-radio value="1">是</el-radio>
          <el-radio value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="密码未过期" prop="credentialsNoExpired">
        <el-radio-group v-model="userQuery.credentialsNoExpired" class="ml-4">
          <el-radio value="1">是</el-radio>
          <el-radio value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="账号未锁定" prop="accountNoLocked">
        <el-radio-group v-model="userQuery.accountNoLocked" class="ml-4">
          <el-radio value="1">是</el-radio>
          <el-radio value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="账号是否启用" prop="accountEnabled">
        <el-radio-group v-model="userQuery.accountEnabled" class="ml-4">
          <el-radio value="1">是</el-radio>
          <el-radio value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="userDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="userSubmit">提 交</el-button>
      </span>
    </template>
  </el-dialog>

</template>

<script>
import {doDelete, doGet, doPost, doPut} from "../http/httpRequest";
import {messageConfirm, messageTip} from "../utils/utils.js";

export default {
  name: "UserView",
  data(){
    return {
      userList: [{}],
      pageSize: 0,
      total: 0,
      userDialogVisible:false,
      userQuery: {
        accountNoExpired : '1',
        credentialsNoExpired : '1',
        accountNoLocked : '1',
        accountEnabled : '1'
      },
      userRules : {
        loginAct : [
          { required: true, message: '请输入登录账号', trigger: 'blur' }
        ],
        loginPwd : [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, max: 16, message: '登录密码长度为6-16位', trigger: 'blur' }
        ],
        name : [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { pattern: /^[\u4E00-\u9FA5]{1,5}$/, message: '姓名必须是中文', trigger: 'blur'}
        ],
        phone : [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { pattern : /^1[3-9]\d{9}$/, message: '手机号码格式有误', trigger: 'blur'}
        ],
        email : [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { pattern : /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式有误', trigger: 'blur'}
        ],
      },
      options : [
        {label : '是', value : 1},
        {label : '否', value : 0}
      ],
      //用户id的数组
      userIdArray : []
    }
  },
  mounted() {
    this.getUserList(1)
  },
  methods: {
    handleSelectionChange(selectDataArray) {
      this.userIdArray = [];
      selectDataArray.forEach(data => {
        let userId = data.id;
        this.userIdArray.push(userId);
      })
    },
    getUserList(current){
      doGet("/api/userList",{current:current}).then((resp)=>{
        if(resp.data.code === 200){
          this.userList = resp.data.data.list;
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },
    //toPage方法的参数currentPage是由el的分页组件自动传递，无需手动获取
    toPage(currentPage){
      this.getUserList(currentPage)
    },

    view(id){
      this.$router.push("/dashboard/user/" + id);
    },
    add(){
      this.userDialogVisible = true;
    },
    edit(id){
      this.userDialogVisible = true;
      this.getUserById(id);
    },
    getUserById(id) {
      doGet("/api/user/" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          this.userQuery = resp.data.data;
          this.userQuery.loginPwd = "";
        }
      })
    },
    userSubmit(){
      let formData = new FormData();
      for (let index in this.userQuery){
        formData.append(index,this.userQuery[index]);
      }
      this.$refs.userRefForm.validate((isValid)=>{
        if(isValid){
          if (this.userQuery.id > 0) { /*编辑*/
            doPost("/api/user/edit", formData).then(resp => {
              if (resp.data.code === 200) {
                messageTip("编辑成功", "success");
                //页面刷新
                this.$router.go(0);
              } else {
                messageTip("编辑失败", "error");
              }
            })
          } else {
            doPost("/api/user/insert", formData).then(resp => {/*新增*/
              if (resp.data.code === 200) {
                messageTip("提交成功", "success");
                //页面刷新
                this.$router.go(0);
              } else {
                messageTip("提交失败", "error");
              }
            })
          }
        }
      })
    },
    del(id) {
      messageConfirm("您确定要删除该数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        doGet("/api/user/delete/" + id, {}).then(resp => {
          if (resp.data.code === 200) {
            messageTip("删除成功", "success");
            //页面刷新
            this.$router.go(0);
          } else {
            messageTip("删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //用户点击“取消”按钮就会触发catch函数
        messageTip("取消删除", "warning");
      })
    },
    batchDel() {
      if (this.userIdArray.length <= 0) {
        //提示一下
        messageTip("请选择要删除的数据", "warning");
        return;
      }
      messageConfirm("您确定要删除这些数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        let ids = this.userIdArray.join(",");
        doGet("/api/user/batchDel", {
          ids : ids
        }).then(resp => {
          console.log(resp);
          if (resp.data.code === 200) {
            messageTip("批量删除成功", "success");
            //页面刷新
            this.$router.go(0);
          } else {
            messageTip("批量删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //用户点击“取消”按钮就会触发catch函数
        messageTip("取消批量删除", "warning");
      })
    },

  }
}
</script>

<style scoped>
.el-table{
  margin-top: 15px;
}

.el-pagination{
  margin-top: 15px;
}
</style>