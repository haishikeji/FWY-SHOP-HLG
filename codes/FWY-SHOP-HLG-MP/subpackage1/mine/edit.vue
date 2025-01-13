<template>
  <view class="page">
    <view class="func-line">
      <view class="func-line_item" @click="goToAddress">
        <text>地址管理</text>
        <view class="arrow"></view>
      </view>
    </view>
  </view>
</template>
<script>
export default {
  onLoad() {},
  data() {
    return {};
  },
  methods: {
    goToAddress() {
      uni.navigateTo({ url: `/subpackage1/address/list` });
    },
  },
};
</script>
<style lang="scss" scoped>
.func-line {
  margin-top: 20rpx;
  &_item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 100rpx;
    margin: 0 62rpx;
    border-bottom: 2rpx solid #e5e5e5;
    text {
      font-size: 28rpx;
      color: #333;
    }
    .arrow {
      border-color: #b1b1b1 #b1b1b1 transparent transparent;
    }
  }
}
</style>
