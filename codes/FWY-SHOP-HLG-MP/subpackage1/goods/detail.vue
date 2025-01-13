<template>
	<view class="page">
		<!-- <u-tabs
      class="tabs"
      ref="tabs"
      :list="tabList"
      :is-scroll="false"
      :current="current"
      active-color="#00B0D0"
      @change="tabChange"
    ></u-tabs> -->
		<view class="container" v-if="goodInfo">
			<view class="swiper">
				<u-swiper :list="swiperList" :height="560"></u-swiper>
			</view>
			<view class="price-info">
				<view class="layout">
					<view class="flex-container">
						<!-- <view v-if="userInfo.isShopowner && goodInfo.shopownerPrice !== 0">
							￥{{ goodInfo.shopownerPrice }}</view> -->
						<view>￥{{ goodInfo.sellPrice }}</view>
					</view>
				</view>
				<view class="layout">
					<view style="text-decoration: line-through">原价￥{{ goodInfo.originalPrice }}</view>
				</view>
			</view>
			<view class="spec">
				<view class="spec-layout">
					<view class="tag">{{ shopType(goodInfo.shopType) }}</view>
					<view class="name">{{ goodInfo.commodityName }}</view>
				</view>
				<view class="code">编号:{{ goodInfo.commodityCode }}</view>
			</view>
			<!-- <view class="count">
        <view style="color: #666">数量</view>
        <u-number-box
          :disabled-input="true"
          bg-color="#00B0D0"
          color="#fff"
          v-model="numberboxCount"
        ></u-number-box>
      </view> -->
			<!-- <view class="distribution">
        <view style="color: #666666; flex: 1 1 108rpx">配送</view>
        <view class="distribution_address">
          <text>{{ goodInfo.shopAddress }}</text>
          <text>送至</text>
          <text>{{ locationInfo.address }}</text>
          <text v-if="mark == 'fromTimely'">预计{{ deliveryTime }}分钟送达</text>
          <text v-else></text>
        </view>
      </view> -->
			<view class="get-coupon" v-if="goodInfo.couponName == null" @click="goToCoupon">
				<view style="display: flex; align-items: center">
					<view class="get-coupon-item">领券</view>
					<u-tag class="get-coupon-item" size="mini" text="暂无可用优惠券" color="#EC1B3B" bg-color="#fff"
						border-color="#EC1B3B" />
				</view>
				<view class="arrow"></view>
			</view>
			<view class="get-coupon" @click="goToCoupon" v-else>
				<view style="display: flex; align-items: center">
					<view class="get-coupon-item">领券</view>
					<u-tag class="get-coupon-item" size="mini" :text="goodInfo.couponName" color="#EC1B3B"
						bg-color="#fff" border-color="#EC1B3B" />
				</view>
				<view class="arrow"></view>
			</view>
			<view class="service">
				<view class="service-item">服务</view>
				<view v-for="item in goodInfo.serviceVoList" :key="item.id">
					<u-tag class="service-item" size="mini" :text="item.serviceName" color="#00B0D0" bg-color="#fff"
						border-color="#00B0D0" />
				</view>
			</view>
			<view class="good-detail">
				<view class="detail__title">商品详情</view>
				<!-- <view class="detail__content" v-html="goodInfo.commodityDetail"></view> -->
				<u-parse class="detail__content" :html="goodInfo.commodityDetail"></u-parse>
			</view>
		</view>

		<view class="bottom-button">
			<img src="/static/icons/11.png" @click="goToShop" />
			<button class="chat" open-type="contact"><img src="/static/icons/9.png" /></button>
			<img src="/static/icons/12.png" @click="goToShopCar" />
			<view class="buttons">
				<view class="solo-buy confirm-button" @click="pushShopCar">加入购物车</view>
				<view v-if="mark == 'fromTimely'" class="immediately-buy confirm-button"
					:class="isOpen ? null : 'disabled'" @click="goToBuy">{{ isOpen ? '立即购买' : '已打烊' }}</view>
				<view v-else class="immediately-buy confirm-button" @click="goToBuy">立即购买</view>
			</view>
		</view>
		<view>
			<u-popup v-model="show" mode="bottom" border-radius="20">
				<view class="popup">
					<view class="goodsShow">
						<img :src="goodInfo.coverUrl" alt="" class="goodsImg" />
						<view class="goodsInfo">
							<view class="goodsName">{{ goodInfo.commodityName }}</view>
							<!-- <view class="goodsPrice" v-if="userInfo.isShopowner && shopownerPrice != 0">
								￥{{ shopownerPrice }}</view> -->
							<view class="goodsPrice">￥{{ specificationPrice }} <text class="originalPrice">￥{{ goodInfo.originalPrice }}</text></view>
						</view>
						<img src="../../static/icons/18.png" alt="" class="cancle" @click="cancle" />
					</view>
					<text style="color: #666">规格</text>
					<view class="standards-list">
						<view class="standards-list_item" v-for="(item, index) in specificationList" :key="item.value"
							@click="choseStandards(item.value, index)"
							:class="{ 'active-item': activeChoose == item.value }">
							{{ item.name }}
						</view>
					</view>
					<view class="goodsNum">
						<view style="color: #666">数量({{unit}})</view>
						<u-number-box :disabled-input="true" bg-color="#00B0D0" color="#fff" :min="1" :max="stock"
							v-model="numberboxCount"></u-number-box>
					</view>
					<button @click="toConfirmPushShopCar">确认</button>
				</view>
			</u-popup>
		</view>
	</view>
</template>
<script>
	import good from '../../api/good/index';
	import shopCar from '../../api/shopCar/index';
	import browsing from '@/api/browsing/index';
	import shop from '@/api/shop/index';
	export default {
		onLoad(param) {
			this.locationInfo = uni.getStorageSync('location');
			this.goodsId = param.id;
			this.mark = param.mark;
			this.getSpecificationList();
			this.getDetails();
			this.toAddBrowsing();
		},
		data() {
			return {
				confirmMode: 0, // 0为加入购物车，1为立即购买
				goodsId: '',
				show: false,
				activeChoose: 0,
				tabList: [{
						name: '商品'
					},
					{
						name: '详情'
					}
				],
				swiperList: [],
				mark: '', // 判断是否是立即送商品
				current: 0,
				numberboxCount: 1,
				goodInfo: null,
				locationInfo: {},
				specificationList: [],
				deliveryTime: '',
				isOpen: true,
				shopInfo: {},
				specificationPrice: '',
				shopownerPrice: '', // 会员价格
				userInfo: uni.getStorageSync('userInfo'),
				stock: 0,
				unit: '',
				originalPrice: 0,
			};
		},
		onShareAppMessage() {
			var that = this;
			// 设置菜单中的转发按钮触发转发事件时的转发内容
			var shareObj = {
				title: "商城", // 默认是小程序的名称(可以写slogan等)
				path: '/subpackage1/goods/detail?id=' + this.goodsId, // 默认是当前页面，必须是以‘/’开头的完整路径
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
				path: '/subpackage1/goods/detail?id=' + this.goodsId, // 默认是当前页面，必须是以‘/’开头的完整路径
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
			shopType(type) {
				return ['自营', '旗舰店', '其他'][type];
			},
			getDetails() {
				uni.showLoading();
				good
					.getDetails({
						commodityId: this.goodsId
					})
					.then(res => {
						this.goodInfo = res.data;
						this.swiperList = res.data.carouselUrl.split(',').map(item => {
							return {
								image: item
							};
						});
						uni.getStorage({
							key: 'delivery_time',
							success: res => {
								this.deliveryTime = res.data;
							}
						});
						uni.hideLoading();
						return shop.getShopInfo({
							shopId: this.goodInfo.shopId
						});
					})
					.then(res => {
						// 判断当前时间是否在营业时间区间内
						let businessTime = res.data.shopInfo.businessHours.split(',').map(item => {
							let hour = new Date(item).getHours();
							let minu = new Date(item).getMinutes() / 60;
							return hour + minu;
						});
						let nowTime = new Date().getHours() + new Date().getMinutes() / 60;
						this.isOpen = nowTime >= businessTime[0] && nowTime <= businessTime[1];
						// 处理店铺数据用于传给确认订单页
						this.shopInfo = {
							shopBusinessTime: res.data.shopInfo.businessHours
								.split(',')
								.map(item => {
									let format = num => {
										return num >= 10 ? num : `0${num}`;
									};
									let hour = new Date(item).getHours();
									let minu = new Date(item).getMinutes();
									return `${format(hour)}:${format(minu)}`;
								})
								.join('~'),
							shopPhone: res.data.shopInfo.customerServicePhone
						};
					});
			},
			getSpecificationList() {
				shopCar.getSpecificationList({
					commodityId: this.goodsId
				}).then(res => {
					console.log(res.data, 123123);
					this.specificationList = res.data.map(item => {
						return {
							name: item.specificationName,
							value: item.id,
							price: item.sellPrice,
							shopownerPrice: item.shopownerPrice,
							stock: item.stock,
							unit: item.unit,
							originalPrice: item.originalPrice
						};
					});
					this.activeChoose = this.specificationList[0].value;
					this.specificationPrice = this.specificationList[0].price;
					this.shopownerPrice = this.specificationList[0].shopownerPrice;
					this.stock = this.specificationList[0].stock;
					this.unit = this.specificationList[0].unit;
					this.originalPrice = this.specificationList[0].originalPrice;
					this.numberboxCount = 1;
				});
			},
			choseStandards(val, index) {
				this.activeChoose = val;
				this.shopownerPrice = this.specificationList[index].shopownerPrice;
				this.specificationPrice = this.specificationList[index].price;
				this.stock = this.specificationList[index].stock;
				this.unit = this.specificationList[index].unit;
				this.originalPrice = this.specificationList[index].originalPrice;
				this.numberboxCount = 1;
				// this.shopInfo = this.specificationList[index]
			},
			pushShopCar() {
				this.show = true;
				this.confirmMode = 0;
			},
			goToBuy() {
				this.show = true;
				this.confirmMode = 1;
			},
			toConfirmPushShopCar() {
				if (this.confirmMode == 0) {
					// 加入购物车
					shopCar
						.add({
							commodityId: this.goodsId,
							specificationId: this.activeChoose,
							purchaseQuantity: this.numberboxCount
						})
						.then(res => {
							if (res.code == 200) {
								uni.showToast({
									title: res.msg
								});
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
								});
							}
							this.show = false;
						})
						.catch(err => {
							console.log(err);
						});
				} else if (this.confirmMode == 1) {
					// 立即购买
					let data = {
						specificationId: this.activeChoose,
						specificationName: this.specificationList.filter(item => {
							return item.value == this.activeChoose;
						})[0].name,
						purchaseQuantity: this.numberboxCount,
						shopInfo: this.shopInfo,
						goodInfo: this.goodInfo,
						specificationPrice: this.specificationPrice,
						shopownerPrice: this.shopownerPrice
					};
					uni.setStorage({
						key: 'orderInfo',
						data,
						success: () => {
							if (this.mark) {
								uni.setStorage({
									key: 'isActiveGood',
									data: true,
									success: () => {
										uni.navigateTo({
											url: `/subpackage1/order/confirm-order`
										});
									}
								});
							} else {
								uni.setStorage({
									key: 'isActiveGood',
									data: false,
									success: () => {
										uni.navigateTo({
											url: `/subpackage1/order/confirm-order`
										});
									}
								});
							}
							// delivery_time在提交订单时用于判断是否是及时送类型
							// 所以非及时送商品时要移除
							if (this.mark != 'fromTimely') {
								uni.removeStorageSync('delivery_time');
							}
						}
					});
				}
			},
			tabChange(index) {
				this.current = index;
			},
			goToShop() {
				uni.navigateTo({
					url: `/subpackage1/shop/index?id=${this.goodInfo.shopId}`
				});
			},
			goToShopCar() {
				uni.switchTab({
					url: `/pages/index/shopCar`
				});
			},
			toAddBrowsing() {
				browsing.record({
					commodityId: this.goodsId
				}).then(res => {});
			},
			goToCoupon() {
				uni.navigateTo({
					url: `/subpackage1/coupon/coupon?id=${this.goodInfo.typeId}`
				});
			},
			cancle() {
				this.show = false;
				
			}
		}
	};
</script>
<style lang="scss" scoped>
	.container {
		height: calc(100vh - 98rpx);
		overflow-y: auto;
		position: fixed;
		top: 0rpx;
		bottom: 98rpx;
		left: 0;

		.swiper {
			width: 100vw;
		}

		.price-info {
			width: 702rpx;
			margin-left: 24rpx;
			margin-top: 20rpx;

			.layout {
				display: flex;
				justify-content: space-between;
				align-items: center;
				line-height: 60rpx;

				.flex-container {
					display: flex;
					align-items: center;

					view {
						color: #ec1b3b;

						&:nth-of-type(1) {
							font-size: 30rpx;
							font-weight: bolder;
						}

						&:nth-of-type(2) {
							font-size: 40rpx;
							margin-right: 20rpx;
						}

						&:nth-of-type(3) {
							font-size: 28rpx;
							color: #999;
						}
					}
				}

				&:nth-of-type(2) {
					view {
						&:nth-of-type(1) {
							color: #999;
							font-size: 28rpx;
						}

						&:nth-of-type(2) {
							color: #f76950;
							font-size: 30rpx;
						}
					}
				}
			}
		}

		.spec {
			// display: flex;
			// align-items: center;
			// justify-content: space-between;
			width: 702rpx;
			margin-left: 24rpx;
			border-bottom: 2rpx solid #dddddd;
			padding: 20rpx;

			&-layout {
				display: flex;
				align-items: center;

				.tag {
					padding: 0 10rpx;
					height: 33rpx;
					line-height: 33rpx;
					text-align: center;
					background-color: #ec1b3b;
					color: #fff;
					font-size: 24rpx;
					margin-right: 20rpx;
				}

				.name {
					width: 550rpx;
					font-size: 30rpx;
					color: #333;
					font-weight: bolder;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
				}
			}

			.code {
				margin: 20rpx 0;
				font-size: 26rpx;
				color: #666;
			}
		}

		.distribution {
			width: 702rpx;
			margin-left: 24rpx;
			display: flex;
			justify-content: space-between;
			padding: 30rpx 0;
			border-bottom: 2rpx solid #ddd;

			&_address {
				flex: 1 1 604rpx;
				font-size: 28rpx;

				text {
					&:nth-of-type(1) {
						color: #666;
						font-weight: bolder;
					}

					&:nth-of-type(2) {
						color: #00b0d0;
						margin: 0 10rpx;
					}

					&:nth-of-type(3) {
						color: #333;
						padding-right: 20rpx;
						border-right: 2rpx solid #333;
					}

					&:nth-of-type(4) {
						color: #00b0d0;
						padding-left: 20rpx;
					}
				}
			}
		}

		.count {
			width: 702rpx;
			line-height: 88rpx;
			margin-left: 24rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 2rpx solid #dddddd;
		}

		.get-coupon {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-left: 24rpx;
			width: 702rpx;
			line-height: 88rpx;

			&-item {
				margin-right: 16rpx;

				&:nth-of-type(1) {
					font-size: 28rpx;
					color: #666;
				}
			}
		}

		.good-detail {
			width: 100vw;
			overflow-x: hidden;

			.detail__title {
				height: 30rpx;
				line-height: 30rpx;
				font-size: 28rpx;
				padding-left: 20rpx;
				margin: 30rpx 0 30rpx 24rpx;
				border-left: 6rpx solid #00b0d0;
			}

			.detail__content {
				padding: 0 20rpx;
				width: 100vw;
			}
		}
	}

	.bottom-button {
		display: flex;
		align-items: center;
		justify-content: space-between;
		height: 98rpx;
		width: 100vw;
		padding: 0 28rpx;
		position: fixed;
		bottom: 0;
		left: 0;

		.chat {
			border: 0;
			background-color: transparent;
			outline: 0;
			margin: 0;
			padding-left: 0;
			padding-right: 0;
			// width: 46rpx;
			// height: 46rpx;
			display: flex;
			align-items: center;
		}

		button:after {
			border: none;
		}

		img {
			width: 46rpx;
			height: 46rpx;
		}

		.buttons {
			display: flex;

			.solo-buy {
				width: 210rpx;
				height: 74rpx;
				font-size: 32rpx;
				border-radius: 40rpx 0 0 40rpx;
				background: linear-gradient(to right, rgb(247, 103, 76), rgb(247, 80, 41));
			}

			.immediately-buy {
				width: 210rpx;
				height: 74rpx;
				font-size: 32rpx;
				border-radius: 0 40rpx 40rpx 0;
			}
		}
	}

	.popup {
		margin: 30rpx;

		&::-webkit-scrollbar {
			display: none;
			width: 0;
		}

		.goodsShow {
			display: flex;
			padding: 28rpx;
			justify-content: space-between;

			.goodsImg {
				width: 200rpx;
				height: 200rpx;
			}

			.cancle {
				width: 40rpx;
				height: 40rpx;
				margin-left: 10rpx;
			}

			.goodsInfo {
				padding: 10rpx;

				.goodsName {
					width: 320rpx;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					margin: 20rpx;
					font-size: 30rpx;
					color: #333;
					font-weight: bolder;
				}

				.goodsPrice {
					margin: 10rpx;
					font-size: 32rpx;
					color: #ec203f;
					font-weight: bolder;
				}
			}
		}

		.standards-list::-webkit-scrollbar {
			display: none;
		}

		.standards-list {
			&_item {
				height: 62rpx;
				border-radius: 20rpx;
				border: 2rpx solid #d2d3d4;
				margin: 16rpx 0;
				text-align: center;
				line-height: 60rpx;
				padding: 0 10rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.active-item {
				color: #00b0d0;
				font-size: 28rpx;
				border: 2rpx solid #00b0d0;
			}
		}

		.goodsNum {
			height: 76rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			border: 0;
			border-top: 1px solid #d2d3d4;
			border-bottom: 1px solid #d2d3d4;
		}

		button {
			width: 100%;
			height: 76rpx;
			background-color: #1ebbd7;
			margin: 14rpx;
			border-radius: 40rpx;
			color: white;
			line-height: 76rpx;
		}
	}

	.disabled {
		background: #eee;
		pointer-events: none;
	}

	.service {
		display: flex;
		align-items: center;
		margin-left: 24rpx;
		line-height: 88rpx;

		&-item {
			margin-right: 16rpx;

			&:nth-of-type(1) {
				font-size: 28rpx;
				color: #666;
			}
		}
	}
	.originalPrice{
		text-decoration: line-through;
		font-size: 28rpx;
		color: #999999;
		font-weight: 400;
	}
</style>
