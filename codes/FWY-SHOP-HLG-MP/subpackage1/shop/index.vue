<template>
  <view class="page">
    <view class="container">
      <img class="bg-img" :src="shopInfo.shopInfo.shopBackground" alt="" />
      <view class="shop-info">
        <img class="shop-img" :src="shopInfo.shopInfo.shopBackground" alt="" />
        <view class="shop-name">{{ shopInfo.shopInfo.shopName }}</view>
        <view class="shop-phone"
          >联系电话：{{ shopInfo.shopInfo.customerServicePhone }}</view
        >
        <!-- <view class="shop-time">营业时间：9:00-22:00</view> -->
        <view class="collection">
          <u-icon
            name="heart"
            color="gray"
            size="35"
            @click="toCollectShop"
            v-if="!shopInfo.shopInfo.followFlag"
          ></u-icon>
          <u-icon
            name="heart-fill"
            color="red"
            size="35"
            @click="toCollectShop"
            v-else
          ></u-icon>
          <!-- <img src="/static/icons/14.png" alt="" /> -->
          <text>收藏</text>
        </view>
      </view>
      <view class="search-input" @click="toSearchGoods">
        <u-icon slot="icon" name="search" color="#999999" size="32"></u-icon>
        <view class="input">搜索店铺内商品</view>
      </view>
      <view class="shopRecommend">
        <view class="shopRecommend-title">
          <view class="title">
            <img src="/static/icons/6.png" />
            <text>商家推荐</text>
          </view>
        </view>
       <view class="shopRecommend-list" v-if="shopInfo.commodityList.length != 0">
          <view
            class="shopRecommend-list__item"
            v-for="item in shopInfo.commodityList"
            :key="item.id"
            @click="goToDetail(item.id)"
          >
            <img :src="item.coverUrl" />
            <text class="name">{{ item.commodityName }}</text>
            <text class="price" v-if="userInfo.isShopowner && item.shopownerPrice != 0">￥{{ item.shopownerPrice }}</text>
            <text class="price" v-else>￥{{ item.sellPrice }}</text>
          </view>
        </view>
        <view class="no-data" v-else>暂无推荐</view>
      </view>
      <view class="coupon" v-if="couponList.length!=0">
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
        <view class="coupon-list" v-if="couponList.length != 0">
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
        <view class="recommend-list">
          <view
            class="recommend-list__item"
            v-for="item in goodsList"
            :key="item.id"
            @click="goToDetail(item.id)"
          >
            <img :src="item.coverUrl" />
            <text class="name">{{ item.commodityName }}</text>
           <text class="price" v-if="userInfo.isShopowner && item.shopownerPrice != 0">￥{{ item.shopownerPrice }}</text>
           <text class="price" v-else>￥{{ item.sellPrice }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>
<script>
import serve from "@/api/shop/index";
import coupon from "@/api/coupon/index";
export default {
  onLoad(param) {
    this.shopId = param.id;
    this.getShopInfo();
    this.getCoupon();
    this.getShopGoods();
  },
  onReachBottom() {
	console.log(1)
	if(this.queryData.current< this.pages) {
		this.queryData.current = parseInt(this.queryData.current) + 1
		this.getShopGoods()
	}
  },
  data() {
    return {
      userInfo:uni.getStorageSync('userInfo'),
      shopId: "",
      shopInfo: {},
      queryData: {
        size: 10,
        current: 1,
      },
      goodsList: [],
      couponList: [],
	  pages:0,
    };
  },
  onShareAppMessage() {
  	var that = this;
  	// 设置菜单中的转发按钮触发转发事件时的转发内容
  	var shareObj = {
  		title: "商城", // 默认是小程序的名称(可以写slogan等)
  		path: '/subpackage1/shop/index', // 默认是当前页面，必须是以‘/’开头的完整路径
  		imageUrl: '', //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径，支持PNG及JPG，不传入 imageUrl 则使用默认截图。显示图片长宽比是 5:4
  		success: function(res) {
  			// 转发成功之后的回调
  			if (res.errMsg == 'shareAppMessage:ok') {}
  		},
  		fail: function() {
  			// 转发失败之后的回调
  			if (res.errMsg == 'shareAppMessage:fail cancel') {
  				// 用户取消转发
  			} else if (res.errMsg == 'shareAppMessage:fail') {
  				// 转发失败，其中 detail message 为详细失败信息
  			}
  		}
  	};
  },
  onShareTimeline() {
  	var that = this;
  	// 设置菜单中的转发按钮触发转发事件时的转发内容
  	var shareObj = {
  		title: "商城", // 默认是小程序的名称(可以写slogan等)
  		path: '/subpackage1/shop/index', // 默认是当前页面，必须是以‘/’开头的完整路径
  		imageUrl: '', //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径，支持PNG及JPG，不传入 imageUrl 则使用默认截图。显示图片长宽比是 5:4
  		success: function(res) {
  			// 转发成功之后的回调
  			if (res.errMsg == 'shareAppMessage:ok') {}
  		},
  		fail: function() {
  			// 转发失败之后的回调
  			if (res.errMsg == 'shareAppMessage:fail cancel') {
  				// 用户取消转发
  			} else if (res.errMsg == 'shareAppMessage:fail') {
  				// 转发失败，其中 detail message 为详细失败信息
  			}
  		}
  	};
  },
  methods: {
    receiveCoupon(id) {
      coupon.receiveCoupon({ couponId: id }).then((res) => {
        if (res.code == 200) {
          uni.showToast({ title: res.msg });
          this.getCoupon();
        } else {
          uni.showToast({
            title: res.msg,
            icon: "none",
          });
        }
      });
    },
    toCollectShop() {
      serve.collect({ shopId: this.shopId }).then((res) => {
        this.getShopInfo();
      });
    },
    toSearchGoods() {
      uni.navigateTo({ url: `/subpackage1/goods/searchList?id=${ this.shopId }` });
    },
    getShopInfo() {
      serve.getShopInfo({ shopId: this.shopId }).then((res) => {
        this.shopInfo = res.data;
      });
    },
    getShopGoods() {
      serve
        .getPageBySHopId({ ...this.queryData, shopId: this.shopId })
        .then((res) => {
			if(this.queryData.current == 1) {
				this.goodsList = res.data.records;
				this.pages = res.data.pages;
			} else {
				this.goodsList = this.goodsList.concat(res.data.records);
			}
          
		  
        });
    },
    receiveCoupon(id) {
      coupon.receiveCoupon({ couponId: id }).then((res) => {
        if (res.code == 200) {
          uni.showToast({ title: res.msg });
          this.getCouponList();
        } else {
          uni.showToast({ title: res.msg, icon: "none" });
        }
      });
    },
    getCoupon() {
      coupon.queryAppCouponPage({ size: 999, current: 1 }).then((res) => {
        this.couponList = res.data.records;
      });
    },
    goToDetail(id) {
      uni.navigateTo({ url: `/subpackage1/goods/detail?id=${id}` });
    },
    goToCoupon() {
      uni.navigateTo({ url: `/subpackage1/coupon/coupon` });
    },
  },
};
</script>
<style lang="scss" scoped>
.page {
  background-color: whitesmoke;
  .container {
    .bg-img {
      width: 100vw;
      height: 180rpx;
      border-radius: 0 0 50% 50%;
      filter: blur(5rpx);
    }
    .shop-info {
      width: 702rpx;
      height: 168rpx;
      margin-left: 24rpx;
      border-radius: 20rpx;
      margin-top: -140rpx;
      background-color: #fff;
      box-shadow: 1px 1px 10px 0px rgba(118, 118, 118, 0.25);
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
      .shop-phone {
        color: #888;
        font-size: 26rpx;
        margin-top: 10rpx;
        position: absolute;
        top: 60rpx;
        left: 124rpx;
      }
      .shop-time {
        color: #888;
        font-size: 26rpx;
        margin-top: 10rpx;
        position: absolute;
        top: 95rpx;
        left: 124rpx;
      }
      .collection {
        display: flex;
        flex-direction: column;
        align-items: center;
        position: absolute;
        top: 24rpx;
        right: 24rpx;
        img {
          width: 36rpx;
          height: 36rpx;
        }
        text {
          font-size: 20rpx;
          color: #999;
        }
      }
    }
    .search-input {
      display: flex;
      align-items: center;
      height: 64rpx;
      width: 702rpx;
      margin-left: 24rpx;
      border-radius: 32rpx;
      background-color: #fff;
      padding: 0 20rpx;
      margin-top: 20rpx;
      .input {
         margin-left: 10rpx;
          color: #999999;
        }
    }
     .shopRecommend {
      width: 702rpx;
      margin-left: 24rpx;
      margin-top: 30rpx;
	  overflow: hidden;
	  box-sizing: border-box;
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
        flex-wrap: wrap;
        margin-top: 20rpx;
        width: 100vw;
        &__item {
          width: 230rpx;
          height: 330rpx;
          display: flex;
          flex-direction: column;
          margin-bottom: 20rpx;
          background-color: #fff;
          padding: 15rpx;
          border-radius: 10rpx;
          margin: 5rpx;
          .name {
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
		width: 100%;
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
    .coupon {
      width: 702rpx;
      height: 212rpx;
      margin-left: 24rpx;
      margin-top: 20rpx;
      background-image: url("/static/icons/coupon_bg.png");
      background-size: 720rpx 212rpx;
      background-repeat: no-repeat;
      padding: 18rpx 20rpx;
      border-radius: 20rpx;
      position: relative;
      &::after {
        display: block;
        content: "";
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
          .receive_over {
            font-size: 30rpx;
            color: #d8d8d8;
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
.no-data {
  height: 250rpx;
}
</style>
