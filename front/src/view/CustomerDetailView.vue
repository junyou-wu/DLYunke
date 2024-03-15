<template>
  <el-form
      ref="customerRemarkRefForm"
      :model="customerRemark"
      label-width="110px"
      :rules="editCustomerRules"
      style="max-width: 95%;">

    <el-form-item label="负责人">
      <div class="desc">{{customerDetail.ownerDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="所属活动">
      <div class="desc">{{customerDetail.activityDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="姓名">
      <div class="desc">{{customerDetail.clueDO.fullName}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="称呼">
      <div class="desc">{{customerDetail.appellationDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="手机">
      <div class="desc">{{customerDetail.clueDO.phone}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="微信">
      <div class="desc">{{customerDetail.clueDO.weixin}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="QQ">
      <div class="desc">{{customerDetail.clueDO.qq}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="邮箱">
      <div class="desc">{{customerDetail.clueDO.email}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="年龄">
      <div class="desc">{{customerDetail.clueDO.age}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="职业">
      <div class="desc">{{customerDetail.clueDO.job}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="年收入">
      <div class="desc">{{customerDetail.clueDO.yearIncome}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="住址">
      <div class="desc">{{customerDetail.clueDO.address}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="贷款">
      <div class="desc">{{customerDetail.needLoanDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="意向状态">
      <div class="desc">{{customerDetail.intentionStateDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="意向产品">
      <div class="desc">{{customerDetail.intentionProductDO.name}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="线索状态">
      <div class="desc">{{customerDetail.stateDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="线索来源">
      <div class="desc">{{customerDetail.sourceDO.typeValue}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="客户描述">
      <div class="desc">{{customerDetail.description}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="下次联系时间">
      <div class="desc">{{customerDetail.nextContactTime}}&nbsp;</div>
    </el-form-item>

    <el-form-item label="填写客户描述" prop="noteContent" name = "noteContent">
      <el-input
          v-model="customerRemark.noteContent"
          :rows="8"
          type="textarea"/>
    </el-form-item>
    <el-form-item label="跟踪方式" prop="noteWay" name = "noteWay">
      <el-select
          v-model="customerRemark.noteWay"
          placeholder="请选择跟踪方式"
          style="width: 100%"
          @click="loadDicValue('noteWay')"
          clearable>
        <el-option
            v-for="item in noteWayOptions"
            :key="item.id"
            :label="item.typeValue"
            :value="item.id"/>
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="customerRemarkSubmit">提 交</el-button>
      <el-button type="success" plain @click="goBack">返 回</el-button>
    </el-form-item>
  </el-form>

  <el-table
      :data="customerRemarkList"
      style="width: 100%">
    <el-table-column type="index" label="序号" width="60"/>
    <el-table-column prop="noteWayDO.typeValue" label="跟踪方式"/>
    <el-table-column prop="noteContent" label="跟踪内容"/>
    <el-table-column property="createTime" label="跟踪时间"/>
    <el-table-column property="createByDO.name" label="跟踪人"/>
    <el-table-column property="editTime" label="编辑时间"/>
    <el-table-column property="editByDO.name" label="编辑人"/>
    <el-table-column label="操作">
      <template #default="scope">
        <a href="javascript:" @click="edit(scope.row.id)">编辑</a>
        &nbsp;
        <a href="javascript:" @click="del(scope.row.id)">删除</a>
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
  <!--  编辑客户备注弹窗-->
  <el-dialog v-model="customerRemarkDialogVisible" title="编辑客户备注记录" width="55%" center draggable>
    <el-form ref="editCustomerRemarkRefForm" :model="editCustomerRemarkQuery" label-width="110px" :rules="editCustomerRemarkRules">
      <el-form-item label="跟踪方式" prop="noteWay">
        <el-select
            v-model="editCustomerRemarkQuery.noteWay"
            placeholder="请选择跟踪方式"
            style="width: 100%"
            @click="loadDicValue('noteWay')"
            clearable>
          <el-option
              v-for="item in noteWayOptions"
              :key="item.id"
              :label="item.typeValue"
              :value="item.id"/>
        </el-select>
      </el-form-item>

      <el-form-item label="客户备注" prop="noteContent">
        <el-input
            v-model="editCustomerRemarkQuery.noteContent"
            :rows="8"
            type="textarea"
            placeholder="请输入线索备注"/>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="customerRemarkDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="editCustomerRemarkSubmit">提 交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import {doGet, doPost} from "../http/httpRequest.js";
import {messageConfirm, messageTip} from "../utils/utils.js";

export default {
  name: "CustomerDetailView.vue",

  data() {
    return {
      //线索详情对象，初始值是空
      customerDetail: {
        ownerDO: {},
        clueDO:{},
        customerDO: {},
        activityDO: {},
        appellationDO: {},
        needLoanDO: {},
        intentionStateDO: {},
        intentionProductDO: {},
        stateDO: {},
        sourceDO: {}
      },
      //线索跟踪记录对象，初始值是空
      customerRemark: {},
      //跟踪方式的下拉选项，初始值是空
      noteWayOptions: [{}],
      //线索跟踪记录列表，初始值是空
      customerRemarkList: [{
        noteWayDO: {},
        createByDO: {},
        editByDO: {}
      }],
      //分页时每页显示多少条数据
      pageSize: 0,
      //分页总共查询出多少条数据
      total: 0,
      //编辑线索备注的弹窗，false不弹
      customerRemarkDialogVisible: false,
      editCustomerRemarkQuery: {
        noteWayOptions: []
      },
      editCustomerRules: {
        noteContent: [
          {required: true, message: '请输入线索备注', trigger: 'blur'},
          {min: 5, max: 255, message: '线索备注长度为5-255个字符', trigger: 'blur'}
        ],
        noteWay: [
          {required: true, message: '请选择跟踪方式', trigger: ['blur', 'change']},
        ],
      },
      editCustomerRemarkRules: {
        noteWay: [
          {required: true, message: '请选择跟踪方式', trigger: ['blur', 'change']},
        ],
        noteContent: [
          {required: true, message: '请输入线索备注', trigger: 'blur'},
          {min: 5, max: 255, message: '线索备注长度为5-255个字符', trigger: 'blur'}
        ]
      }
    }
  },

  mounted() {
    this.loadCustomerDetail();
    this.loadCustomerRemarkList(1);
  },

  methods: {
    goBack() {
      this.$router.go(-1)
    }
    ,
    //加载线索详情
    loadCustomerDetail() {
      let id = this.$route.params.id;
      doGet("/api/customer/detail/" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp.data.data);
          this.customerDetail = resp.data.data;
        }
      })
    },

    //提交线索跟踪记录
    customerRemarkSubmit() {
      this.$refs.customerRemarkRefForm.validate((isValid) => {
        if (isValid) {
          doPost("/api/customer/remark", {
            customerId: this.customerDetail.id,
            noteWay: this.customerRemark.noteWay,
            noteContent: this.customerRemark.noteContent,

          }).then(resp => {
            if (resp.data.code === 200) {
              messageTip("提交成功", "success");
              //刷新
              this.$router.go(0)
            } else {
              messageTip("提交失败", "error");
            }
          })
        }
      })
    },

    //加载字典数据
    loadDicValue(typeCode) {
      doGet("/api/dicValue/" + typeCode, {}).then(resp => {
        if (resp.data.code === 200) {
          if (typeCode === 'noteWay') {
            this.noteWayOptions = resp.data.data;
          } else if (typeCode === 'product') {
            this.productOptions = resp.data.data;
          }
        }
      })
    },

    //查询用户列表数据
    loadCustomerRemarkList(current) {
      doGet("/api/customers/remark", {
        current: current, //当前查询第几页,
        customerId: this.$route.params.id
      }).then(resp => {
        if (resp.data.code === 200) {
          console.log(resp.data.data)
          this.customerRemarkList = resp.data.data.list;
          this.pageSize = resp.data.data.pageSize;
          this.total = resp.data.data.total;
        }
      })
    },

    //分页函数(current这个参数是ele-plus组件传过来，就是传的当前页)
    toPage(current) {
      this.loadCustomerRemarkList(current);
    },
    edit(id){
      this.customerRemarkDialogVisible = true;
      doGet("/api/customer/remark/" + id, {}).then(resp => {
        if (resp.data.code === 200) {
          this.editCustomerRemarkQuery = resp.data.data;
          this.loadDicValue('noteWay');
        }
      })
    },
    editCustomerRemarkSubmit(){
      this.$refs.editCustomerRemarkRefForm.validate((isValid)=>{
        if (isValid) {
          doPost("/api/customer/remark/edit", {
            id : this.editCustomerRemarkQuery.id,
            customerId : this.editCustomerRemarkQuery.customerId,
            noteWay : this.editCustomerRemarkQuery.noteWay,
            noteContent: this.editCustomerRemarkQuery.noteContent
          }).then(resp => {
            if (resp.data.code === 200) {
              messageTip("编辑成功", "success");
              //刷新
              this.$router.go(0)
            } else {
              messageTip("编辑失败", "error");
            }
          })
        }
      })
    },
    del(id) {
      messageConfirm("您确定要删除该数据吗？").then(() => { //用户点击“确定”按钮就会触发then函数
        doGet("/api/customer/remark/delete/" + id, {}).then(resp => {
          console.log(resp);
          if (resp.data.code === 200) {
            messageTip("删除成功", "success");
            //页面刷新
            this.$router.go(0)
          } else {
            messageTip("删除失败，原因：" + resp.data.msg, "error");
          }
        })
      }).catch(() => { //用户点击“取消”按钮就会触发catch函数
        messageTip("取消删除", "warning");
      })
    },
  }
}
</script>

<style scoped>

</style>