<template>
  <view class="container">
    <view class="search-input">
      <u-icon slot="icon" name="search" color="#999999" size="32"></u-icon>
      <input
        style="margin-left: 10rpx"
        @input="debounceInput"
        placeholder="请输入商品名称"
        v-model="queryData.name"
      />
    </view>
    <view class="recommend">
      <view class="recommend-title">
        <img src="/static/icons/6.png" />
        <text>{{
          queryData.name.length == 0 ? "为您推荐" : "搜索结果如下"
        }}</text>
      </view>
      <view class="nodata" v-if="searchList == ''"> 暂无搜索结果 </view>
      <view class="recommend-list">
        <view
          class="recommend-list__item"
          v-for="item in searchList"
          :key="item"
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
</template>

<script>
import good from "../../api/good/index";
import util from "@/util/util.js";
import shop from "@/api/shop/index";
export default {
  onLoad(param) {
	  console.log(param)
    this.mark = param.mark;
    this.shopId = param.id;
    if (param.id) {
      this.getShopInfo();
    } else {
      this.toSearchGoods();
    }
  },
  computed: {
    debounceInput() {
      return util.debounce(this.toSearchGoods);
    },
  },
  data() {
    return {
      userInfo:uni.getStorageSync('userInfo'),
      queryData: {
        current: 1,
        size: 10,
        name: "",
      },
      searchList: [],
      timer: null,
      shopId: "",
      mark: "",
    };
  },
  onReachBottom() {
    this.queryData.current++;
    good.getRecommendCommodityPage(this.formData).then((res) => {
      if (res.data.total > this.searchList.length) {
        this.searchList.push(...res.data.records);
      }
    });
  },
  onShareAppMessage() {
  	var that = this;
  	// 设置菜单中的转发按钮触发转发事件时的转发内容
  	var shareObj = {
  		title: "商城", // 默认是小程序的名称(可以写slogan等)
  		path: '/subpackage1/goods/searchList', // 默认是当前页面，必须是以‘/’开头的完整路径
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
  		path: '/subpackage1/goods/searchList', // 默认是当前页面，必须是以‘/’开头的完整路径
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
    getShopInfo() {
      shop.getShopInfo({ shopId: this.shopId }).then((res) => {
        this.searchList = res.data.commodityList;
      });
    },
    toSearchGoods() {
      if (this.shopId) {
        good
          .getPageBySHopId({
            shopId: this.shopId,
            queryName: this.queryData.name,
          })
          .then((res) => {
            this.searchList = res.data.records;
          });
      } else {
        good.getRecommendCommodityPage(this.queryData).then((res) => {
          this.searchList = res.data.records;
        });
      }
    },
    goToDetail(id) {
      if (this.mark) {
        uni.navigateTo({ url: `/subpackage1/goods/detail?mark=fromTimely&id=${id}` });
      } else {
        uni.navigateTo({ url: `/subpackage1/goods/detail?id=${id}` });}
    },
  },
  watch: {
    name: (str) => {
      if (typeof str === "string") {
        if (str.trim().length !== 0) {
          this.debounce(this.changeStr, 1500);
        } else {
        }
      }
    },
  },
};
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background-color: whitesmoke;
  padding-top: 20rpx;
  .search-input {
    margin-left: 45rpx;
    display: flex;
    align-items: center;
    height: 64rpx;
    width: 650rpx;
    border-radius: 32rpx;
    background-color: #fff;
    padding: 0 20rpx;
    border: 2rpx solid gray;
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
            // white-space: nowrap;
            // overflow: hidden;
            // text-overflow: ellipsis;
            // font-weight: bold;
			font-weight: bold;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 2;
			overflow: hidden;
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
}
</style>