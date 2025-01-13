<template>
  <view class="page"> 
    <view class="price">
      <text>支付</text>
      <text>￥</text>
      <text>100</text>
    </view>
    <view class="left-time">
      <view>剩余时间</view>
      <view>02:45:43</view>
    </view>
    <view class="confirm-button">确认支付</view>
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
  .price {
    margin-top: 150rpx;
    text {
      &:nth-of-type(1) {
        color: #333;
        font-size: 44rpx;
      }
      &:nth-of-type(2) {
        color: #EC1B3B;
        font-size: 44rpx;
      }
      &:nth-of-type(3) {
        color: #EC1B3B;
        font-size: 60rpx;
      }
    }
  }
  .left-time {
    view {
      &:nth-of-type(1) {
        color: #666;
        font-size: 28rpx;
        margin-top: 40rpx;
      }
      &:nth-of-type(2) {
        color: #F76950;
        font-size: 30rpx;
        margin-top: 24rpx;
      }
    }
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
