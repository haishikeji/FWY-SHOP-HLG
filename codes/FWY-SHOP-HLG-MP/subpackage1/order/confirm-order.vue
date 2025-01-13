<template>
  <view class="page">
    <view class="container">
   <!--   <u-tabs
        class="tabs"
        ref="tabs"
        :list="tabList"
        :is-scroll="false"
        :current="current"
        v-if="!mark"
        active-color="#00B0D0"
        @change="tabChange"
      ></u-tabs> -->
      <template v-if="current == 0">
        <!-- <view class="send-time">
          <view style="color: #666">选择配送时间</view>
          <view class="send-time__layout">
            <view style="color: #666">请选择时间</view>
            <view class="arrow"></view>
          </view>
        </view> -->
        <template v-if="addressInfo">
          <view class="receiving-info">
            <view class="user-info">
              <text>{{ addressInfo.addressee }}</text>
              <text>{{ addressInfo.contactNumber }}</text>
            </view>
            <img src="/static/icons/13.png" @click="goToAddress" />
            <view class="address">
              <u-icon name="map" color="#BEBEBE" size="32"></u-icon>
              <text>{{ addressInfo.address }}</text>
            </view>
          </view>
          <view class="house-number">
            <text>楼号/门牌</text>
            <text>{{ addressInfo.houseNumber }}</text>
          </view>
        </template>
        <view class="no-address" @click="goToAddress" v-else>
          <text>请选择收货地址</text>
          <view class="arrow"></view>
        </view>
      </template>
      <template v-if="current == 1">
        <view class="page-section page-section-gap">
          <map
            style="width: 100vw; height: 300rpx"
            :latitude="shopAddress.latitude"
            :longitude="shopAddress.longitude"
            :markers="covers"
          >
          </map>
        </view>
        <view class="shop-address">
          <view class="address">{{ orderInfo.goodInfo.shopAddress }}</view>
          <view class="time_phone">
            <text>营业时间：{{ orderInfo.shopInfo.shopBusinessTime }}</text>
            <text>电话：{{ orderInfo.shopInfo.shopPhone }}</text>
          </view>
        </view>
        <view class="receive">
          <text>取货人</text>
          <view class="name_phone">
            <text v-if="form.name && form.tel"
              ><text>{{ form.name }}</text> <text>{{ form.tel }}</text></text
            >
            <text v-else>请输入取件人信息</text>
            <img src="/static/icons/13.png" alt="" @click="toModifyAddressee" />
          </view>
        </view>
      </template>
      <view class="good-info">
        <view class="shop-info">
          <img class="shop_ic" src="/static/icons/11.png" alt="" />
          <text class="shop_name">{{ shopAddress.shopName }}</text>
        </view>
        <view class="good-list">
          <view class="good-list__item">
            <view class="good-layout">
              <img class="good-img" :src="orderInfo.goodInfo.coverUrl" alt="" />
              <view class="good-list__item_info">
                <view class="good-name">{{
                  `${orderInfo.goodInfo.commodityName}&nbsp;&nbsp;${orderInfo.specificationName}`
                }}</view>
                <view class="price-layout">
                  <text style="color: #e81717; font-size: 28rpx">￥</text>
                  <text style="color: #e81717; font-size: 32rpx">{{
                   allPrice.price
                  }}</text>
                </view>
              </view>
            </view>
            <view class="good-list__item_count">
              x{{ orderInfo.purchaseQuantity }}
            </view>
          </view>
        </view>
        <view class="message">
          <u-field v-model="message" label="买家留言:" type="textarea" placeholder="请输入留言">
          </u-field>
        </view>
        <view class="coupon" @click="showCouponList = true" v-if="!mark">
          <view>优惠券</view>
          <view class="coupon_count">
            <view>{{ useCoupon.id ? useCoupon.couponName : '请选择' }}</view>
            <view class="arrow"></view>
          </view>
        </view>
      </view>
    </view>
    <view class="bottom-button">
      <view class="all-price">
        <text>总计</text>
        <text>￥</text>
        <text>{{ allPrice.actualPrice }}</text>
      </view>
      <view class="confirm-button" @click="confirmOrder">提交订单</view>
    </view>
    <u-popup v-model="showCouponList" mode="center" border-radius="20">
      <view class="coupon-list" v-if="myCouponList.length != 0">
        <view
          class="coupon-list__item"
          v-for="item in myCouponList"
          :key="item.id"
          @click="choiseCoupon(item)"
        >
          <img :src="item.coverUrl" alt="" />
          <view class="coupon-name">{{ item.couponName }}</view>
          <view class="count">￥{{ item.reduceAmount }}</view>
          <view class="full-count">满{{ item.fullAmount }}可用</view>
        </view>
      </view>
      <view class="coupon-list no-data" v-else>暂无可用优惠券</view>
    </u-popup>
    <u-popup v-model="show" mode="center" border-radius="20" length="70%">
      <view class="addressee" style="padding: '20rpx'">
        <u-form :model="form" ref="form">
          <u-form-item label="姓名" required>
            <u-input v-model="form.name" />
          </u-form-item>
          <u-form-item label="电话" required>
            <u-input :maxlength="11" v-model="form.tel" />
          </u-form-item>
        </u-form>
        <view @click="submit('form')" class="confirm-button" style="height: 70rpx"
          >确定</view
        >
      </view>
    </u-popup>
  </view>
</template>
<script>
import serve from '@/api/order/index';
import address from '@/api/address/index';
export default {
  onLoad() {
    this.orderInfo = uni.getStorageSync('orderInfo');
    console.log(this.orderInfo,567)
    this.mark = uni.getStorageSync('isActiveGood')
    this.getShopLocation();
    this.calcPrice();
    this.getCouponList();
  },
  onShow() {
    if (uni.getStorageSync('addressInfo')) {
      this.addressInfo = uni.getStorageSync('addressInfo');
    } else {
      this.getDefaultAddress();
    }
  },
  onHide() {
    uni.removeStorageSync('addressInfo');
  },
  data() {
    return {
      form: {
        name: '',
        tel: '',
      },
      rules: {
        name: [
          {
            required: true,
            message: '请输入收货人姓名',
            trigger: ['change'],
          },
        ],
        tel: [
          {
            required: true,
            message: '请输入收货人电话',
            trigger: ['change'],
          },
        ],
      },
      show: false,
      myCouponList: [],
      showCouponList: false,
      shopAddress: {
        latitude: '30.539123',
        longitude: '104.078852',
      },
      covers: [
        {
          id: 1,
          latitude: '30.539123',
          longitude: '104.078852',
          iconPath: '/static/tabbar/timely.png',
          width: 20,
          height: 20,
        },
      ],
      current: 0,
      tabList: [{ name: '配送' }, { name: '门店自提' }],
      orderInfo: [],
      addressInfo: null,
      allPrice: 0,
      useCoupon: {},
      message: '',
      mark: false, // 是否为活动商品的标记，用于隐藏优惠券和隐藏到店自提
      userInfo:uni.getStorageSync('userInfo'),
    };
  },
  methods: {
    submit() {
      if (!this.form.name) {
        uni.showToast({
          title: '请输入取件人姓名',
          icon: 'none',
        });
        return;
      } else if (!this.form.tel) {
        uni.showToast({
          title: '请输入取件人电话',
          icon: 'none',
        });
      }
      this.show = false;
    },
    toModifyAddressee() {
      this.show = true;
    },
    getShopLocation() {
      serve
        .getShopLocation({ shopId: this.orderInfo.goodInfo.shopId })
        .then((res) => {
          this.shopAddress = {
            latitude: res.data.latitude,
            longitude: res.data.longitude,
            shopName: res.data.shopName,
          };
          this.covers = this.covers.map((item) => {
            return {
              ...item,
              latitude: res.data.latitude,
              longitude: res.data.longitude,
            };
          });
        });
    },
    getDefaultAddress() {
      address.queryDefaultUserAddressInfo().then((res) => {
        this.addressInfo = res.data;
      });
    },
    getCouponList() {
      serve
        .chooseCoupon({
          specialId: this.orderInfo.specificationId,
          num: this.orderInfo.purchaseQuantity,
        })
        .then((res) => {
          this.myCouponList = res.data;
        });
    },
    calcPrice() {
      if (this.orderInfo.way == 0 || this.orderInfo.way == 1) {
        // 拼团商品计算价格
        let data = {
          commodityId: this.orderInfo.goodInfo.commodityId,
          num: this.orderInfo.purchaseQuantity,
          way: this.orderInfo.way,
        };
        serve.countPrice(data).then((res) => {
          if (res.code == 200) {
            this.allPrice = res.data;
          } else {
            uni.showToast({ title: res.msg, icon: 'none' });
          }
        });
      } else {
        let data = {
          couponId: this.useCoupon.couponId,
          paySingleDTO: [
            {
              commodityId: this.orderInfo.goodInfo.id,
              commodityType: this.orderInfo.goodInfo.commodityType,
              commoditySpecialId: this.orderInfo.specificationId,
              num: this.orderInfo.purchaseQuantity,
            },
          ],
        };
        serve.payCommodity(data).then((res) => {
          if (res.code == 200) {
            this.allPrice = res.data;
          } else {
            uni.showToast({ title: res.msg, icon: 'none' });
          }
        });
      }
    },
    tabChange(index) {
      this.current = index;
    },
    goToAddress() {
      uni.navigateTo({ url: `/subpackage1/address/list` });
    },
    choiseCoupon(val) {
      this.useCoupon = val;
      this.showCouponList = false;
      this.calcPrice();
    },
    confirmOrder() {
      if (!this.addressInfo && this.current == 0) {
        uni.showToast({
          title: '请选择收货地址',
          icon: 'none',
        });
        return;
      }
      if (this.current == 1 && this.form.name == '') {
        uni.showToast({
          title: '请填写收货人信息',
          icon: 'none',
        });
        return;
      }
      uni.showLoading();
      if (this.orderInfo.way == 0 || this.orderInfo.way == 1) {
        //拼团订单支付：拼团为1，自购为0
        let data = {
          addressId: this.addressInfo?.id ? this.addressInfo.id : '',
          commodityOrderDTO: {
            groupWorkNo: this.orderInfo.goodInfo.groupWorkNo || '',
            pickName: this.form.name,
            pickPhone: this.form.tel,
            deliveryMode: this.current,
            goodId: this.orderInfo.goodInfo.commodityId,
            num: this.orderInfo.purchaseQuantity,
            orderNote: this.message,
            specificationId: this.orderInfo.specificationId,
            way: this.orderInfo.way,
          },
        };
        serve.submitOrderGroup(data).then((res) => {
          uni.requestPayment({
            provider: 'wxpay',
            orderInfo: res.data.orderNo,
            timeStamp: res.data.timeStamp,
            nonceStr: res.data.nonceStr,
            package: res.data.packageValue,
            signType: res.data.signType,
            paySign: res.data.paySign,
            success: () => {
              uni.reLaunch({
                url: `/subpackage1/success/pay`,
              });
              uni.removeStorageSync('addressInfo');
              uni.removeStorageSync('isActiveGood');
              uni.hideLoading();
            },
            fail: (res) => {
              uni.showToast({
                title:"支付失败",
                icon:"none"
              })
              console.log('失败了');
              uni.navigateTo({
                url:'/subpackage1/order/my-order'
              })
              uni.removeStorageSync('addressInfo');
              uni.hideLoading();
            },
          });
        });
      } else {
        //非平团商品订单支付
        let data = {
          addressId: this.addressInfo?.id ? this.addressInfo.id : '',
          discountId: this.useCoupon.couponId,
          commodityOrderDTO: [
            {
              pickName: this.form.name,
              pickPhone: this.form.tel,
              deliveryMode: uni.getStorageSync('delivery_time')
                ? 2
                : this.current, // 增加及时送订单类型
              goodId: this.orderInfo.goodInfo.id,
              num: this.orderInfo.purchaseQuantity,
              orderNote: this.message,
              specificationId: this.orderInfo.specificationId,
            },
          ],
        };
        serve.addOrder(data).then((res) => {
          // 调用支付
          if(res.code !== 200) {
            return  uni.showToast({
                title:res.msg,
                icon:"none"
              })
          }
          uni.requestPayment({
            provider: 'wxpay',
            orderInfo: res.data.orderNo,
            timeStamp: res.data.timeStamp,
            nonceStr: res.data.nonceStr,
            package: res.data.packageValue,
            signType: res.data.signType,
            paySign: res.data.paySign,
            success: () => {
              uni.reLaunch({
                url: `/subpackage1/success/pay`,
              });
              uni.removeStorageSync('addressInfo');
              uni.hideLoading();
            },
            fail: (res) => {
              uni.showToast({
                title:"支付失败",
                icon:"none"
              })
              console.log('失败了');
              uni.navigateTo({
                url:'/subpackage1/order/my-order'
              })
              uni.removeStorageSync('addressInfo');
              uni.hideLoading();
            },
          });
        });
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  height: calc(100vh - 98rpx);
  overflow-y: auto;
  background-color: whitesmoke;
  .send-time {
    width: 702rpx;
    line-height: 88rpx;
    display: flex;
    margin-left: 24rpx;
    justify-content: space-between;
    align-items: center;
    border-bottom: 2rpx solid #ddd;
    &__layout {
      display: flex;
      align-items: center;
      .arrow {
        border-color: #666 #666 transparent transparent;
      }
    }
  }
  .receiving-info {
    width: 100vw;
    height: 128rpx;
    padding: 0 24rpx;
    position: relative;
    border-bottom: 2rpx solid #ddd;
    background-color: #fff;
    .user-info {
      position: absolute;
      left: 24rpx;
      top: 25rpx;
      text {
        font-size: 30rpx;
        color: #333;
        &:nth-of-type(1) {
          margin-right: 20rpx;
        }
      }
    }
    img {
      width: 40rpx;
      height: 40rpx;
      position: absolute;
      top: 25rpx;
      right: 24rpx;
    }
    .address {
      position: absolute;
      bottom: 20rpx;
      left: 24rpx;
      text {
        font-size: 28rpx;
        color: #666;
        margin-left: 12rpx;
      }
    }
  }
  .house-number {
    width: 100vw;
    padding: 0 24rpx;
    line-height: 88rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #fff;
    text {
      font-size: 28rpx;
      color: #666;
    }
  }
  .good-info {
    width: 100vw;
    padding: 25rpx;
    margin: 20rpx 0;
    background-color: #fff;
    .shop-info {
      line-height: 70rpx;
      border-bottom: 1rpx solid #e5e5e5;
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
    .good-list {
      &__item {
        height: 100rpx;
        margin: 40rpx 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .good-layout {
          display: flex;
          align-items: center;
          .good-img {
            width: 100rpx;
            height: 100rpx;
            margin-right: 20rpx;
          }
          .price-layout {
            margin-top: 10rpx;
          }
          .good-name {
            color: #333;
            font-size: 28rpx;
            width: 520rpx;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
          }
        }
        &_count {
          color: #888;
          font-size: 28rpx;
          align-self: flex-end;
        }
      }
    }
    .message {
      font-size: 28rpx;
      color: #333;
    }
    .coupon {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      box-sizing: border-box;
      padding: 20rpx;
      margin-top: 20rpx;
      &_count {
        display: flex;
        align-items: center;
        color: #999;
      }
    }
  }
  .shop-address {
    width: 100vw;
    height: 128rpx;
    padding: 24rpx;
    background-color: #fff;
    .address {
      font-size: 28rpx;
      color: #333;
      line-height: 40rpx;
    }
    .time_phone {
      display: flex;
      justify-content: space-between;
      align-items: center;
      line-height: 40rpx;
      text {
        font-size: 28rpx;
        color: #333;
      }
    }
  }
  .receive {
    width: 100vw;
    height: 88rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24rpx;
    margin-top: 20rpx;
    background-color: #fff;
    & > text {
      color: #666;
      font-size: 30rpx;
    }
    .name_phone {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 50%;
      text {
        color: #333;
        font-size: 30rpx;
      }
      img {
        width: 40rpx;
        height: 40rpx;
      }
    }
  }
}
.bottom-button {
  height: 98rpx;
  width: 100vw;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24rpx;
  position: fixed;
  left: 0;
  bottom: 0;
  .all-price {
    text {
      &:nth-of-type(1) {
        color: #333;
        font-size: 30rpx;
        margin-right: 20rpx;
      }
      &:nth-of-type(2) {
        font-size: 30rpx;
        color: #ec1b3b;
      }
      &:nth-of-type(3) {
        font-size: 40rpx;
        color: #ec1b3b;
      }
    }
  }
  .confirm-button {
    width: 290rpx;
    height: 74rpx;
  }
}
.no-address {
  width: 100vw;
  height: 128rpx;
  line-height: 128rpx;
  background-color: #fff;
  padding: 0 30rpx;
  font-size: 30rpx;
  color: #999;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.coupon-list {
  height: 50vh;
  width: 80vw;
  overflow-y: auto;
  padding: 10rpx 0;
  &__item {
    width: 94%;
    margin-left: 3%;
    height: 150rpx;
    margin-bottom: 10rpx;
    position: relative;
    background-color: #f8f8f8;
    border-radius: 10rpx;
    img {
      width: 120rpx;
      height: 120rpx;
      border-radius: 20rpx;
      position: absolute;
      top: 0;
      bottom: 0;
      left: 20rpx;
      margin: auto;
    }
    .coupon-name {
      font-size: 30rpx;
      color: #333;
      position: absolute;
      top: 20rpx;
      left: 170rpx;
    }
    .count {
      font-size: 32rpx;
      color: #ec1b3b;
      position: absolute;
      left: 170rpx;
      top: 80rpx;
    }
    .full-count {
      font-size: 24rpx;
      color: #ec1b3b;
      position: absolute;
      top: 89rpx;
      left: 280rpx;
    }
  }
}
.addressee {
  padding: 30rpx;
}
</style>
