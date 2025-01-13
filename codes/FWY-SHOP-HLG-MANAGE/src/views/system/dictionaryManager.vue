<!--
 * @Author: your name
 * @Date: 2020-07-14 15:45:31
 * @LastEditTime: 2020-10-20 16:18:18
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \jujubetwin\src\views\banner\banner.vue
-->
<template>
  <div class="contenner">
    <el-card class="search_info">
      <!-- <el-input
        v-model="queryData.fieldName"
        placeholder="字段名称"
        size="mini"
        clearable
      ></el-input> -->
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
        :isShowPage="true"
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
          <el-button
            class="cancel-button"
            type="text"
            size="mini"
            @click="deleteDetail(scope.row)"
          >
            删除
          </el-button>
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
        ref="detail"
        :rules="ruleFrom"
        label-width="100px"
        :disabled="disabled"
      >
        <el-form-item label="字典名称" prop="fieldName">
          <el-input
            v-model="detail.fieldName"
            placeholder=""
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="字典Code" prop="fieldCode">
          <el-input
            v-model="detail.fieldCode"
            placeholder=""
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="等级" prop="level">
          <el-select
            size="small"
            v-model="detail.level"
            placeholder="请选择层级"
            class="qer_input"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="说明" prop="fieldValue">
          <el-input
            v-model="detail.fieldValue"
            placeholder=""
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="detail.remark"
            placeholder=""
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="父级code"
          prop="parentCode"
          v-if="detail.level == 1"
        >
          <el-select
            size="small"
            v-model="detail.parentCode"
            placeholder="请选择父级code"
            class="qer_input"
          >
            <el-option
              v-for="item in parents"
              :key="item.fieldCode"
              :label="item.fieldName"
              :value="item.fieldCode"
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
          @click="insertAndUpdataInfo('detail')"
          >确认</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import dictionary from '@/api/system/dictionary';
export default {
  components: {
    Table: () => import('@/components/table'),
  },
  data() {
    return {
      ruleFrom: {
        fieldName: [
          { required: true, message: '请输入字典名称', trigger: 'blur' },
        ],
        fieldCode: [
          { required: true, message: '请输入字典code', trigger: 'blur' },
        ],
        level: [{ required: true, message: '请选择层级', trigger: 'blur' }],
        parentCode: [
          { required: true, message: '请输入父级code', trigger: 'blur' },
        ],
      },
      options: [
        { label: '顶级', value: 0 },
        { label: '子级', value: 1 },
      ],
      parents: [],
      name: '',
      treeProps: {
        children: 'vos',
      },
      tableData: [],
      tableHead: [
        {
          label: '字段名称',
          prop: 'fieldName',
        },
        {
          label: '编码',
          prop: 'fieldCode',
        },
        {
          label: '说明',
          prop: 'fieldValue',
        },
        {
          label: '备注',
          prop: 'remark',
        },
        {
          label: '父级code',
          prop: 'parentCode',
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
      title: '',
      checkAll: false, // 是否全选
      checkedCities: [], // 已选中的值
      cities: [],
      isIndeterminate: true,
    };
  },
  created() {
    this.getRecordData();
    this.getParents();
  },
  watch: {
    showDialog(val) {
      if (!val) {
        this.detail = {
          parentUuid: [],
        };
      }
    },
  },
  methods: {
    /**
     * 获取父级code
     */
    getParents() {
      dictionary.queryFieldByParentCode({ parentCode: 0 }).then((res) => {
        if (res.code == 200) {
          this.parents = res.data;
        }
      });
    },
    insertAndUpdataInfo(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let data = {
            fieldCode: this.detail.fieldCode,
            fieldName: this.detail.fieldName,
            fieldValue: this.detail.fieldValue,
            parentCode: this.detail.level == 0 ? 0 : this.detail.parentCode,
            fieldId: this.detail.id,
            level: this.detail.level,
            remark: this.detail.remark,
          };
          //修改
          if (data.fieldId) {
            dictionary.updateDicInfo(data).then((res) => {
              if (res.code == 200) {
                this.$message.success('成功');
                this.closeDialog();
                this.getRecordData();
                this.getParents()
              } else {
                this.$message.error('失败');
              }
            });
          } else {
            // 新增字段表
            dictionary.addDicInfo(data).then((res) => {
              if (res.code == 200) {
                this.$message.success('成功');
                this.closeDialog();
                this.getRecordData();
                this.getParents()
              } else {
                this.$message.error('失败');
              }
            });
          }
        }
      });
    },
    /**
     * 查询列表
     */
    getRecordData() {
      let data = { ...this.queryData };
      dictionary.getDicList(data).then((res) => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },
    /**
     * 新增字典
     */
    insertRole() {
      this.title = '新增字典';
      this.showDialog = true;
      this.disabled = false;
    },
    /**
     * 修改字典
     */
    updataDetail(row) {
      this.getDetals(row.id);
      this.title = '修改字典';
      this.disabled = false;
      this.showDialog = true;
    },
    /**查看字典*/
    handleDetail(row) {
      this.getDetals(row.id);
      this.title = '查看详情';
      this.disabled = true;
      this.showDialog = true;
    },
    /**
     * 查看详情
     */
    getDetals(id) {
      dictionary.queryDataDetails({ fieldId: id }).then((res) => {
        if (res.code == 200) {
          this.detail = res.data;
        }
      });
    },
    /**
     * 删除字典
     */
    deleteDetail(row) {
      let data = {
        fieldId: row.id,
      };
      this.$confirm('确认是否要删除此数据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          dictionary.deleteDicInfo(data).then((res) => {
            if (res.code == 200) {
              this.$message.success('成功');
              this.getRecordData();
            } else {
              this.$message.error('失败');
            }
          });
        })
        .catch(() => {});
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
  },
};
</script>
<style lang="less" scoped></style>
