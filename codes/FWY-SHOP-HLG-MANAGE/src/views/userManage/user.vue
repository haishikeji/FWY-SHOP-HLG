<template>
  <div class="contenner">
    <el-card class="search_info">
      <el-input
        v-model="queryData.nickname"
        placeholder="用户名称"
        clearable
        size="mini"
      ></el-input>
      <el-input
        v-model="queryData.phone"
        placeholder="电话"
        clearable
        size="mini"
      ></el-input>
      <el-select
        v-model="queryData.sex"
        size="mini"
        clearable
        placeholder="请选择性别"
      >
        <el-option
          v-for="item in selectSex"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="queryData.state"
        size="mini"
        clearable
        placeholder="请选择状态"
      >
        <el-option
          v-for="item in states"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button type="primary" size="mini" @click="getRecordData"
        >查询</el-button
      >
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
        <template #bannerImgUrl="{ scope }">
          <img
            :src="scope.row.bannerImgUrl"
            style="width: 100px; heigth: 100px"
          />
        </template>
        <template #state="{ scope }">
          <el-switch
            @change="changeRecommend(scope.row)"
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
        <template #operation="{ scope }">
          <el-button
            v-if="scope.row.isShopowner === false"
            type="text"
            size="mini"
            @click="upgradeShoper(scope.row)"
          >
            升级店长
          </el-button>
          <el-button
            v-if="scope.row.isShopowner === true"
            type="text"
            size="mini"
            @click="cancelShoper(scope.row)"
          >
            取消店长
          </el-button>
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">
            查看
          </el-button>
          <!-- <el-button type="text" size="mini" @click="updateDetail(scope.row)">
            编辑
          </el-button> -->
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
        ref="ruleForm"
        :rules="Formrules"
        label-width="100px"
        :disabled="disabled"
      >
        <el-form-item label="用户名称" prop="nickname">
          <el-input
            v-model="detail.nickname"
            placeholder=""
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="detail.sex" size="small" placeholder="">
            <el-option
              v-for="item in sex"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-show="item.show"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input
            v-model="detail.phone"
            placeholder=""
            size="small"
            :disabled="true"
          ></el-input>
        </el-form-item>
      </el-form>
      <!-- <span slot="footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button size="small" type="primary" @click="updataAndAddmenu"
          >确认</el-button
        >
      </span> -->
    </el-dialog>
  </div>
</template>
<script>
import user from '@/api/userManager/user';
import role from '@/api/system/role';
export default {
  components: {
    Table: () => import('@/components/table'),
    Ueditor: () => import('@/components/UEditor'),
  },
  data() {
    return {
      sex: [
        { label: '女', value: 2, show: true },
        { label: '男', value: 1, show: true },
        { label: '未知', value: 0, show: false },
      ],
      selectSex: [
        { label: '女', value: 2, show: true },
        { label: '男', value: 1, show: true },
      ],
      states: [
        { label: '启用', value: 0 },
        { label: '禁用', value: 1 },
      ],
      treeProps: {},
      tableData: [],
      tableHead: [
        {
          label: '用户名称',
          prop: 'nickname',
          formatter: this.nameFormat,
        },
        {
          label: '性别',
          prop: 'sex',
          formatter: this.sexFormatter,
        },
        {
          label: '联系电话',
          prop: 'phone',
        },
        {
          label: '类型',
          prop: 'isShopowner',
          formatter: this.isShopowner,
        },
        {
          label: '状态',
          prop: 'state',
        },
      ],
      queryData: {
        current: 1,
        size: 10,
      },
      total: 10,
      showDialog: false,
      detail: {
        roleList: [],
      },
      disabled: false,
      title: '',
      loading: true,
      Formrules: {},
      allRoleList: [],
      updateId: '',
    };
  },
  created() {
    this.getRecordData();
  },
  methods: {
    isShopowner(row){
      if(row.isShopowner === true) {
        return '店长'
      } else {
        return '普通'
      }
    },
    /***
     * 取消店长
     */
    cancelShoper(row){
      this.$confirm('是否取消店长?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async() => {
        let {code} =  await user.updIsShopowner({
            id:row.id,
            isShopowner:false
          })
          if(code === 200) {
            this.$message({
              type: 'success',
              message: '成功!'
            });
          this.getRecordData()
          }
        }).catch(() => {
        });
    },
    /***
     * 升级店长
     */
    upgradeShoper(row){
       this.$confirm('是否升级为店长?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async() => {
        let {code} =  await user.updIsShopowner({
            id:row.id,
            isShopowner:true
          })
          if(code === 200) {
            this.$message({
              type: 'success',
              message: '成功!'
            });
          this.getRecordData()
          }
        }).catch(() => {
                  
        });
    },
    /**
     * 启用禁用
     */
    changeRecommend(row) {
      let params = {
        appletUserId: row.id,
        state: row.state,
      };
      this.$confirm('是否要改变用户状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          user.setUpState(params).then((res) => {
            if (res.code == 200) {
              this.$message.success(res.msg);
              this.getRecordData();
            } else {
              this.$message.warning(res.msg);
            }
          });
        })
        .catch(() => {
          this.getRecordData();
        });
    },
    /**
     * 详情
     */
    getUserDetailsById(id) {
      user.getUserDetailsById({ appletUserId: id }).then((res) => {
        if (res.code == 200) {
          this.detail = res.data;
        }
      });
    },
    /**
     * 查询列表
     */
    getRecordData() {
      user.getUserPage(this.queryData).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data.records;
          this.total = res.data.total;
        }
      });
    },
    /**
     * 查看详情
     */
    handleDetail(row) {
      this.getUserDetailsById(row.id);
      this.title = '查看详情';
      this.showDialog = true;
      this.disabled = true;
    },
    updateDetail(row) {
      this.title = '编辑';
      this.disabled = false;
      this.showDialog = true;
      this.updateId = row.id;
      this.getUserDetailsById(row.id);
    },
    /**
     * 关闭弹窗
     */
    closeDialog() {
      // this.resetForm('ruleForm');
      this.showDialog = false;
      this.familyDialog = false;
    },
    /**
     * 修改/新增 确认按钮
     */
    sendRequest() {
      this.submitForm('ruleForm');
    },
    /**
     * 表格数据格式化
     */
    sexFormatter(e) {
      return e.sex != null ? ['未知', '男', '女'][e.sex] : '未知';
    },
    nameFormat(e) {
      return e.nickname ? e.nickname : `用户${e.id}`;
    },
    /**
     * 清除表单验证
     */
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    updataAndAddmenu() {
      let data = {
        ...this.detail,
        id: this.updateId,
        roleList: [this.detail.roleList],
      };
      user.updateUser(data).then((res) => {
        if (res.code == 200) {
          this.$message.success('成功');
          this.getRecordData();
        } else {
          this.$message.error('失败');
        }
      });
    },
  },
  watch: {
    /**
     * 数据监测初始化
     */
    showDialog(val) {
      if (!val) {
        this.detail = {};
      }
    },
  },
};
</script>
<style lang="less" scoped></style>
