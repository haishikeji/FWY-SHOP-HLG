<template>
  <view class="page">
    <view class="search-input">
      <u-icon slot="icon" name="search" color="#999999" size="32"></u-icon>
      <input
        style="margin-left: 10rpx"
        @input="debounceInput"
        placeholder="搜索商品及店铺"
        v-model="formData.name"
      />
    </view>
    <view class="good-list" v-if="seckillList.length > 0">
      <view
        class="good-list__item"
        v-for="item in seckillList"
        :key="item.id"
        @click="goToDetail(item.id)"
      >
        <img class="good-img" :src="item.coverUrl" />
        <view class="good-tag">秒杀</view>
        <view class="good-name">{{item.brandName}}&nbsp;{{ item.commodityName }}</view>
        <view class="tag">{{ shopType(item.shopType) }}</view>
        <view class="limit">限量{{ item.stock }}件</view>
        <view class="end-time">
          <view class="end-time_text">距离结束</view>
          <view class="end-time_time">{{ item.countDown }}</view>
        </view>
        <view class="good-price">
          <text>￥</text>
           <!-- <text v-if="userInfo.isShopowner && item.shopownerPrice !== 0">{{ item.shopownerPrice }}</text> -->
          <text>{{ item.sellPrice }} <text class="originalPrice">￥{{item.originalPrice}}</text></text>
        </view>
        <view class="buy-button"> 立即抢购 </view>
      </view>
    </view>
    <view class="noDataContainer" v-else>
      <img src="../../static/icons/image_no_data.png" alt="" />
    </view>
  </view>
</template>
<script>
import active from "@/api/active/index";
import util from "@/util/util";
export default {
  onLoad() {
    this.getSeckill();
  },
  onUnload() {
    clearInterval(this.interval);
  },
  data() {
    return {
      userInfo:uni.getStorageSync('userInfo'),
      seckillList: [],
      countDown: "",
      interval: null,
      formData: {
        size: 999,
        current: 1,
        name: "",
      },
    };
  },
  onReachBottom() {
    this.formData.current++;
    active.getSpikeCommodityPage(this.formData).then((res) => {
      if (res.data.total > this.seckillList.length) {
        this.seckillList.push(
          ...res.data.records.map((item) => {
            return {
              ...item,
              countDown: this.openCountDown(item.endTime.replace(/\-/g, "/")),
            };
          })
        );
        if (this.interval) clearInterval(this.interval);
        this.interval = setInterval(() => {
          this.seckillList.forEach((item) => {
            item.countDown = this.openCountDown(item.endTime.replace(/\-/g, "/"));
          });
        }, 1000);
      }
    });
  },
  computed: {
    debounceInput() {
      return util.debounce(this.getSeckill);
    },
  },
  methods: {
    shopType(type) {
      return ["自营", "旗舰店", "其他"][type];
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/subpackage1/active/seckill-detail?id=${id}`,
      });
    },
    getSeckill() {
      active.getSpikeCommodityPage(this.formData).then((res) => {
        this.seckillList = res.data.records.map((item) => {
          return {
            ...item,
            countDown: this.openCountDown(item.endTime.replace(/\-/g, "/")),
          };
        });
        if (this.interval) clearInterval(this.interval);
        this.interval = setInterval(() => {
          this.seckillList.forEach((item) => {
            // this.$set(item, 'countDown', this.openCountDown(item.endTime));
            item.countDown = this.openCountDown(item.endTime.replace(/\-/g, "/"));
          });
        }, 1000);
      });
    },
    openCountDown(endTime) {
      let date = new Date(endTime).getTime() - new Date().getTime();
      let day = Math.floor(date / (1000 * 60 * 60 * 24));
      let hours = Math.floor((date / (1000 * 60 * 60)) % 24);
      let minutes = Math.floor((date / (1000 * 60)) % 60);
      let seconds = Math.floor((date / 1000) % 60);
      return `${day}天${hours < 10 ? "0" + hours : hours}:${
        minutes < 10 ? "0" + minutes : minutes
      }:${seconds < 10 ? "0" + seconds : seconds}
            `;
    },
  },
};
</script>
<style lang="scss" scoped>
.page {
  .search-input {
    display: flex;
    align-items: center;
    height: 64rpx;
    width: 702rpx;
    border-radius: 32rpx;
    background-color: #f7f7f7;
    padding: 0 40rpx;
    margin: 20rpx;
    input {
      width: 600rpx;
    }
  }
  .nodata {
    height: 100rpx;
    text-align: center;
    line-height: 100rpx;
    color: grey;
    font-style: italic;
  }
  .good-list {
    background-color: whitesmoke;
    padding: 20rpx 0;
    min-height: calc(100vh - 104rpx);
    &__item {
      width: 702rpx;
      height: 240rpx;
      margin-left: 24rpx;
      margin-bottom: 20rpx;
      position: relative;
      background-color: #fff;
      border-radius: 20rpx;
      .good-img {
        width: 172rpx;
        height: 172rpx;
        position: absolute;
        top: 33rpx;
        left: 33rpx;
      }
      .good-name {
        width: 300rpx;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-size: 30rpx;
        color: #333;
        position: absolute;
        top: 37rpx;
        left: 241rpx;
        font-weight: bold;
      }
      .good-tag {
        width: 80rpx;
        height: 40rpx;
        border-radius: 0 20rpx 20rpx 0;
        background-color: #f76950;
        font-size: 26rpx;
        color: #fff;
        display: flex;
        justify-content: center;
        align-items: center;
        position: absolute;
        left: 20rpx;
        top: 20rpx;
      }
      .tag {
        height: 33rpx;
        line-height: 33rpx;
        text-align: center;
        background-color: #ec1b3b;
        color: #fff;
        font-size: 24rpx;
        position: absolute;
        top: 90rpx;
        left: 251rpx;
      }
      .limit {
        font-size: 26rpx;
        color: #666;
        position: absolute;
        top: 88rpx;
        left: 326rpx;
      }
      .end-time {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        height: 80rpx;
        position: absolute;
        top: 40rpx;
        right: 20rpx;
        &_text {
          color: #666;
          font-size: 26rpx;
        }
        &_time {
          font-size: 26rpx;
          color: #f76950;
        }
      }
      .good-price {
        position: absolute;
        left: 248rpx;
        top: 164rpx;
        font-weight: bold;
        text {
          color: #ec1b3b;
          &:nth-of-type(1) {
            font-size: 24rpx;
          }
          &:nth-of-type(2) {
            font-size: 30rpx;
          }
        }
		.originalPrice{
				text-decoration: line-through;
				font-size: 28rpx;
				color: #999999;
				font-weight: 400;
			}
      }
      .buy-button {
        width: 182rpx;
        height: 60rpx;
        border-radius: 32rpx;
        background: $button-bgc;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 30rpx;
        color: #fff;
        position: absolute;
        top: 145rpx;
        right: 20rpx;
      }
    }
  }
}

.noDataContainer {
  height: calc(100vh - 104rpx);
}
</style>
