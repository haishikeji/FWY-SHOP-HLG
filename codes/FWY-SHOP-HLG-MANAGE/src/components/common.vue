<!--
 * @Author: your name
 * @Date: 2020-08-25 11:21:45
 * @LastEditTime: 2020-09-18 16:25:27
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \jujubetwin\src\views\autism\autismList.vue
-->
<template>
  <div class="contenner">
    <el-card class="search_info">
      <el-input v-model="name" placeholder="" size="mini"></el-input>
      <el-button type="primary" size="mini">查询</el-button>
      <el-button type="primary" size="mini" @click="insertRole">新增</el-button>
    </el-card>
    <el-card class="body_info">
      <!-- 表格 -->
      <Table
        :table-data="tableData"
        :total="total"
        :tree-props="treeProps"
        :table-head="tableHead"
        :current-page.sync="queryData.current"
        :page-size.sync="queryData.size"
        :operation-column-width="220"
        :is-show-selection="false"
        @selection-change="selectionChange"
        @change-page="getRecordData"
      >
        <template #operation="{ scope }">
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">
            查看
          </el-button>
          <el-button type="text" size="mini" @click="updataDetail(scope.row)">
            修改
          </el-button>
          <el-button type="text" size="mini" @click="deleteRole(scope.row)">
            删除
          </el-button>
        </template>
        <template #changeMenu="{scope}">
          <el-button type="text" size="mini" @click="updataMenu(scope.row)">
            关联菜单
          </el-button>
        </template>
      </Table>
      <el-dialog
        :title="title"
        :visible="showDialog"
        :close-on-click-modal="false"
        :before-close="closeDialog"
      >
        <el-form :model="detail" 
          ref="detail"
          :rules="FormRules" label-width="100px" :disabled="disabled">
          <el-form-item label="名称" prop="roleName">
            <el-input
              v-model="detail.roleName"
              placeholder=""
              size="small"
            ></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="roleDesc">
            <el-input
              v-model="detail.roleDesc"
              placeholder=""
              size="small"
            ></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button size="small"  @click="closeDialog">取消</el-button>
            <el-button size="small" type="primary">确认</el-button>
          </span>
      </el-dialog>
    </el-card>
  </div>
</template>
<script>
import serve from '@/api/autism/autismList'
  export default {
    components: {
      Table: () => import("@/components/table"),
    },
    data() {
      return {
        treeProps: {},
        tableData: [
          {
            name: "admin",
            roleDesc: "超级管理员",
            id: "12312321312"
          }
        ],
        tableHead: [
          {
            label: "用户名",
            prop: "roleName",
          },
          {
            label: "角色描述",
            prop: "roleDesc",
          },
          {
            label: "Id",
            prop: "id"
          },
        ],
        queryData: {
          current: 1,
          size: 10,
        },
        total: 100,
        showDialog: false,
        detail: {},
        disabled: false,
        title: "",
      }
    },
    methods:{
      closeDialog() {
        this.showDialog = false
      },
      handleDetail(row) {
         this.showDialog = true
         this.detail = { ...row }
         this.disabled = true
      },
      updataDetail(row) {
        this.showDialog = true
        this.detail = { ...row }
        this.disabled = false
      },
      deleteRole(row) {
        let data = {
          id: row.id
        }
        console.log(data)
      },
      /**
       * 查询列表
       */
      getRecordData() {
        let data = { ...this.queryData };
        console.log(data)
        serve.getAutismList(data).then(res =>{
          this.tableData = res.data.records
          this.total =res.data.total
        })
      },
      selectionChange() {
      },
    },
    created() {
      this.getRecordData()

      this.$confirm("确认是否要改变状态？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

          })
          .catch(() => {
          });
    }
}
</script>