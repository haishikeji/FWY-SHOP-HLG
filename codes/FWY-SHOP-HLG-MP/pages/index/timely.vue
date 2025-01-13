<template>
  <view class="page">
    <view class="noDataContainer" v-if="code == 7002 || !shopInfo">
      <img src="../../static/icons/image_no_data.png" alt="" />
    </view>
    <view class="container" v-if="shopInfo">
      <view class="top-select">
        <view style="display: flex; align-items: center">
          <u-icon name="map" color="#333" size="40"></u-icon>
          <text style="color: #333; fong-size: 30rpx">{{
            queryData.city
          }}</text>
        </view>
        <view class="search-input" @click="toSearchGoods(shopId)">
          <u-icon slot="icon" name="search" color="#999999" size="32"></u-icon>
         <view class="input">搜索店铺内商品</view>
        </view>
        <img src="@/static/shop.png" alt="" @click="goToShopList(shopId)" />
      </view>
      <!-- <view class="tips">
        <view>配送时间：9:00—12:00</view>
        <view>配送范围：8km以内实时配送，超出8km走正常物流</view>
      </view> -->
      <template>
        <img class="bg-img" :src="shopInfo.shopBackground" alt="" />
        <view class="shop-info">
          <img class="shop-img" :src="shopInfo.shopBackground" alt="" />
          <view class="shop-name">{{shopInfo.brandName}}&nbsp;{{ shopInfo.shopName }}</view>
          <view class="shop-distence">
            <u-icon name="map" color="#333" size="40"></u-icon>
            <text style="color: #333; fong-size: 30rpx"
              >距您{{ recentShop.distance }}km</text
            >
          </view>
          <view class="time">营业时间：{{ shopInfo.businessHours }}</view>
          <view class="shop-address"
            >地址：{{ shopInfo.province }}{{ shopInfo.region
            }}{{ shopInfo.shopAddress }}</view
          >
        </view>
        <view class="shopRecommend">
          <view class="shopRecommend-title">
            <view class="title">
              <img src="/static/icons/6.png" />
              <text>商家推荐</text>
            </view>
          </view>
          <view class="shopRecommend-list" v-if="shopCommodityList.length != 0">
            <view
              class="shopRecommend-list__item"
              v-for="item in shopCommodityList"
              :key="item.id"
              @click="goToDetail(item.id)"
            >
              <img :src="item.coverUrl" alt="" />
              <text class="name">{{ item.commodityName }}</text>
              <text class="price" v-if="userInfo.isShopowner && item.shopownerPrice !== 0">￥{{ item.shopownerPrice }}</text>
              <text class="price" v-else>￥{{ item.sellPrice }}</text>
            </view>
          </view>
          <view class="no-data" v-else style="height: 250rpx">暂无推荐</view>
        </view>
      </template>
      <view class="coupon" v-if="couponList.length != 0">
        <view class="coupon-head">
          <view class="title">
            <img src="/static/icons/10.png" />
            <text>优惠券</text>
          </view>
          <view class="more" @click="goToCoupon">
            <view>更多</view>
            <view class="arrow"></view>
          </view>
        </view>
        <view class="coupon-list">
          <view
            class="coupon-list__item"
            v-for="item in couponList"
            :key="item.id"
          >
            <view class="coupon-count"
              ><text>￥</text><text>{{ item.reduceAmount }}</text></view
            >
            <view class="coupon-primise">满{{ item.fullAmount }}可用</view>
            <view
              class="receive"
              @click="receiveCoupon(item.id)"
              v-if="!item.isReceive"
              >领取</view
            >
            <view class="receive" v-else style="color: #d8d8d8">已领</view>
            <view class="cycle">
              <view></view>
              <view></view>
              <view></view>
              <view></view>
            </view>
          </view>
        </view>
      </view>
      <view class="recommend">
        <view class="recommend-title" style="justify-content: flex-start">
          <img src="/static/icons/8.png" />
          <text>全部商品</text>
        </view>
        <view class="recommend-list" v-if="shopAllGoods.length > 0">
          <view
            class="recommend-list__item"
            v-for="item in shopAllGoods"
            :key="item.id"
            @click="goToDetail(item.id)"
          >
            <img :src="item.coverUrl" alt="" />
            <text class="name">{{ item.commodityName }}</text>
            <text class="price" v-if="userInfo.isShopowner && item.shopownerPrice !== 0">￥{{ item.shopownerPrice }}</text>
            <text class="price" v-else>￥{{ item.sellPrice }}</text>
          </view>
        </view>
      </view>
    </view>
    <u-tabbar
      :list="tabbar"
      :mid-button="false"
      active-color="#1EA8C1"
    ></u-tabbar>
  </view>
</template>
<script>
import timely from '@/api/timely/index';
import shop from '@/api/shop/index';
import good from '@/api/good/index';
import coupon from '@/api/coupon/index';
const QQMapWX = require('../../util/map/qqmap-wx-jssdk.js');
export default {

  onLoad() {
    this.tabbar = this.$store.state.tabbarList;
    uni.showLoading();
    this.getCouponList();
    this.getTimelyShop();
  },
  onReachBottom() {
   
	if(this.size<this.total){
		this.size+=3;
		 this.getpageBy();
	}
	
  },
  onShow() {
    this.isAuthorizeInfo();
    this.getLocation();
    this.getDeliveryTime()
    if (uni.getStorageSync('timelyShopId')) {
      this.shopId = uni.getStorageSync('timelyShopId');
      this.getShopAllInfo(this.shopId);
    }
  },

  //当非从附近商家列表跳入到及时送界面时，删除缓存的shopId
  onHide() {
    uni.removeStorageSync('timelyShopId');
  },
  data() {
    return {
      tabbar: [],
      queryData: {
        city: '',
        dimension: '',
        latitude: '',
      },
      recentShop: '',
      shopInfo: null,
      shopCommodityList: '',
      shopAllGoods: [],
      couponList: [],
      code: '',
      shopId: '',
	  size:4,
	  current:1,
	  total:'',
	  id:'',
      userInfo:uni.getStorageSync('userInfo'),
    };
  },
  methods: {
    // goToShop(id) {
    //   uni.navigateTo({ url: `/subpackage1/shop/index?id=${id}` });
    // },
    goToCoupon() {
      uni.navigateTo({ url: `/subpackage1/coupon/coupon` });
    },
    // 获取优惠券列表
    getCouponList() {
      coupon.queryAppCouponPage({ current: 1, size: 999 }).then((res) => {
        this.couponList = res.data.records;
      });
    },
    // 点击搜索店内商品，传mark参数，用于区分是否是及时送商品
    toSearchGoods(id) {
      uni.navigateTo({
        url: `/subpackage1/goods/searchList?mark=fromTimely&id=${id}`,
      });
    },
    goToShopList(id) {
      uni.navigateTo({ url: `/subpackage1/shop/recentShopList?id=${id}` });
    },
    // 去到商品详细页面，传入Mark参数用于区分是否是及时送商品，是否在详情页标识配送时间
    goToDetail(id) {
      uni.navigateTo({
        url: `/subpackage1/goods/detail?mark=fromTimely&id=${id}`,
      });
    },
    // 领取优惠券
    receiveCoupon(id) {
      coupon.receiveCoupon({ couponId: id }).then((res) => {
        if (res.code == 200) {
          uni.showToast({ title: res.msg });
          this.getCouponList();
        } else {
          uni.showToast({ title: res.msg, icon: 'none' });
        }
      });
    },
    // 获取附近店铺
    getTimelyShop() {
      uni.getStorage({
        //获取缓存里的经纬度信息
        key: 'location',
        success: (res) => {
          this.queryData = {
            city: res.data.city,
            longitude: res.data.longitude,
            latitude: res.data.latitude,
          };
          timely.getRecentShop(this.queryData).then((res) => {
            uni.hideLoading();
            //获取最近店铺
            this.recentShop = res.data;
            this.code = res.code;
            this.shopId = res.data.shopId;
            this.getShopAllInfo(res.data.shopId);
          });
        },
        fail: () => {
          this.getLocation();
        },
      });
    },
    // 下单时因标记及时送订单需要，每次onShow需要缓存delivery_time
    getDeliveryTime() {
      uni.getStorage({
        //获取缓存里的经纬度信息
        key: 'location',
        success: (res) => {
          this.queryData = {
            city: res.data.city,
            longitude: res.data.longitude,
            latitude: res.data.latitude,
          };
          timely.getRecentShop(this.queryData).then((res) => {
            let deliveryTime = res.data.estimatedTime;
            uni.setStorage({
              //把配送时间存起来用于在详情页面进行展示,也用于标记及时送订单
              key: 'delivery_time',
              data: deliveryTime,
              success: () => {},
            });
          });
        },
      });
    },
    // 获取店铺的所有信息
    getShopAllInfo(id) {
      shop.getShopInfo({ shopId: id }).then((res) => {
        //商铺信息
        this.shopInfo = res.data.shopInfo;
        this.shopCommodityList = res.data.commodityList;
        this.shopInfo.businessHours = this.shopInfo.businessHours //格式化时间
          .split(',')
          .map((item) => {
            let format = (num) => {
              return num >= 10 ? num : `0${num}`;
            };
            let hour = new Date(item).getHours();
            let minu = new Date(item).getMinutes();
            return `${format(hour)}:${format(minu)}`;
          })
          .join('~');
        uni.hideLoading();
      });
  //     good.getPageBySHopId({ shopId: id,size:this.size,current:this.current }).then((res) => {
  //       //获取商铺中的所有商品信息
  //       this.shopAllGoods = res.data.records;
		// console.log(res.data,'_______________');
		// this.total=res.data.total
  //       uni.hideLoading();
  //     });
			this.id=id
			this.getpageBy()
			
    },
	async getpageBy(){
		 const res= await good.getPageBySHopId({commodityType:1, shopId: this.id,size:this.size,})
		  //获取商铺中的所有商品信息
		  this.shopAllGoods = res.data.records;
				console.log(res.data,'_______________');
				this.total=res.data.total
				uni.hideLoading();
		
		
	},
    // 判断用户是否开启位置授权
    isAuthorizeInfo() {
      uni.getSetting({
        success: (authSetting) => {
          if (!authSetting.authSetting['scope.userLocation']) {
            uni.showModal({
              content: '检测到您没有开启位置权限，是否去开启？',
              success: (res) => {
                if (res.confirm) {
                  uni.openSetting({
                    success: () => {
                      uni.showToast({
                        title: '开启后请重新进入此页面',
                        icon: 'none',
                      });
                    },
                  });
                }
              },
            });
          } else {
            this.getLocation();
          }
        },
      });
    },
    getLocation() {
      let mapService = new QQMapWX({
        key: 'TAFBZ-3ZEKX-ADQ4G-THO7F-OVNZE-JQFNM',
      });
      uni.getLocation({
        type: 'gcj02',
        success: (res) => {
          mapService.reverseGeocoder({
            location: {
              latitude: res.latitude,
              longitude: res.longitude,
            },
            success: (result) => {
              let location = {
                latitude: res.latitude,
                longitude: res.longitude,
                address: result.result.address,
                ...result.result.address_component,
              };
              this.locationInfo = location;
              uni.setStorage({
                key: 'location',
                data: location,
              });
            },
            fail: (result) => {
              console.log(result);
            },
          });
        },
        fail: (err) => {
          console.log(err);
        },
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.page {
  width: 100vw;
  background-color: whitesmoke;
  .container {
    .top-select {
      display: flex;
      align-items: center;
      justify-content: space-around;
      width: 100%;
      background-color: #fff;
      .search-input {
        display: flex;
        align-items: center;
        height: 64rpx;
        width: 551rpx;
        border-radius: 32rpx;
        background-color: #fff;
        padding: 0 10rpx;
        .input {
         margin-left: 10rpx;
          color: #999999;
        }
      }
      .msg-icon {
        width: 43rpx;
        height: 46rpx;
      }
      img {
        width: 40rpx;
        height: 40rpx;
        margin-right: 20rpx;
      }
    }
    .tips {
      width: 100vw;
      height: 110rpx;
      background-color: #f2fafb;
      padding: 0 24rpx;
      view {
        color: #00b0d0;
        line-height: 50rpx;
        font-size: 26rpx;
      }
    }
    .bg-img {
      width: 100vw;
      height: 180rpx;
      border-radius: 0 0 50% 50%;
      filter: blur(5rpx);
    }
    .shop-info {
      width: 702rpx;
      height: 208rpx;
      margin-left: 24rpx;
      border-radius: 20rpx;
      margin-top: -160rpx;
      background-color: #fff;
      position: relative;
      .shop-img {
        width: 80rpx;
        height: 80rpx;
        border-radius: 8rpx;
        position: absolute;
        top: 24rpx;
        left: 24rpx;
      }
      .shop-name {
        font-size: 32rpx;
        color: #333;
        position: absolute;
        top: 27rpx;
        left: 124rpx;
      }
      .shop-distence {
        display: flex;
        align-items: center;
        position: absolute;
        top: 70rpx;
        left: 124rpx;
      }
      .time {
        font-size: 26rpx;
        color: #888;
        position: absolute;
        top: 124rpx;
        left: 26rpx;
      }
      .shop-address {
        font-size: 26rpx;
        color: #888;
        position: absolute;
        top: 158rpx;
        left: 26rpx;
      }
    }
    .recommend {
      width: 702rpx;
      margin-left: 24rpx;
      margin-top: 30rpx;
      &-title {
        display: flex;
        padding-left: 20rpx;
        img {
          width: 40rpx;
          height: 40rpx;
          margin-right: 13rpx;
        }
        text {
          font-size: 28rpx;
          color: #333;
        }
      }
      &-list {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        margin-top: 20rpx;
        &__item {
          flex: 0 0 48%;
          display: flex;
          flex-direction: column;
          margin-bottom: 30rpx;
          background-color: #fff;
          border-radius: 10rpx;
          padding: 15rpx;
          .name {
             width: 320rpx;
            color: #333;
            font-size: 28rpx;
            line-height: 30rpx;
            margin: 20rpx 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            font-weight: bold;
          }
          .price {
           
            color: #e81717;
            font-size: 32rpx;
            font-weight: bolder;
          }
          img {
            width: 100%;
            height: 360rpx;
            border-radius: 10rpx;
          }
        }
      }
    }
    .shopRecommend {
      width: 702rpx;
      margin-left: 24rpx;
      margin-top: 30rpx;
      &-title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-left: 20rpx;
        width: 100%;
        .title {
          display: flex;
          align-items: center;
        }
        .more {
          display: flex;
          align-items: center;
          font-size: 28rpx;
          color: #999;
          .arrow {
            width: 15rpx;
            height: 15rpx;
            border-color: #999 #999 transparent transparent;
          }
        }
        img {
          width: 40rpx;
          height: 40rpx;
          margin-right: 13rpx;
        }
        text {
          font-size: 28rpx;
          color: #333;
        }
      }
      &-list {
        display: flex;
        justify-content: space-between;
        flex-wrap: nowrap;
        margin-top: 20rpx;
        &__item {
          flex: 0 1 auto;
          height: 330rpx;
          display: flex;
          flex-direction: column;
          margin-bottom: 20rpx;
          background-color: #fff;
          padding: 15rpx;
          border-radius: 10rpx;
          .name {
            color: #333;
            width: 200rpx;
            font-size: 28rpx;
            line-height: 30rpx;
            margin: 20rpx 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            font-weight: bold;
          }
          .price {
            color: #e81717;
            font-size: 28rpx;
            font-weight: bolder;
          }
          img {
            width: 200rpx;
            height: 200rpx;
            border-radius: 10rpx;
          }
        }
      }
    }
    .coupon {
      width: 702rpx;
      height: 212rpx;
      margin-left: 24rpx;
      margin-top: 20rpx;
      background-image: url('/static/icons/coupon_bg.png');
      background-size: 720rpx 212rpx;
      background-repeat: no-repeat;
      padding: 18rpx 20rpx;
      border-radius: 20rpx;
      position: relative;
      &::after {
        display: block;
        content: '';
        height: 134rpx;
        width: 40rpx;
        background: linear-gradient(
          to left,
          rgba(246, 117, 73, 0.7),
          rgba(246, 117, 73, 0)
        );
        position: absolute;
        right: 15rpx;
        top: 60rpx;
        z-index: 9;
      }
      &-head {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .title {
          display: flex;
          align-items: center;
          text {
            color: #fff;
          }
          img {
            width: 40rpx;
            height: 40rpx;
            margin-right: 12rpx;
          }
        }
        .more {
          display: flex;
          align-items: center;
          font-size: 28rpx;
          color: #fff;
          .arrow {
            width: 15rpx;
            height: 15rpx;
            border-color: #fff #fff transparent transparent;
          }
        }
      }
      &-list {
        display: flex;
        overflow-x: auto;
        &::-webkit-scrollbar {
          width: 0;
          height: 0;
          background-color: transparent;
        }
        &__item {
          flex: 0 0 220rpx;
          height: 114rpx;
          margin-right: 20rpx;
          border-radius: 10rpx;
          background-color: #fff;
          margin-top: 20rpx;
          position: relative;
          .coupon-count {
            position: absolute;
            top: 15rpx;
            left: 17rpx;
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
          .coupon-primise {
            font-size: 24rpx;
            color: #ec1b3b;
            position: absolute;
            bottom: 20rpx;
            left: 17rpx;
          }
          .receive {
            font-size: 30rpx;
            color: #ec1b3b;
            writing-mode: vertical-lr;
            letter-spacing: 8rpx;
            position: absolute;
            right: 20rpx;
            top: 25rpx;
          }
          .cycle {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            height: 70rpx;
            position: absolute;
            right: -10rpx;
            top: 20rpx;
            view {
              width: 15rpx;
              height: 15rpx;
              border-radius: 50%;
              background-color: rgba(247, 91, 58, 1);
            }
            clip-path: polygon(0 0, 50% 0, 50% 100%, 0 100%);
          }
        }
      }
    }
  }
}
</style>
