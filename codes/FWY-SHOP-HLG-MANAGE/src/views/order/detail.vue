<template>
  <div class="page">
    <el-card class="el-card"> 订单详情 </el-card>
    <el-card class="el-card">
       <div style="font-weight: bold" v-if="type==1">售后信息</div>
      <div class="reback-info" v-if="type==1">
        <div class="list">
          <div>
          <span>申请时间：</span>
          <span>{{afterInfo.createTime}}</span>
        </div>
        <div class="reback-price">
          <div class="price">申请状态：{{stateFormat1(afterInfo.state)}}</div>
        </div>
        </div>
       <div class="list">
          <div>
          <span>申请原因：</span>
          <span>{{afterInfo.reason}}</span>
        </div>
        <div class="reback-price">
          <div class="price">审核时间：{{afterInfo.examineTime}}</div>
        </div>
        </div>
        <div class="list">
          <div class="reback-img">
          <span>图片凭证：</span>
        <div class="img-list">
          <img v-for="item in afterInfo.url" :key="item.id" :src="item" alt="">
        </div>
        </div>
        <div class="reback-price">
          <div class="price">退款金额：{{afterInfo.refundAmount||0}}元</div>
          <div style="margin:20px 0 0 30px">备注：{{afterInfo.note}}</div>
        </div>
        </div>
        
      </div>
      <div class="steps">
        <div
          class="steps_title"
          v-if="orderInfo.state == 8"
          style="color:green"
        >
          {{ stateFormat(orderInfo.state) }}
        </div>
        <div class="steps_title" v-else>{{ stateFormat(orderInfo.state) }}</div>
        <div class="steps_content">
          <el-steps :active="stateActiveFormat(orderInfo.state)">
            <el-step title="提交订单" icon="el-icon-s-order"></el-step>
            <el-step title="待发货" icon="el-icon-success"></el-step>
            <el-step title="待收货" icon="el-icon-truck"></el-step>
            <el-step title="已收货/完成" icon="el-icon-check"></el-step>
          </el-steps>
        </div>
      </div>
      <div class="user-info">
        <div class="user-info__head">
          <div style="font-weight: bold">收货人信息</div>
          <div>
            收货方式：{{
              orderInfo.deliveryMode == 0 ? '快递配送' : orderInfo.deliveryMode == 1? '到店自提':'及时送'
            }}
          </div>
        </div>
        <div class="user-info__items" v-if="orderInfo.deliveryMode == 0||orderInfo.deliveryMode == 2">
          <div>下单人： {{ orderInfo.appletUser.nickname }}</div>
          <div>收货人： {{ orderInfo.address.addressee }}</div>
          <div>联系电话：{{ orderInfo.address.contactNumber }}</div>
          <div>收货地址：{{ orderInfo.address.address }}</div>
        </div>
        <div class="user-info__items" v-else>
          <div>下单人： {{ orderInfo.appletUser.nickname }}</div>
          <div>取货人：{{ orderInfo.pickName }}</div>
          <div>联系电话：{{ orderInfo.pickPhone }}</div>
          <div>取货店铺：{{ orderInfo.shopName }}</div>
        </div>
        <div class="order-info">
          <div class="user-info__head">
            <div style="font-weight: bold">订单信息</div>
            <div class="logisticsNo" v-if="orderInfo.state == 6">
              <span>物流单号：{{ orderInfo.logisticsNo }}</span>
              <el-button
                size="mini"
                type="primary"
                @click="
                  logisticsNoDialog = true;
                  logisticsNo = '';
                "
                >更改</el-button
              >
            </div>
          </div>
          <div class="order-info__items">
            <div>订单编号：{{ orderInfo.orderNo }}</div>
            <div>下单时间：{{ orderInfo.createTime }}</div>
            <div>支付时间：{{ orderInfo.payTime }}</div>
            <div>订单状态： {{ stateFormat(orderInfo.state) }}</div>
            <div>订单金额：{{ orderInfo.totalPrice }}</div>
            <div>
              是否使用优惠券：{{ orderInfo.discountId == '' ? '否' : '是' }}
            </div>
            <div>优惠金额：{{ orderInfo.discountPrice }}</div>
            <div>实付金额：{{ orderInfo.actualPrice }}</div>
            <div>支付方式：{{ payChannel(orderInfo.payChannel) }}</div>
            <div>订单备注：{{ orderInfo.orderNote }}</div>
          </div>
        </div>
        <div class="good-info">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="name" label="商品名称" width="180">
            </el-table-column>
            <el-table-column prop="price" label="价格" width="180">
            </el-table-column>
            <el-table-column prop="count" label="数量"> </el-table-column>
            <el-table-column prop="allPrice" label="总价"> </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
    <el-dialog
      title="物流单号"
      :visible.sync="logisticsNoDialog"
      width="30%"
      :before-close="handleClose"
    >
      <el-input placeholder="请填写物流单号" v-model="logisticsNo"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="logisticsNoDialog = false">取 消</el-button>
        <el-button type="primary" @click="updateLogisticsNo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import order from '@/api/order/index';
export default {
  created() {
    this.orderId = this.$route.query.id;
    this.type=this.$route.query.type
    this.afterId=this.$route.query.afterId
    if(this.afterId){
      this.getAfterOrderInfo()
    }
    this.getDetail();
  },
  computed: {},
  data() {
    return {
      logisticsNo: '',
      logisticsNoDialog: false,
      tableData: [],
      orderId: '',
      afterId:"",
      type:"",
      afterInfo:{},
      orderInfo: {
        address: {},
        appletUser: {},
      },
    };
  },
  methods: {
    getDetail() {
      order.selectOrderInfo({ orderInfoId: this.orderId }).then((res) => {
        console.log(res);
        this.orderInfo = res.data;
        this.tableData = [
          {
            name: res.data.goodName,
            price: res.data.specsPrice,
            count: res.data.num,
            allPrice: res.data.actualPrice,
          },
        ];
      });
    },
    updateLogisticsNo() {
      order
        .updateLogisticsNoById({
          id: this.orderId,
          logisticsNo: this.logisticsNo,
        })
        .then((res) => {
          if (res.code == 200) {
            this.$message.success(res.msg);
            this.logisticsNoDialog = false
            this.getDetail();
          } else {
            this.$message(res.msg);
          }
        });
    },
    //查询售后订单详情
    getAfterOrderInfo(){
      order.getInfo({id:this.afterId}).then(res=>{
        if(res.code==200){
          this.afterInfo={...res.data,url:res.data.url.split(',')}
          console.log(this.afterInfo,'opopopop');
        }
      })
    },
    stateFormat(state) {
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
      ][state];
    },
    stateFormat1(state) {
     return ["待审核", "未通过", "已退货", "待退款", "已退款"][state];
    },
    stateActiveFormat(state) {
      return ['', 0, 0, 0, 0, 2, 3, 0, 4][state];
    },
    payChannel(state) {
      return ['', '支付宝', '微信', '银联', '线下'][state];
    },
  },
};
</script>
<style lang="less" scoped>


.reback-info{
  width: 100%;
  
  padding: 15px 0;
   border-bottom: 1px solid #ddd;
    border-top: 1px solid #ddd;
   margin: 10px 0 20px 0;
   .list{
display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
   }
  img{
    width: 80px;
    height: 80px;
    margin:0 10px;
  }
  .reback-img{
  display: flex;
  width: 50%;
}
.reback-price{
width: 50%;
}
}
.steps {
  display: flex;
  width: 100%;
  &_title {
    flex: 0 0 120px;
    height: 50px;
    text-align: center;
    line-height: 50px;
    font-size: 26px;
    border-right: 2px solid #ddd;
    margin-right: 20px;
    color: rgb(219, 44, 44);
  }
  &_content {
    flex: 1;
  }
}
.user-info {
  margin-top: 30px;
  &__head {
    width: 100%;
    height: 40px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #ddd;
    div {
      flex: 0 0 25%;
    }
    .logisticsNo {
      display: flex;
      align-items: center;
      span {
        white-space: nowrap;
        margin-right: 10px;
      }
    }
  }
  &__items {
    display: flex;
    flex-wrap: wrap;
    div {
      flex: 0 0 33%;
      margin-top: 20px;
    }
  }
}
.order-info {
  margin-top: 30px;
  &__title {
    width: 100%;
    height: 40px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #ddd;
  }
  &__items {
    display: flex;
    flex-wrap: wrap;
    div {
      flex: 1 1 33%;
      margin-top: 20px;
    }
  }
}
.good-info {
  margin-top: 30px;
}
.el-card {
  margin-bottom: 10px;
}
</style>
