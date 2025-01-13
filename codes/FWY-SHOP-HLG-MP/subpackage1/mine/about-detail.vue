<template>
  <view class="page"> 
    <view class="title">{{detail.articleName}}</view>
    <view class="info" v-html="detail.articleInfo"></view>
  </view>
</template>
<script>
import serve from '@/api/article/index';
export default {
  onLoad(param) {
    this.getList();
  },
  data() {
    return {
      detail: {},
    };
  },
  methods: {
	  getList() {
	    serve.getPage(this.queryData).then((res) => {
	      this.articleList = res.data.records;
	  	console.log(res.data.records)
		 this.getDetail(res.data.records[0].id)
	    });
	  },
    getDetail(id) {
      serve.getDetails({ articleId: id }).then((res) => {
        this.detail = res.data;
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.title {
  width: 100vw;
  height: 100rpx;
  line-height: 100rpx;
  font-size: 36rpx;
  font-weight: bold;
  text-align: center;
}
.info {
  padding: 20rpx;
  width: 100vw;
}
</style>
