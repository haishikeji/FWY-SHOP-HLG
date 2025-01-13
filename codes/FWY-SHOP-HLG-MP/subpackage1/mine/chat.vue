<template>
  <view class="page">
    <scroll-view
      scroll-y="true"
      id="scroll-view"
      class="scroll-view"
      @scrolltoupper="toUpper"
      :scroll-top="scrollTop"
    >
      <view
        class="container"
        id="container"
        v-for="item in chatList"
        :key="item.id"
      >
        <view class="chat-user" v-if="item.type == 0">
          <view class="chat-user__content">
            {{ item.content }}
          </view>
          <img :src="userHeadUrl" v-if="userHeadUrl" />
          <img src="/static/mine/image_Avatar_boy.png" v-else />
        </view>
        <view class="chat-butler" v-if="item.type == 1">
          <img src="/static/icons/16.png" />
          <view class="chat-butler__content">
            {{ item.content }}
          </view>
        </view>
      </view>
    </scroll-view>
    <view class="bottom-input">
      <u-input v-model="content" height="70"></u-input>
      <view class="emit" @click="emitMsg">发送</view>
    </view>
  </view>
</template>
<script>
export default {
  onLoad() {
    this.getRelationId();
    uni.getStorage({
      key: 'loginAppletUserVo',
      success: (res) => {
        this.userHeadUrl = res.data.headUrl;
      },
    });
  },
  updated() {
    this.$nextTick(() => {
      this.scrollToBottom();
    });
  },
  data() {
    return {
      associationId: '',
      chatList: [],
      content: '',
      userHeadUrl: '',
      queryData: {
        current: 1,
        size: 30,
      },
      scrollTop: 0,
    };
  },
  methods: {
    toUpper() {
      this.queryData = {
        ...this.queryData,
        current: this.queryData.current + 1,
      };
      serve
        .queryChatRecordPage({
          ...this.queryData,
          associationId: this.associationId,
          type: 0,
        })
        .then((res) => {
          if (this.chatList.length < res.data.total) {
            this.chatList.unshift(...res.data.records.reverse());
          } else {
            return false;
          }
        });
    },
    scrollToBottom() {
      let query = uni.createSelectorQuery();
      query
        .in(this)
        .selectAll('#container')
        .boundingClientRect((res) => {
          let mitemHeight = 0;
          mitemHeight = res[0].reduce((acc, cur) => {
            return acc + cur.height + 30;
          }, 0);
          this.scrollTop = mitemHeight;
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.page {
  padding: 30rpx;
  .scroll-view {
    height: calc(100vh - 110rpx);
    .container {
      .chat-user {
        width: 690rpx;
        display: flex;
        justify-content: flex-end;
        margin-top: 30rpx;
        img {
          width: 60rpx;
          height: 60rpx;
          border-radius: 10rpx;
        }
        &__content {
          width: 400rpx;
          margin-right: 10rpx;
          background-color: #fff;
          text-align: right;
        }
      }
      .chat-butler {
        width: 690rpx;
        display: flex;
        justify-content: flex-start;
        margin-top: 30rpx;
        img {
          width: 60rpx;
          height: 60rpx;
          border-radius: 10rpx;
        }
        &__content {
          width: 400rpx;
          margin-left: 10rpx;
          background-color: #fff;
        }
      }
    }
  }
  .bottom-input {
    width: 100%;
    position: fixed;
    bottom: 10rpx;
    left: 0;
    padding: 0 40rpx;
    display: flex;
    u-input {
      flex: 0 0 70vw;
      margin-right: 2vw;
    }
    .emit {
      flex: 0 0 18vw;
      background-color: #2fba69;
      height: 70rpx;
      line-height: 70rpx;
      text-align: center;
      border-radius: 20rpx;
      color: #fff;
    }
  }
}
</style>
