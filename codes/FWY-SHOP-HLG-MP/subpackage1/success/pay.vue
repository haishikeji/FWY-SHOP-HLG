<template>
  <view class="page"> 
    <img src="/static/icons/pay-success.png" alt="">
    <text class="text">支付成功，可前往我的订单查看</text>
    <view class="confirm-button" @click="back">再逛一逛</view>
    <text class="back" @click="back">返回首页</text>
  </view>
</template>
<script>
export default {
  onLoad() {},
  data() {
    return {};
  },
  methods: {
    back() {
      uni.switchTab({
        url: `/pages/index/index`
      })
    }
  },
};
</script>
<style lang="scss" scoped>
.page {
  display: flex;
  flex-direction: column;
  align-items: center;
  img {
    width: 165rpx;
    height: 191rpx;
    margin-top: 120rpx;
  }
  .text {
    font-size: 30rpx;
    color: #333;
    margin-top: 30rpx;
  }
  .confirm-button {
    width: 402rpx;
    height: 88rpx;
    font-size: 36rpx;
    margin-top: 40rpx;
  }
  .back {
    font-size: 36rpx;
    color: #00B0D0;
    margin-top: 40rpx;
  }
}
</style>
