<template>
  <div class="contenner">
    <el-card class="search_info">
      <el-input
        v-model="queryData.nickName"
        placeholder="请输入用户名称"
        size="mini"
        clearable
      ></el-input>
      <el-button type="primary" size="mini" @click="serchInfo">查询</el-button>
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
            编辑
          </el-button>
        </template>
        <template #state="{ scope }">
          <el-switch
            @change="updataUserState(scope.row)"
            v-model="scope.row.state"
            active-text="启用"
            inactive-text="禁用"
            active-color="#2375cc"
            inactive-color="#eee"
            :active-value="0"
            :inactive-value="1"
          >
          </el-switch>
        </template>
      </Table>
    </el-card>
    <el-dialog
      :title="title"
      :visible="showDialog"
      :close-on-click-modal="false"
      :before-close="closeDialog"
    >
      <el-form
        :model="detail"
        label-width="130px"
        :disabled="disabled"
        ref="detail"
        :rules="loginFormRules"
      >
        <el-form-item label="用户名称" prop="nickname">
          <el-input
            v-model.trim="detail.nickname"
            placeholder=""
            size="small"
            class="qer_input"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="detail.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input
            v-model.trim="detail.phone"
            placeholder=""
            size="small"
            class="qer_input"
          ></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="loginName">
          <el-input
            v-model.trim="detail.loginName"
            placeholder=""
            size="small"
            class="qer_input"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" v-if="title == '新增用户'" prop="password">
          <el-input
            type="password"
            v-model.trim="detail.password"
            placeholder=""
            size="small"
            class="qer_input"
          ></el-input>
        </el-form-item>
        <el-form-item label="关联角色" prop="roleList">
          <el-select
            size="small"
            v-model="detail.roleList"
            placeholder="请选择关联角色"
            class="qer_input"
          >
            <el-option
              v-for="item in allRoleList"
              :key="item.id"
              :label="item.roleName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          @click="updataAndAddUser('detail')"
          >确认</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import user from "@/api/system/user";
import role from "@/api/system/role";
export default {
  components: {
    Table: () => import("@/components/table"),
    // upload:() => import("@/components/upload/uploadIndex")
  },
  data() {
    return {
      loginFormRules: {
        loginName: [
          { required: true, message: "请输入登录账号", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        nickname: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        phone: [{ required: true, message: "请输入手机号", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        roleList: [
          { required: true, message: "请选择用户角色", trigger: "blur" },
        ],
      },
      imageUrl: "",
      treeProps: {},
      tableData: [],
      tableHead: [
        {
          label: "用户名称",
          prop: "nickname",
        },
        {
          label: "用户账号",
          prop: "loginName",
        },
        {
          label: "电话",
          prop: "phone",
        },
        {
          label: "角色",
          prop: "userType",
          formatter: this.userTypeFormat,
        },
        {
          label: "状态",
          prop: "state",
        },
      ],
      queryData: {
        current: 1,
        size: 10,
        nickName: null,
      },
      total: 100,
      showDialog: false,
      detail: {
        nickname:"",
        phone:"",
        loginName:"",
        password:"",
      },
      disabled: false,
      title: "",
      checkedCities: [], // 已选中的值
      allRoleList: [], // 所有角色
      check: {},
      options: [],
    };
  },
  created() {
    this.getRecordData();
    this.getAllRole();
  },
  watch: {
    showDialog(val) {
      if (!val) {
        this.detail = {
          userType: "ADMIN",
        };
        this.checkedCities = [];
      }
    },
  },
  methods: {
    formatter(e) {
      return e.state == "0" ? (
        <span class="success">已启用</span>
      ) : (
        <span class="danger">已禁用</span>
      );
    },
    /**
     * 搜索用户列表
     */
    serchInfo() {
      this.queryData.page = 1;
      this.getRecordData();
    },
    /**
     * 查询列表
     */
    getRecordData() {
      let data = { ...this.queryData };
      user.queryAllUserPage(data).then((res) => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },
    /**
     * 修改用户状态
     */
    updataUserState(row) {
      let data = {
        state: row.state,
        userId: row.id,
      };
      this.$confirm("确认是否要改变状态？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          user.changeDisabled(data).then((res) => {
            if (res.code == 200) {
              this.$message.success(res.msg);
              this.getRecordData();
              this.showDialog = false;
            } else {
              this.$message.success(res.msg);
            }
          });
        })
        .catch(() => {
          this.getRecordData();
        });
    },
    /**
     * 新增用户
     */
    insertRole() {
      this.title = "新增用户";
      this.showDialog = true;
      this.disabled = false;
       this.$refs.detail.resetFields();
    },
    updataAndAddUser(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let data = this.detail;
          data.roleList = [this.detail.roleList];
          if (data.id) {
            //修改
            // delete data.createTime;
            // delete data.updateTime;
            // delete data.createId;
            // delete data.create_id;
            // data.userId = data.id;
            // delete data.state;
            // delete data.id;
            user.updateUser(data).then((res) => {
              if (res.code == 200) {
                this.$message.success(res.msg);
                this.getRecordData();
                this.showDialog = false;
              } else {
                this.$message.error(res.msg);
              }
            });
          } else {
            if (!data.password) {
              this.$message.warning("请输入密码");
              return;
            }
            user.addUserInfo(data).then((res) => {
              if (res.code == 200) {
                this.$message.success(res.msg);
                this.getRecordData();
                this.showDialog = false;
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
    },
    /**
     * 修改用户
     */
    updataDetail(row) {
      this.queryUserById(row);
      this.title = "修改用户";
      this.disabled = false;
      this.showDialog = true;
       this.$refs.detail.resetFields();
    },
    /**查看用户*/
    handleDetail(row) {
      this.title = "查看详情";
      this.queryUserById(row);
      this.disabled = true;
      this.showDialog = true;
      this.$refs.detail.resetFields();
    },
    /**
     * 查询用户信息
     */
    queryUserById(row) {
      let data = {
        userId: row.id,
      };
      user.queryUserById(data).then((res) => {
        if (res.code == 200) {
          this.detail = res.data;
        }
        this.detail.roleList = res.data.roleList[0];
        this.detail.deptId = this.detail.deptId == 0 ? "" : this.detail.deptId;
        // this.detail.dept = res.data.department
        // this.detail.password = ''
        // this.checkedCities = []
        // this.checkedCities.push(res.data.roleId)
      });
    },
    /**
     * 关闭弹窗
     */
    closeDialog() {
      this.showDialog = false;
    },
    /**
     * 多选框改变事件
     */
    selectionChange() {},
    /**
     * 获取当前用户所有启用的角色
     */
    getAllRole() {
      role.getRoleList().then((res) => {
        if (res.code == 200) {
          this.allRoleList = res.data.records;
        }
      });
    },
    userTypeFormat(e) {
      return ["平台", "商户"][e.userType];
    },
  },
};
</script>
<style lang="less" scoped>
.el-form {
  width: 80%;
  margin: 0 auto;
}
.qer_input {
  width: 80%;
}
</style>
