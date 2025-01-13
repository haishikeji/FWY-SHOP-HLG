<!--
 * @Author: your name
 * @Date: 2020-07-14 15:45:31
 * @LastEditTime: 2022-03-26 13:38:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \jujubetwin\src\views\banner\banner.vue
-->
<template>
  <div class="contenner">
    <el-card style="margin-bottom: 10px">
      <div class="header">
        <div class="header__title">商品列表</div>
        <el-button type="primary" @click="addGood">添加商品</el-button>
      </div>
    </el-card>
    <el-card style="height: 80vh; overflow-y: auto">
      <div class="header__button">
        <div class="search-info">
          <div class="search-info__item">
            <el-input
              v-model="queryData.name"
              placeholder="商品名称"
              size="small"
              clearable
            ></el-input>
          </div>
          <div label="商品类型" class="search-info__item">
            <el-select
              size="small"
              clearable
              v-model="queryData.typeId"
              placeholder="商品类型"
            >
              <el-option
                v-for="item in goodTypeList"
                :key="item.id"
                :label="item.classificationName"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <div label="商品类型" class="search-info__item">
            <el-select
              size="small"
              clearable
              v-model="queryData.brandId"
              placeholder="商品品牌"
            >
              <el-option
                v-for="item in goodBrandList"
                :key="item.id"
                :label="item.brandName"
                :value="item.id"
              ></el-option>
            </el-select>
          </div>
          <el-button type="primary" size="mini" @click="serchInfo"
            >查询</el-button
          >
        </div>
        <!-- <el-button type="primary">商品导出</el-button> -->
      </div>
      <!-- 表格 -->
      <Table
        ref="table"
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
          <el-button
            v-if="userInfo.userType == 1 && scope.row.state == 1"
            type="text"
            size="mini"
            @click="updataDetail(scope.row)"
          >
            修改
          </el-button>
          <el-button
            class="cancel-button"
            type="text"
            size="mini"
            v-if="userInfo.userType == 1 && scope.row.state == 1"
            @click="deleteDetail(scope.row)"
          >
            删除
          </el-button>
        </template>
        <template #recommend="{ scope }">
          <el-switch
            @change="updateRecommend(scope.row)"
            v-model="scope.row.recommend"
            active-text="推荐"
            inactive-text="不推荐"
            active-color="#2375cc"
            inactive-color="#eee"
            :active-value="1"
            :inactive-value="0"
          >
          </el-switch>
        </template>
        <template #state="{ scope }">
          <el-switch
            @change="updateState(scope.row)"
            v-model="scope.row.state"
            active-text="上架"
            inactive-text="下架"
            active-color="#2375cc"
            inactive-color="#eee"
            :active-value="0"
            :inactive-value="1"
          >
          </el-switch>
        </template>
      </Table>
    </el-card>
  </div>
</template>
<script>
import brand from "@/api/brand/index";
import serve from "@/api/goods/index";
import goodType from "@/api/goodType/index";
export default {
  components: {
    Table: () => import("@/components/table"),
  },
  created() {
    this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
    this.tableHeadControl();
    this.getRecordData();
    this.getGoodsTypeList();
    this.getBrandList();
  },
  data() {
    return {
      userInfo: {
        userType: null,
      },
      formData: {},
      title: "",
      addDialog: false,
      parents: [],
      name: "",
      treeProps: {},
      tableData: [],
      tableHead: [
        {
          label: "商品名称",
          prop: "commodityName",
        },
        {
          label: "价格（元）",
          prop: "sellPrice",
        },
        {
          label: "库存",
          prop: "stock",
        },
        {
          label: "商品类型",
          prop: "typeName",
        },
        {
          label: "商品品牌",
          prop: "brandName",
        },
        {
          label: "所属店铺",
          prop: "shopName",
        },
        {
          label: "创建时间",
          prop: "createTime",
        },
        {
          label: "状态",
          prop: "state",
        },
        {
          label: "是否推荐",
          prop: "recommend",
        },
      ],
      queryData: {
        current: 1,
        size: 10,
        commodityType: 1,
      },
      total: 0,
      goodTypeList: [],
      goodBrandList: [],
    };
  },
  methods: {
    serchInfo() {
      this.queryData.current = 1;
      this.getRecordData();
    },
    tableHeadControl() {
      // 0为平台，1为商家
      if (this.userInfo.userType == 0) {
        this.tableHead = this.tableHead.map((item) => {
          if (["是否推荐"].includes(item.label)) {
            return {
              ...item,
              hidden: true,
            };
          } else {
            return item;
          }
        });
      } else {
        this.tableHead = this.tableHead.map((item) => {
          if (["所属店铺"].includes(item.label)) {
            return {
              ...item,
              hidden: true,
            };
          } else {
            return item;
          }
        });
      }
    },
    getRecordData() {
      serve.getPage(this.queryData).then((res) => {
        this.tableData = res.data.records;
        this.total = res.data.total;
        setTimeout(() => {
          this.$refs.table.doLayout();
        }, 1000);
      });
    },
    //获取商品类型
    getGoodsTypeList() {
      goodType.getList().then((res) => {
        this.goodTypeList = res.data;
        console.log(res);
      });
    },
    //获取商品品牌
    getBrandList() {
      brand.getList().then((res) => {
        if (res.code == 200) {
          this.goodBrandList = res.data;
        }
      });
    },
    updateRecommend(row) {
      serve
        .setUpRecommended({ commodityId: row.id, recommend: row.recommend })
        .then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg);
            this.getRecordData();
          } else {
            this.$message.error(res.msg);
            this.getRecordData();
          }
        });
    },
    updateState(row) {
      serve
        .setUpCommodityState({ commodityId: row.id, state: row.state })
        .then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg);
            this.getRecordData();
          } else {
            this.$message.error(res.msg);
            this.getRecordData();
          }
        });
    },
    addGood() {
      this.$router.push("/home/goods/addGood?mode=0");
    },
    handleDetail(row) {
      this.$router.push(`/home/goods/addGood?mode=2&id=${row.id}`);
    },
    updataDetail(row) {
      this.$router.push(`/home/goods/addGood?mode=1&id=${row.id}`);
    },
    deleteDetail(row) {
      this.$confirm("确认要删除？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        serve.delete({ commodityId: row.id }).then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg);
            this.getRecordData();
          } else {
            this.$message.error(res.msg);
          }
        });
      });
    },
    stateFormat(e) {
      return ["上架", "下架"][e.state];
    },
  },
};
</script>
<style lang="less" scoped>
.header {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.search-info {
  display: flex;
  margin-bottom: 10px;
  &__item {
    margin-right: 10px;
  }
}
.container {
  .inline-form {
    display: flex;
    flex-wrap: wrap;
    &_item {
      flex: 0 0 45%;
    }
  }
}
.add-specifications {
  width: 100%;
  display: flex;
  justify-content: space-between;
}
</style>
