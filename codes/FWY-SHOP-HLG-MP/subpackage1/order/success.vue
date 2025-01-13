<template>
  <view class="page">
    <img src="../../static/success.png" alt="">
    <text>您已成功提交售后，请等待商家审核</text>
    <view class="btn" @click="toIndex">返回首页</view>
  </view>
</template>

<script>
export default {
  methods:{
    toIndex(){
      uni.switchTab({
        url:'/pages/index/index'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.page{
  background-color: #fff;
  min-height: 100vh;
  display: flex;
  align-items: center;
  flex-direction: column;
  img{
    width: 200rpx;
    height: 200rpx;
    margin: 50rpx;
  }
  text{
    font-size: 28rpx;
  }
  .btn{
    width: 200rpx;
    height: 70rpx;
    background-color: #2BBBD4;
    color: #fff;
    text-align: center;
    line-height: 70rpx;
    border-radius: 10rpx;
    margin-top: 30rpx;
  }
}
</style>