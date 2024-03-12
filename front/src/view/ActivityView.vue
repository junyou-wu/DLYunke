<template>
  <el-form :inline="true" :model="activityQuery" :rules="activityRules">
    <el-form-item label="负责人">
      <el-select
          v-model="activityQuery.ownerId"
          placeholder="请选择负责人"
          @click="loadOwner"
          clearable>
        <el-option
            v-for="item in ownerOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
      </el-select>
    </el-form-item>

    <el-form-item label="活动名称">
      <el-input v-model="activityQuery.name" placeholder="请输入活动名称" clearable />
    </el-form-item>

    <el-form-item label="活动时间">
      <el-date-picker
          v-model="activityQuery.activityTime"
          type="datetimerange"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"/>
    </el-form-item>

    <el-form-item label="活动预算" prop="cost">
      <el-input v-model="activityQuery.cost" placeholder="请输入活动预算" clearable />
    </el-form-item>

    <el-form-item label="创建时间">
      <el-date-picker
          v-model="activityQuery.createTime"
          type="datetime"
          placeholder="请选择创建时间"
          value-format="YYYY-MM-DD HH:mm:ss"/>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="onSearch">搜 索</el-button>
      <el-button type="primary" plain @click="onReset">重 置</el-button>
    </el-form-item>
  </el-form>

  <el-button type="primary" @click="add">录入市场活动</el-button>
  <el-button type="danger" @click="batchDel">批量删除</el-button>

  <el-table
      :data="activityList"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" />
    <el-table-column type="index" label="序号" width="60" />
    <el-table-column prop="ownerDO.name" label="负责人"/>
    <el-table-column property="name" label="活动名称" show-overflow-tooltip />
    <el-table-column property="startTime" label="开始时间" show-overflow-tooltip />
    <el-table-column property="endTime" label="结束时间" show-overflow-tooltip />
    <el-table-column property="cost" label="活动预算" show-overflow-tooltip />
    <el-table-column property="createTime" label="创建时间" show-overflow-tooltip />
    <el-table-column label="操作" width="230">
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
      :page-size="pageSize"
      :total="total"
      @prev-click="toPage"
      @next-click="toPage"
      @current-change="toPage"/>

</template>

<script>
import {defineComponent} from 'vue'
import {doGet} from "../http/httpRequest.js";
import {messageConfirm, messageTip} from "../utils/utils.js";

export default defineComponent({
  name: "ActivityView",

  data() {
    return {
      userId: '',
      //市场活动搜索表单对象，初始值是空
      activityQuery : {
        activityTime: {}
      },
      //市场活动列表对象，初始值是空
      activityList : [{
        ownerDO : {}
      }],
      //分页时每页显示多少条数据
      pageSize : 0,
      //分页总共查询出多少条数据
      total : 0,
      //负责人的下拉列表数据
      ownerOptions : [{}],
      //定义市场活动搜索表单验证规则
      activityRules : {
        cost : [
          //正则表达式，从网上找，或者AI工具找，找到后需要测试一下，因为有可能找到的正则有问题
          { pattern : /^[0-9]+(\.[0-9]{2})?$/, message: '活动预算必须是整数或者两位小数', trigger: 'blur'}
        ]
      },
      ActivityIdArray : []
    }
  },

  mounted() {
    this.getData(1);
  },

  methods : {
    handleSelectionChange(selectDataArray) {
      this.ActivityIdArray = [];
      selectDataArray.forEach(data => {
        let activityId = data.id;
        this.ActivityIdArray.push(activityId);
      })
    },
    //查询用户列表数据
    getData(current) {
      let startTime = ''; //开始时间
      let endTime = ''; //结束时间
      for (let key in this.activityQuery.activityTime) {
        if (key === '0') {
          startTime = this.activityQuery.activityTime[key];
        }
        if (key === '1') {
          endTime = this.activityQuery.activityTime[key];
        }
      }

      doGet("/api/activitys", {
        current : current, //当前查询第几页
        //6个搜索条件参数
        ownerId : this.activityQuery.ownerId,
        name : this.activityQuery.name,
        startTime : startTime,
        endTime  : endTime,
        cost : this.activityQuery.cost,
        createTime : this.activityQuery.createTime
      }).then(resp => {
        if (resp.data.code === 200) {
          this.activityList = resp.data.data.list;
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })


    },

    //分页函数(current这个参数是ele-plus组件传过来，就是传的当前页)
    toPage(current) {
      this.getData(current);
    },

    //加载负责人
    loadOwner() {
      doGet("/api/user/owner", {}).then(resp => {
        if (resp.data.code === 200)  {
          this.ownerOptions = resp.data.data;
        }
      })
    },

    //搜索
    onSearch() {
      this.getData(1);
    },

    //搜索条件重置（清空）
    onReset() {
      this.activityQuery = {};
    },

    //录入市场活动
    add() {
      this.$router.push("/dashboard/activity/add");
    },

    //编辑市场活动
    edit(id){
      this.$router.push("/dashboard/activity/edit/"  + id);
    },

    //查看详情
    view(id) {
      this.$router.push("/dashboard/activity/" + id);
    },

    del(id) {
      messageConfirm("您确定要删除该数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        doGet("/api/activity/delete/" + id, {}).then(resp => {
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
      if (this.ActivityIdArray.length <= 0) {
        //提示一下
        messageTip("请选择要删除的数据", "warning");
        return;
      }
      messageConfirm("您确定要删除这些数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        let ids = this.ActivityIdArray.join(",");
       /* console.log("ActivityIdArray==============="+ this.ActivityIdArray);
        console.log("ids==============="+ids);*/
        doGet("/api/activity/batchDel", {
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
})
</script>

<style scoped>
.el-form {
  margin-bottom: 20px;
}
.el-table {
  margin-top: 12px;
}
.el-pagination {
  margin-top: 12px;
}
</style>