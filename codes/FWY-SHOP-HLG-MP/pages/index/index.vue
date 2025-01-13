<template>
	<view class="page">
		<img src="/static/bg.png" class="bg-img" />
		<view class="container">
			<view class="top-select">
				<view @click="getLocation">
					<u-icon name="map" color="#fff" size="32"></u-icon>
					<text @click="getAddress" style="color: #fff; fong-size: 30rpx">{{
            locationInfo.city
          }}</text>
				</view>
				<view class="search-input" @click="toSearchGoods">
					<u-icon slot="icon" name="search" color="#999999" size="32"></u-icon>
					<view class="input">搜索商品</view>
				</view>
				<img class="msg-icon" src="/static/icons/1.png" alt="" @click="goToMessage" />
			</view>
			<view class="swiper">
				<u-swiper :list="swiperList" :height="288"></u-swiper>
			</view>
			<view class="active">
				<view class="active-title">
					<img src="/static/icons/5.png" alt="" />
					<text>限时活动</text>
				</view>
				<view class="active-list">
					<view class="active-list__item" @click="goToSeckill">
						<img src="/static/icons/2.png" alt="" />
						<text>秒杀</text>
					</view>
					<view class="active-list__item" @click="goToPresale">
						<img src="/static/icons/3.png" alt="" />
						<text>预售</text>
					</view>
					<view class="active-list__item" @click="goToGroup">
						<img src="/static/icons/7.png" alt="" />
						<text>拼团</text>
					</view>
					<view class="active-list__item" @click="goToMore">
						<img src="/static/icons/4.png" alt="" />
						<text>更多</text>
					</view>
				</view>
			</view>
			<view class="recommend">
				<view class="recommend-title">
					<img src="/static/icons/6.png" />
					<text>为你推荐</text>
				</view>
				<view class="recommend-list">
					<view class="recommend-list__item" v-for="item in recommendGoodsList" :key="item.id"
						@click="goToDetail(item.id)">
						<img :src="item.coverUrl" />
						<text class="name">{{item.brandName}}&nbsp;{{ item.commodityName}}</text>

						<!-- <text class="price"
							v-if="userInfo.isShopowner && item.shopownerPrice != 0">￥{{ item.shopownerPrice }}</text> -->
						<text class="price">￥{{ item.sellPrice }} <text class="originalPrice">￥{{item.originalPrice}}</text></text>
						
					</view>
				</view>
			</view>
		</view>
		<u-tabbar :list="tabbar" :mid-button="false" active-color="#1EA8C1"></u-tabbar>
	</view>
</template>

<script>
	import good from "../../api/good/index";
	import banner from "../../api/banner/index";
	const QQMapWX = require("../../util/map/qqmap-wx-jssdk.js");
	export default {
		onLoad() {
			this.tabbar = this.$store.state.tabbarList;
			this.isLogin();
			this.getRecommendGood();
			this.getBanner();
			this.getLocation();
		},
		onPullDownRefresh() {
			this.isLogin();
			this.getRecommendGood();
			this.getBanner();
			this.getLocation();
		},
		onReachBottom() {
			this.queryData.current++;
			good.getRecommendCommodityPage(this.queryData).then((res) => {
				if (res.data.total > this.recommendGoodsList.length) {
					this.recommendGoodsList.push(...res.data.records);
				}
			});
		},
		data() {
			return {
				userInfo: uni.getStorageSync('userInfo'),
				tabbar: [],
				swiperList: [],
				queryData: {
					current: 1,
					size: 10,
				},
				recommendGoodsList: [],
				locationInfo: {},
			};
		},
		onShareAppMessage() {
			var that = this;
			// 设置菜单中的转发按钮触发转发事件时的转发内容
			var shareObj = {
				title: "商城", // 默认是小程序的名称(可以写slogan等)
				path: '/pages/index/index', // 默认是当前页面，必须是以‘/’开头的完整路径
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
				path: '/pages/index/index', // 默认是当前页面，必须是以‘/’开头的完整路径
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
			getAddress() {
				uni.chooseLocation({
					success: (res) => {
						console.log(res)
						this.$nextTick(() => {
							this.locationInfo.city = res.address.slice(0, 6)
						})
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
					}
				});
			},
			getLocation() {
				let mapService = new QQMapWX({
					key: "TAFBZ-3ZEKX-ADQ4G-THO7F-OVNZE-JQFNM",
				});
				uni.getLocation({
					type: "gcj02",
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
									key: "location",
									data: location,
								});
							},
							fail: (result) => {
								console.log(result);
							},
						});
					},
				});
			},
			getBanner() {
				banner.queryBannerByPosition({
					position: "home"
				}).then((res) => {
					this.swiperList = res.data.map((item) => {
						return {
							image: item.bannerPicture,
						};
					});
					uni.stopPullDownRefresh();
				});
			},
			getRecommendGood() {
				this.queryData.current = 1;
				good.getRecommendCommodityPage(this.queryData).then((res) => {
					this.recommendGoodsList = res.data.records;
				});
			},
			isLogin() {
				uni.getStorage({
					key: "token",
					success: (res) => {
						if (!res) {
							uni.navigateTo({
								url: "/subpackage1/login/index",
							});
						}
					},
				});
			},
			goToDetail(id) {
				uni.navigateTo({
					url: `/subpackage1/goods/detail?id=${id}`
				});
			},
			goToSeckill() {
				uni.navigateTo({
					url: `/subpackage1/active/seckill`
				});
			},
			goToPresale() {
				uni.navigateTo({
					url: `/subpackage1/active/presale`
				});
			},
			goToGroup() {
				uni.navigateTo({
					url: `/subpackage1/active/group-buy`
				});
			},
			goToMore() {
				uni.switchTab({
					url: `/pages/index/category`
				});
			},
			goToMessage() {
				uni.navigateTo({
					url: `/subpackage1/mine/message`
				});
			},
			toSearchGoods() {
				uni.navigateTo({
					url: `/subpackage1/goods/searchList`
				});
			},
		},
	};
</script>

<style scoped lang="scss">
	.page {
		background-color: whitesmoke;
		min-height: 100vh;

		.bg-img {
			width: 100vw;
			height: 350rpx;
		}

		.container {
			margin-top: -350rpx;

			.top-select {
				display: flex;
				align-items: center;
				justify-content: space-around;
				width: 100vw;

				.search-input {
					display: flex;
					align-items: center;
					height: 64rpx;
					width: 476rpx;
					border-radius: 32rpx;
					background-color: #fff;
					padding: 0 20rpx;

					.input {
						color: #999999;
						margin-left: 10rpx;
					}
				}

				.msg-icon {
					width: 43rpx;
					height: 46rpx;
				}
			}

			.swiper {
				width: 702rpx;
				height: 288rpx;
				border-radius: 20rpx;
				margin-top: 20rpx;
				margin-left: 24rpx;
			}

			.active {
				width: 702rpx;
				background: #fff;
				box-shadow: 2rpx 2rpx 20rpx 0rpx rgba(118, 118, 118, 0.25);
				border-radius: 20rpx;
				margin-left: 24rpx;
				padding: 20rpx;
				margin-top: 30rpx;

				&-title {
					display: flex;

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
					width: 100%;
					margin-top: 20rpx;
					display: flex;

					&__item {
						flex: 1;
						display: flex;
						flex-direction: column;
						align-items: center;

						img {
							width: 84rpx;
							height: 84rpx;
						}

						text {
							color: #333;
							font-size: 28rpx;
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
		.originalPrice{
			text-decoration: line-through;
			color: #999999;
			font-size: 28rpx;
			font-weight: 400;
			margin-left: 10rpx;
		}
	}
</style>
