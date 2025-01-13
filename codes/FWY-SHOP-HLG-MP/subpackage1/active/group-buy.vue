<template>
  <view class="page">
    <view class="search-input">
      <u-icon slot="icon" name="search" color="#999999" size="32"></u-icon>
      <input
        style="margin-left: 10rpx"
        @input="debounceInput"
        placeholder="搜索商品及店铺"
        v-model="queryData.name"
      />
    </view>
    <view class="good-list" v-if="groupList.length > 0">
      <view
        class="good-list__item"
        v-for="item in groupList"
        :key="item.id"
        @click="goToDetail(item.id, item.groupNum)"
      >
        <img class="good-img" :src="item.coverUrl" />
        <!-- <view class="good-tag">预售     </view> -->
        <view class="good-name">{{ item.commodityName }}</view>
        <view class="tag">{{ shopType(item.shopType) }}</view>      
        <view class="limit">{{ item.groupNum }}人拼团</view>
        <view class="good-price">
          <text>￥</text>
          <text>{{ item.groupWorkPrice }}</text>
        </view>
        <view class="buy-button"> 我要开团 </view>
      </view>
    </view>
    <view class="noDataContainer" v-else>
      <img src="../../static/icons/image_no_data.png" alt="" />
    </view>
  </view>
</template>
<script>
import active from '@/api/active/index';
import util from '@/util/util';
export default {
  onLoad() {
    this.getGroup();
  },
  onReachBottom() {
    this.queryData.current++;
    active.getJoinAGroupCommodityPage(this.queryData).then((res) => {
      if (this.groupList.length < res.data.total) {
        this.groupList.push(...res.data.records);
      }
    });
  },
  data() {
    return {
      groupList: [],
      queryData: {
        size: 10,
        current: 1,
        name: "",
      },
    };
  },
  onReachBottom() {
    this.formData.current++;
    active.getJoinAGroupCommodityPage(this.formData).then((res) => {
      if (res.data.total > this.groupList.length) {
        this.groupList.push(...res.data.records);
      }
    })},
  computed: {
    debounceInput() {
      return util.debounce(this.getGroup);
    },
  },
  methods: {
    shopType(type) {
      return ["自营", "旗舰店", "其他"][type];
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/subpackage1/active/group-buy-detail?id=${id}`,
      });
    },
    getGroup() {
      active.getJoinAGroupCommodityPage(this.queryData).then((res) => {
        this.groupList = res.data.records;
      });
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
    min-height: calc(100vh - 104rpx);
    padding: 20rpx 0;
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
        width: 450rpx;
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
