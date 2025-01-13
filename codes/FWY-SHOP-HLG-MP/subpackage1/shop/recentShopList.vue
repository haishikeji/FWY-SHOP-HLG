<template>
  <view class="page">
    <view class="msg-list" v-if="defaultShop || shopList.length > 0">
      <u-radio-group v-model="checkedRadio" @change="radioChange">
        <view>
          <text>当前门店</text>
          <view class="msg-list__item">
            <view class="container">
              <view class="header">
                <view class="title">
                  <view class="img">
                    <img src="../../static/shop2.png" alt="" />
                  </view>
                  <view class="goodType">{{ defaultShop.shopName }}</view>
                </view>
                <view class="distance">
                  <!-- <view class="radio">
                    <u-radio shape="circle" name="current"></u-radio>
                  </view> -->
                  <!-- <view>距您1111km</view> -->
                </view>
              </view>
              <text class="content"
                >{{ defaultShop.province }}{{ defaultShop.region
                }}{{ defaultShop.shopAddress }}</text
              >
            </view>
          </view>
        </view>
        <text>附近门店</text>
        <view
          class="msg-list__item"
          v-for="item in shopList"
          :key="item.shopId"
          @click="toGoTimely(item.shopId)"
        >
          <view class="container">
            <view class="header">
              <view class="title">
                <view class="img">
                  <img src="../../static/shop2.png" alt="" />
                </view>
                <view class="goodType">{{ item.shopName }}</view>
              </view>
              <view class="distance">
                <view class="radio">
                  <u-radio shape="circle" :name="item.shopId"></u-radio>
                  <view class="clearFloat"></view>
                </view>
                <view>距您{{ item.distance }}km</view>
              </view>
            </view>
            <view class="content">{{ item.shopAddress }}</view>
          </view>
        </view>
      </u-radio-group>
    </view>
  </view>
</template>

<script>
import timely from "@/api/timely/index";
import shop from "@/api/shop/index";
export default {
  onLoad(param) {
    uni.showLoading()
    this.shopId = param.id;
    this.checkedRadio = this.shopId;
    this.getRecentShops();
    this.getShopInfo();
  },
  data() {
    return {
      queryData: {
        city: "",
        dimension: "",
        latitude: "",
      },
      shopList: [],
      checkedRadio: "current",
      shopId: "",
      defaultShop: null,
    };
  },
  methods: {
    getShopInfo() {
      shop.getShopInfo({ shopId: this.shopId }).then((res) => {
        this.defaultShop = res.data.shopInfo;
        uni.hideLoading()
      });
    },
    getRecentShops() {
      uni.getStorage({
        key: "location",
        success: (res) => {
          this.queryData = {
            city: res.data.city,
            longitude: res.data.longitude,
            latitude: res.data.latitude,
          };
          timely.getRecentShops(this.queryData).then((res) => {
            this.shopList = res.data;
            uni.hideLoading()
          });
        },
      });
    },
    toGoTimely(id) {
      this.checkedRadio = id;
      uni.setStorage({
        key: `timelyShopId`,
        data: id,
        success: () => {
          uni.switchTab({
            url: `/pages/index/timely`,
          });
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.page {
  .msg-list {
    background-color: whitesmoke;
    min-height: 100vh;
    padding: 10rpx;
    &__item {
      width: 100vw;
      display: flex;
      width: 95%;
      margin: 20rpx;
      padding: 30rpx;
      border-radius: 10rpx;
      background-color: white;
      .container {
        display: flex;
        flex-direction: column;
        .header {
          width: 480rpx;
          display: flex;
          .title {
            display: flex;
            img {
              width: 50rpx;
              height: 50rpx;
            }
            .goodType {
              padding: 8rpx;
              font-weight: bolder;
            }
          }
          .distance {
            position: absolute;
            right: 40rpx;
            font-size: 28rpx;
            color: #999;
            padding: 8rpx;
          }
          .clearFloat {
            clear: both;
          }
          .radio {
            margin-left: 68rpx;
          }
        }
        .content {
          margin-top: 20rpx;
          margin-left: 60rpx;
          color: #666666;
          width: 400rpx;
          text-overflow: -o-ellipsis-lastline;
          overflow: hidden;
        }
      }
    }
  }
  .no-data {
    width: 100vw;
    height: 100vh;
  }
}
</style>