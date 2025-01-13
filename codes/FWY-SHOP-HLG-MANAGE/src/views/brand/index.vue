<template>
  <div class="page">
    <el-card>
      <div class="page-header">
        <div>品牌分类</div>
      </div>
    </el-card>
    <el-card class="container">
      <div class="select-header">
        <div class="select-item">
          <el-input
            placeholder="请输入品牌名称"
            clearable
            v-model="queryData.brandName"
            size="small"
          ></el-input>
        </div>
        <el-button type="primary" @click="lookup" size="small">查询</el-button>
        <el-button type="primary" @click="addDetail" size="small"
          >新增</el-button
        >
      </div>
      <Table
        :table-data="tableData"
        :total="total"
        :tree-props="treeProps"
        :table-head="tableHead"
        :current-page.sync="queryData.current"
        :page-size.sync="queryData.size"
        :operation-column-width="220"
        :is-show-selection="false"
        :is-show-index="true"
        :show-operation="true"
        @selection-change="selectionChange"
        @change-page="getRecordData"
      >
        <template #operation="{ scope }">
          <el-button type="text" size="mini" @click="readDetail(scope.row)">
            查看
          </el-button>
          <el-button type="text" size="mini" @click="updateDetail(scope.row)">
            编辑
          </el-button>
          <el-button type="text" size="mini" @click="deleteDetail(scope.row)">
            删除
          </el-button>
        </template>
        <template #state="{scope}">
          <el-switch
            @change="updateState(scope.row)"
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
      :title="dialogTitle"
      :visible.sync="addDialog"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
      >
        <el-form-item label="品牌名称" prop="brandName">
          <el-input
            v-model="formData.brandName"
            :disabled="disabled"
          ></el-input>
        </el-form-item>
        <el-form-item label="品牌描述">
          <el-input
            v-model="formData.brandDesc"
            type="textarea"
            :rows="4"
            :disabled="disabled"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="dialogTitle != '查看'">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="addClass">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import serve from '@/api/brand/index';
export default {
  components: {
    Table: () => import('@/components/table'),
  },
  created() {
    this.getRecordData();
  },
  data() {
    return {
      addDialog: false,
      dialogTitle: '',
      shopState: '',
      formData: {
        brandName: '',
        brandDesc: '',
      },
      tableData: [],
      total: 10,
      treeProps: {},
      tableHead: [
        {
          label: '品牌名称',
          prop: 'brandName',
        },
        {
          label: '状态',
          prop: 'brandDesc',
          formatter: this.stateFormat,
        },
      ],
      queryData: {
        current: 1,
        size: 10,
      },
      rules: {
        brandName: [
          { required: true, message: '请填写名称', trigger: 'change' },
        ],
      },
      disabled: false,
      updateId: '',
      keyword: '',
    };
  },
  methods: {
    getRecordData() {
      serve.getPage(this.queryData).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data.records;
          this.total = res.data.total;
        }
      });
    },
    updateState(row) {
      let data = {
        state: row.state,
        classificationId: row.id,
      };
      this.$confirm('确认是否要改变状态？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          serve.updateStatus(data).then((res) => {
            if (res.code == 200) {
              this.$message.success(res.msg);
              this.getRecordData();
            } else {
              this.$message.error(res.msg);
            }
          });
        })
        .catch(() => {
          this.getRecordData();
        });
    },
    readDetail(row) {
      this.addDialog = true;
      this.disabled = true;
      this.dialogTitle = '查看';
      serve.getDetail({ commodityBrandId: row.id }).then((res) => {
        this.formData = res.data;
      });
    },
    addDetail() {
      this.addDialog = true;
      this.dialogTitle = '新增';
      this.disabled = false;
      this.$refs['ruleForm'].resetFields();
      this.formData = {};
    },
    deleteDetail(row) {
      this.$confirm('确认是否要删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        serve.delete({ commodityBrandId : row.id }).then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg);
            this.getRecordData();
          } else {
            this.$message.error(res.msg);
          }
        });
      });
    },
    updateDetail(row) {
      this.addDialog = true;
      this.disabled = false;
      this.dialogTitle = '编辑';
      this.updateId = row.id;
      serve.getDetail({ commodityBrandId: row.id }).then((res) => {
        this.formData = res.data;
      });
    },
    lookup() {
      this.queryData = {
        ...this.queryData,
        name: this.keyword,
      };
      this.getRecordData();
    },
    addClass() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.dialogTitle == '编辑') {
            serve
              .update({
                ...this.formData,
              })
              .then((res) => {
                if (res.code == 200) {
                  this.$message.success(res.msg);
                  this.getRecordData();
                } else {
                  this.$message.error(res.msg);
                }
              });
          } else {
            serve.add(this.formData).then((res) => {
              if (res.code == 200) {
                this.$message.success(res.msg);
                this.getRecordData();
              } else {
                this.$message.error(res.msg);
              }
            });
          }
          this.addDialog = false;
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleClose() {
      this.$refs['ruleForm'].resetFields();
      this.addDialog = false;
    },
    stateFormat(e) {
      return e.state == 0 ? '正常' : '禁用';
    },
  },
};
</script>
<style lang="less" scoped>
.container {
  .select-header {
    display: flex;
    margin-bottom: 10px;
    .select-item {
      display: flex;
      align-items: center;
      margin-right: 15px;
      span {
        margin-right: 5px;
        white-space: nowrap;
      }
    }
  }
}
.apply {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}
.shopInfo-item {
  margin: 20px 0;
  &__title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  &__wrap {
    padding-left: 20px;
    display: flex;
    flex-wrap: wrap;
    div {
      line-height: 30px;
      flex: 0 0 50%;
    }
  }
  &__nowrap {
    padding-left: 20px;
    div {
      line-height: 30px;
    }
  }
}
</style>
