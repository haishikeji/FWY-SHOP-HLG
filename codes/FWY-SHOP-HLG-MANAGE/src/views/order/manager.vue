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
    <el-card style="margin-bottom: 10px">
      <div class="header">
        <div class="header__title">订单管理</div>
      </div>
    </el-card>
    <el-card>
      <div class="search-info">
        <div class="search-info__item">
          <el-input
            v-model="queryData.keywords"
            placeholder="用户名称"
            size="small"
            clearable
          ></el-input>
        </div>
        <div class="search-info__item">
          <el-select
            v-model="queryData.state"
            placeholder="订单状态"
            size="small"
            clearable
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <el-button type="primary" size="mini" @click="getRecordData"
          >查询</el-button
        >
      </div>
      <!-- 表格 -->
      <Table
        :table-data="tableData"
        :total="total"
        :tree-props="treeProps"
        :table-head="tableHead"
        :current-page.sync="queryData.currentPage"
        :page-size.sync="queryData.pageSize"
        :operation-column-width="220"
        :is-show-selection="false"
        :isShowPage="true"
        @selection-change="selectionChange"
        @change-page="getRecordData"
      >
        <template #operation="{ scope }">
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">
            详情
          </el-button>
          <el-button
            type="text"
            size="mini"
            v-if="
              (scope.row.goodsType != 4 &&
                scope.row.state == 5 &&
                userInfo.userType == 1) ||
              (scope.row.goodsType == 4 &&
                scope.row.state == 5 &&
                userInfo.userType == 1 &&
                scope.row.userNumJoin == scope.row.userNumTotal)
            "
            @click="openDialog(scope.row)"
          >
            去发货
          </el-button>
          <el-button
            type="text"
            size="mini"
            v-if="scope.row.state == 6"
            @click="openLogistics(scope.row)"
          >
            查看物流
          </el-button>
        </template>
      </Table>
    </el-card>
    <el-dialog
      title="发货"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-input placeholder="请填写物流单号" v-model="logisticsNo"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="toDeliverGoods">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="查看物流"
      :visible.sync="logisticsDialog"
      width="50%"
      :before-close="handleClose"
    >
      <el-timeline v-if="logisticsInfo.data.length > 0">
        <el-timeline-item
          v-for="(item, index) in logisticsInfo.data"
          :key="index"
          :timestamp="item.time"
        >
          {{ item.context }}
        </el-timeline-item>
      </el-timeline>
      <div v-else>{{ logisticsMsg }}</div>
    </el-dialog>
  </div>
</template>
<script>
import order from '@/api/order/index'
export default {
  components: {
    Table: () => import('@/components/table'),
    upload: () => import('@/components/upload/uploadIndex'),
  },
  created() {
    this.userInfo = JSON.parse(sessionStorage.getItem('userInfo'))
    this.getRecordData()
  },
  data() {
    return {
      logisticsMsg: '',
      logisticsDialog: false,
      logisticsInfo: {
        data: [],
      },
      logisticsNo: '',
      orderId: '',
      dialogVisible: false,
      formData: {},
      title: '',
      addDialog: false,
      parents: [],
      treeProps: {},
      tableData: [],
      userInfo: '',
      tableHead: [
        {
          label: '订单编号',
          prop: 'orderNo',
        },
        {
          label: '收货人',
          prop: 'address.addressee',
        },
        {
          label: '联系方式',
          prop: 'address.contactNumber',
        },
        {
          label: '商品名称',
          prop: 'goodName',
        },
        {
          label: '数量',
          prop: 'num',
        },
        {
          label: '订单金额',
          prop: 'totalPrice',
        },
        {
          label: '实付金额',
          prop: 'actualPrice',
        },
        {
          label: '订单状态',
          prop: 'state',
          formatter: this.stateFormat,
        },
        {
          label: '支付时间',
          prop: 'payTime',
        },
        {
          label: '下单人',
          prop: 'appletUser.nickname',
        },
        {
          label: '商品类型',
          prop: 'goodsType',
          formatter: this.typeFormat,
          width: 200,
        },
        {
          label: '店铺名称',
          prop: 'shopName',
        },
      ],
      queryData: {
        currentPage: 1,
        pageSize: 10,
        deliveryMode: 0,
      },
      total: 0,
      options: [
        {
          value: 1,
          label: '待付款',
        },
        {
          value: 2,
          label: '付款中',
        },
        {
          value: 3,
          label: '支付超时',
        },
        {
          value: 4,
          label: '取消支付',
        },
        {
          value: 5,
          label: '已付款',
        },
        {
          value: 6,
          label: '已发货',
        },
        {
          value: 7,
          label: '已退款',
        },
        {
          value: 8,
          label: '确认收货',
        },
      ],
    }
  },
  methods: {
    getRecordData() {
      order.selectOrderList(this.queryData).then((res) => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    toDeliverGoods() {
      order
        .toDeliverGoods({ id: this.orderId, logisticsNo: this.logisticsNo })
        .then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg)
            this.dialogVisible = false
            this.getRecordData()
          } else {
            this.$message(res.msg)
          }
        })
    },
    handleDetail(row) {
      this.$router.push(`/home/order/detail?id=${row.id}`)
    },
    openDialog(row) {
      this.logisticsNo = ''
      this.dialogVisible = true
      this.orderId = row.id
    },
    openLogistics(row) {
      this.logisticsDialog = true
      this.logisticsMsg = ''
      order.findOrder({ orderId: row.logisticsNo }).then((res) => {
        if (res.code == 200) {
          this.logisticsInfo = JSON.parse(res.data)
        } else {
          this.logisticsMsg = res.msg
          this.$message(res.msg)
        }
      })
    },
    stateFormat(e) {
      return [
        '',
        '待付款',
        '付款中',
        '支付超时',
        '取消支付',
        '已付款',
        '已发货',
        '已退款',
        '确认收货',
      ][e.state]
    },
    typeFormat(e) {
      console.log(e, 123)

      let shopGoodsName =
        '拼团商品(' + (e.userNumJoin || 0) + '/' + (e.userNumTotal || 0) + ')'
      return ['', '普通商品', '秒杀商品', '预售商品', shopGoodsName][
        e.goodsType
      ]
    },
  },
}
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
