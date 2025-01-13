<template>
  <view class="page">
    <u-tabs
      class="tabs"
      ref="tabs"
      :list="tabList"
      :is-scroll="false"
      :current="current"
      active-color="#00B0D0"
      @change="tabChange"
    ></u-tabs>
    <view class="coupon-list" v-if="myCouponList.length > 0">
      <view
        class="coupon-list__item"
        v-for="item in myCouponList"
        :key="item.id"
      >
        <view class="price">
          <view>
            <text>￥</text>
            <text>{{ item.reduceAmount }}</text>
          </view>
          <view>满{{ item.fullAmount }}可用</view>
        </view>
        <view class="title">
          <view>{{ item.couponName }}</view>
          <view>有效期：{{ item.termValidity }}</view>
        </view>
        <view
          class="confirm-button"
          v-if="item.useState == 0 && item.isOverTime == false"
          @click="goToIndex"
          >去使用</view
        >
        <view
          class="confirm-button"
          style="background: #d8d8d8"
          v-if="item.useState == 1 && item.isOverTime == false"
          >已使用</view>
          <view
          class="confirm-button"
          style="background: #d8d8d8" v-if="item.isOverTime == true"
          >已过期</view>
      </view>
    </view>
    <view class="noDataContainer" v-else>
      <img src="../../static/icons/image_no_data.png" alt="" />
    </view>
  </view>
</template>
<script>
import coupon from "@/api/coupon/index";
export default {
  onLoad() {
    this.toGetCouponList();
  },
  data() {
    return {
      tabList: [{ name: "全部" }, { name: "已使用" }, { name: "未使用" }],
      current: 0,
      myCouponList: [],
      queryData: {
        currentPage: 1,
        pageSize: 999,
        useState: -1,
      },
    };
  },
  methods: {
    tabChange(index) {
      this.current = index;
      if (index == 0) {
        this.queryData.useState = -1;
      } else if (index == 1) {
        this.queryData.useState = 1;
      } else {
        this.queryData.useState = 0;
      }
      this.toGetCouponList();
    },
    goToIndex() {
      uni.switchTab({
        url: `/pages/index/index`,
      });
    },
    toGetCouponList() {
      coupon.myCoupon(this.queryData).then((res) => {
        console.log(res);
        // this.myCouponList = res.data.records;
        this.myCouponList = res.data.records.map((item) => {
          return {
            ...item,
            isOverTime:
              new Date(item.termValidity).getTime() < new Date().getTime(),
          };
        });
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.coupon-list {
  background-color: whitesmoke;
  min-height: 100vh;
  &__item {
    width: 702rpx;
    margin-left: 24rpx;
    height: 186rpx;
    background-image: url("/static/icons/coupon_bg1.png");
    background-size: 100% 100%;
    margin-bottom: 10rpx;
    position: relative;
    .price {
      position: absolute;
      left: 40rpx;
      top: 50rpx;
      view {
        color: #ec1b3b;
        &:nth-of-type(1) {
          text {
            color: #ec1b3b;
            &:nth-of-type(1) {
              font-size: 30rpx;
            }
            &:nth-of-type(2) {
              font-size: 40rpx;
            }
          }
        }
        &:nth-of-type(2) {
          font-size: 24rpx;
          margin-top: 10rpx;
        }
      }
    }
    .title {
      position: absolute;
      left: 240rpx;
      top: 50rpx;
      view {
        color: #333;
        &:nth-of-type(1) {
          font-size: 30rpx;
          font-weight: 500;
          margin-bottom: 20rpx;
        }
        &:nth-of-type(2) {
          font-size: 24rpx;
          font-weight: 400;
        }
      }
    }
    .confirm-button {
      background: linear-gradient(to right, rgb(247, 97, 77), rgb(246, 27, 59));
      width: 140rpx;
      height: 50rpx;
      font-size: 30rpx;
      position: absolute;
      right: 24rpx;
      bottom: 80rpx;
    }
  }
}
</style>
