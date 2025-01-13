<template>
  <view class="page">
    <view class="container">
      <img class="line" src="/static/icons/line.png" alt="" />
      <view class="receive">
        <view v-if="orderInfo.deliveryMode == 1">
          <view class="receive__address">
            <text style="width: 140rpx;">店铺地址:</text>
            <text>{{ orderInfo.shopAddress }}</text>
          </view>
          <view class="receive__user">
            <text>取货人:</text>
            <text class="receive__user_layout">
              <text style="margin-right: 20rpx">{{ orderInfo.pickName }}</text>
              <text>{{ orderInfo.pickPhone }}</text>
            </text>
          </view>
        </view>
        <view v-else>
          <view class="receive__address">
            <text style="width: 140rpx;">收货地址:</text>
            <text>{{ orderInfo.address.address }}</text>
          </view>
          <view class="receive__user">
            <text>收货人:</text>
            <text class="receive__user_layout">
              <text style="margin-right: 20rpx">{{
                orderInfo.address.addressee
              }}</text>
              <text>{{ orderInfo.address.contactNumber }}</text>
            </text>
          </view>
        </view>
        <view class="receive__method">
          <view class="receive__method_layout">
            <text>送货方式:</text>
            <text>{{
              orderInfo.deliveryMode == 0
                ? "快递配送"
                : orderInfo.deliveryMode == 1
                ? "到店自提"
                : "及时送"
            }}</text>
          </view>
          <!-- <view class="receive__method_time">{{
            orderInfo.receivingTime
          }}</view> -->
        </view>
      </view>
      <view class="good-info">
        <view class="shop-info">
          <view class="shop-info_layout">
            <img class="shop_ic" src="/static/icons/11.png" alt="" />
            <text class="shop_name">{{ orderInfo.shopName }}</text>
          </view>
          <view class="order-state">{{ stateFormat(orderInfo.state) }}</view>
        </view>
        <view class="good-list">
          <view class="good-list__item">
            <view class="good-layout">
              <img class="good-img" :src="orderInfo.goodPicture" alt="" />
              <view class="good-list__item_info">
                <view class="good-name" style="color: #333; font-size: 28rpx">{{
                  orderInfo.goodName
                }}</view>
                <view class="price-layout">
                  <text style="color: #333; font-size: 28rpx">￥</text>
                  <text style="color: #333; font-size: 32rpx">{{
                    orderInfo.specsPrice
                  }}</text>
                </view>
              </view>
            </view>
            <view class="good-list__item_count"> x{{ orderInfo.num }} </view>
            <view class="good-list__item_all-price">
              <text>￥</text>
              <text>{{ orderInfo.totalPrice }}</text>
            </view>
          </view>
        </view>
      </view>
      <view class="order-info">
        <view class="order-info__item">
          <text>订单编号:</text>
          <text>{{ orderInfo.orderNo }}</text>
        </view>
        <view class="order-info__item">
          <text>下单时间:</text>
          <text>{{ orderInfo.createTime }}</text>
        </view>
        <view class="order-info__item">
          <text>支付时间:</text>
          <text>{{ orderInfo.payTime == null ? "" : orderInfo.payTime }}</text>
        </view>
      </view>
      <view class="order-info" style="margin-top: 30rpx">
        <view class="order-info__item">
          <text>商品金额:</text>
          <text>
            <text style="font-size: 24rpx">￥</text>
            <text style="font-size: 30rpx">{{ orderInfo.totalPrice }}</text>
          </text>
        </view>
        <!-- <view class="order-info__item">
          <text>配送费</text>
          <text>
            <text style="font-size: 22rpx">￥</text>
            <text style="font-size: 28rpx">5.00</text>
          </text>
        </view> -->
        <view class="order-info__item">
          <text>优惠规则:</text>
          <view class="rule-tag">
            <u-tag
              text="优惠券"
              bg-color="#fff"
              border-color="#EC1B3B"
              color="#EC1B3B"
              size="mini"
            />
            <text>
              <text>-￥</text>
              <text>{{ orderInfo.discountPrice }}</text>
            </text>
          </view>
        </view>
      </view>
      <view class="all-price">
        <text>共计</text>
        <text>￥</text>
        <text>{{ orderInfo.actualPrice }}</text>
      </view>

      <template>
        <view class="time-line" v-if="orderInfo.state == 6">
          <u-time-line>
            <u-time-line-item>
              <!-- 此处没有自定义左边的内容，会默认显示一个点 -->
              <template v-slot:content>
                <view v-for="item in logisticsList" :key="item.id">
                  <view class="u-order-desc">{{ item.context }}</view>
                  <view class="u-order-time">{{ item.time }}</view>
                </view>
              </template>
            </u-time-line-item>
          </u-time-line>
        </view>
      </template></view
    ></view
  >
</template>
    </view>
    <!-- <view class="bottom-button">
      <img src="/static/icons/9.png" />
      <view class="bottom-button_layout">
        <view class="button" v-if="[1,4].includes(orderInfo.state)">取消订单</view>
        <view class="button" v-if="false">申请退货</view>
        <view class="button" v-if="false">查看物流</view>
        <view class="button height-light" v-if="orderInfo.state == 1">立即支付</view>
        <view class="button height-light" v-if="false">确认收货</view>
      </view>
    </view> -->


<script>
import serve from "@/api/order/index";
export default {
  onLoad(param) {
    this.orderId = param.id;
    this.getDetail();
    this.findOrder();
  },
  data() {
    return {
      orderInfo: {},
      orderId: "",
      logisticsList: [],
    };
  },
  methods: {
    getDetail() {
      serve.selectOrderInfo({ orderInfoId: this.orderId }).then((res) => {
        console.log(res.data,'价格')
        this.orderInfo = res.data;
      });
    },
    // 查询物流详情
    findOrder() {
      serve.findOrder({ orderId: this.orderInfo.logisticsNo }).then((res) => {
        this.logisticsList = JSON.parse(res.data).data;
      });
    },
    stateFormat(state) {
      return [
        "",
        "待付款",
        "付款中",
        "支付失败",
        "取消支付",
        "已付款",
        "已售后",
        "已退款",
        "确认收货",
        "已完成",
        "已完成",
      ][state-1];	
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  height: calc(100vh - 98rpx);
  overflow-y: auto;
  .line {
    width: 100vw;
    height: 10rpx;
    margin-top: 20rpx;
  }
  .receive {
    width: 702rpx;
    margin-left: 24rpx;
    padding: 24rpx;
    text {
      font-size: 30rpx;
      color: #333;
    }
    &__address {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 24rpx;
    }
    &__user {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 24rpx;
      &_layout {
        display: flex;
        align-items: center;
      }
    }
    &__method {
      margin-top: 24rpx;
      border-top: 2rpx solid #e5e5e5;
      padding-top: 24rpx;
      &_layout {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      &_time {
        width: 100%;
        text-align: end;
        font-size: 30rpx;
        color: #333;
      }
    }
  }
  .good-info {
    width: 100vw;
    padding: 24rpx 48rpx;
    .shop-info {
      line-height: 70rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      &_layout {
        display: flex;
        align-items: center;
        .shop_ic {
          width: 32rpx;
          height: 32rpx;
        }
        .shop_name {
          font-size: 28rpx;
          color: #333;
          margin-left: 12rpx;
        }
      }
      .order-state {
        font-size: 26rpx;
        color: #f76950;
      }
    }
    .good-list {
      &__item {
        height: 144rpx;
        margin: 40rpx 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: relative;
        .good-layout {
          display: flex;
          align-items: center;
          .good-name {
            width: 350rpx;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2; //控制行数
            overflow: hidden;
          }
          .good-img {
            width: 144rpx;
            height: 144rpx;
            margin-right: 20rpx;
          }
          .good-list__item_info {
            align-self: flex-start;
            margin-top: 17rpx;
          }
          .price-layout {
            margin-top: 10rpx;
          }
        }
        &_count {
          color: #888;
          font-size: 28rpx;
          position: absolute;
          right: 0;
          bottom: 50rpx;
        }
        &_all-price {
          color: #333;
          align-self: flex-end;
          text {
            &:nth-of-type(1) {
              font-size: 24rpx;
              font-weight: 600;
            }
            &:nth-of-type(2) {
              font-size: 30rpx;
              font-weight: 600;
            }
          }
        }
      }
    }
    .message {
      font-size: 28rpx;
      color: #333;
    }
  }
  .order-info {
    width: 702rpx;
    margin-left: 24rpx;
    padding: 0 20rpx;
    padding-bottom: 30rpx;
    border-bottom: 2rpx solid #e5e5e5;
    &__item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      line-height: 50rpx;
      text {
        font-size: 28rpx;
        color: #333;
      }
      .rule-tag {
        display: flex;
        align-items: center;
        & > text {
          margin-left: 20rpx;
        }
      }
    }
  }
  .all-price {
    width: 702rpx;
    margin-left: 24rpx;
    margin-bottom: 30rpx;
    height: 50rpx;
    display: flex;
    align-items: flex-end;
    justify-content: flex-end;
    text {
      &:nth-of-type(1) {
        color: #333;
        font-size: 24rpx;
      }
      &:nth-of-type(2) {
        color: #ec1b3b;
        font-size: 28rpx;
      }
      &:nth-of-type(3) {
        color: #ec1b3b;
        font-size: 36rpx;
      }
    }
  }
}
.bottom-button {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 98rpx;
  width: 100vw;
  padding: 0 24rpx;
  position: fixed;
  bottom: 0;
  left: 0;
  img {
    width: 46rpx;
    height: 46rpx;
  }
  &_layout {
    display: flex;
    .button {
      width: 170rpx;
      height: 68rpx;
      border: 2rpx solid #e5e5e5;
      color: #333;
      border-radius: 28rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-left: 20rpx;
    }
    .height-light {
      color: #00b0d0;
      border-color: #00b0d0;
    }
  }
}

.time-line {
  padding: 20rpx;
  .u-order-title {
    color: #333333;
    font-weight: bold;
    font-size: 32rpx;
  }

  .u-order-desc {
    color: rgb(150, 150, 150);
    font-size: 28rpx;
    margin-bottom: 10rpx;
  }

  .u-order-time {
    color: rgb(200, 200, 200);
    font-size: 26rpx;
    margin-bottom: 18rpx;
  }
}
</style>
