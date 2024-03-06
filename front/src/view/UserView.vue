<template>
  <el-button type="primary">添加用户</el-button>
  <el-button type="danger">批量删除</el-button>

  <el-table
      ref="multipleTableRef"
      :data="userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55" />
    <el-table-column type="index" label="序号" width="100"/>
    <el-table-column property="loginAct" label="账号" width="150" />
    <el-table-column property="name" label="姓名" width="200" />
    <el-table-column property="phone" label="手机号" width="200" />
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
      layout="prev, pager, next"
      :page-size=pageSize
      :total=total
      @prev-click="toPage"
      @current-change="toPage"
      @next-click="toPage"
  />

</template>

<script>
import {doGet} from "../http/httpRequest";

export default {
  name: "UserView",
  data(){
    return {
      userList: [{}],
      pageSize: 0,
      total: 0
    }
  },
  mounted() {
    this.getUserList(1)
  },
  methods: {
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
    }
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