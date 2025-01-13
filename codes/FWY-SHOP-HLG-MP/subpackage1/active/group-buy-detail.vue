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
				<u-swiper :list="swiperList" :height="750"></u-swiper>
			</view>
			<view class="price-info">
				<view class="layout">
					<view class="flex-container">
						<view>拼团价￥</view>
						<view>{{ goodInfo.groupWorkPrice }}</view>
						<view>{{ goodInfo.numberParticipants }}人拼(已有{{
                goodInfo.userNumJoin
              }}人参团)</view>
					</view>
					<view style="color: #666666; font-size: 28rpx">距离拼团结束</view>
				</view>
				<view class="layout">
					<view style="text-decoration: line-through">售价￥{{ goodInfo.sellPrice }}</view>
					<view v-html="goodInfo.countDown"></view>
				</view>
			</view>
			<view class="spec">
				<view class="spec-layout">
					<view class="tag">{{ shopType(goodInfo.shopType) }}</view>
					<view class="name">{{ goodInfo.commodityName }}</view>
				</view>
				<view class="code">编码：{{ goodInfo.commodityCode }}</view>
			</view>
			<view class="count">
				<view style="color: #666">数量</view>
				<u-number-box :input-width="80" :input-height="40" :disabled-input="true" bg-color="#00B0D0"
					color="#fff" :min="1" :max="specificationList[0].stock" v-model="numberboxCount"></u-number-box>
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
				<view class="detail__content" v-html="goodInfo.commodityDetail"></view>
			</view>
		</view>

		<view class="bottom-button">
			<button class="chat" open-type="contact">
				<img src="/static/icons/9.png" />
			</button>
			<view class="buttons">
				<view class="solo-buy confirm-button" @click="singleBuy">单独购买</view>
				<view class="immediately-buy confirm-button" :class="{ disabled: isJoinGroup.code != 200 }"
					@click="groupBuy">{{ confirmGroup }}</view>
			</view>
		</view>
		<view>
			<u-popup v-model="show" mode="bottom" border-radius="20">
				<view class="popup">
					<view class="goodsShow">
						<img :src="goodInfo.coverUrl" alt="" class="goodsImg" />
						<view class="goodsInfo">
							<view class="goodsName">{{ goodInfo.commodityName }}</view>
							<view class="goodsPrice" v-if="confirmMode == 0">￥{{ specificationSellPrice }}</view>
							<view class="goodsPrice" v-else>￥{{ goodInfo.groupWorkPrice}}</view>
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
						<view style="color: #666">数量</view>
						<u-number-box :disabled-input="true" bg-color="#00B0D0" color="#fff" :min="1"
							:max="specificationList[0].stock" v-model="numberboxCount"></u-number-box>
					</view>
					<button @click="toConfirmPushShopCar">确认</button>
				</view>
			</u-popup>
		</view>
	</view>
</template>
<script>
	import shopCar from '../../api/shopCar/index';
	import good from '../../api/good/index';
	import util from '@/util/util';
	import browsing from '@/api/browsing/index';
	import shop from '@/api/shop/index';
	export default {
		onLoad(param) {
			this.getDetails(param.id);
			this.goodsId = param.id;
			this.isGroup();
			this.toAddBrowsing();
			this.getSpecificationList();
		},
		onUnload() {
			clearInterval(this.interval);
		},
		computed: {
			confirmGroup() {
				return this.isJoinGroup.code == 200 ?
					this.goodInfo.isSuccess == 0 ?
					'我要拼团' :
					'我要开团' :
					'已参团';
			},
		},
		data() {
			return {
				tabList: [{
						name: '商品',
					},
					{
						name: '详情',
					},
				],
				confirmMode: 0, //0为单独购买，1为拼团
				activeChoose: 0,
				show: false,
				swiperList: [],
				current: 0,
				numberboxCount: 1,
				goodInfo: null,
				goodsId: '',
				specificationList: [],
				goodInfo: {},
				specificationSellPrice: '',
				specificationOriginalPrice: '',
				isJoinGroup: {},
			};
		},
		methods: {
			// 获取弹出层中商品规格列表
			getSpecificationList() {
				shopCar
					.getSpecificationList({
						commodityId: this.goodsId
					})
					.then((res) => {
						this.specificationList = res.data.map((item) => {
							return {
								name: item.specificationName,
								value: item.id,
								sellPrice: item.sellPrice,
								originalPrice: item.originalPrice,
								stock: item.stock
							};
						});
						this.activeChoose = this.specificationList[0].value;
						this.specificationSellPrice = this.specificationList[0].sellPrice;
						this.specificationOriginalPrice = this.specificationList[0].originalPrice;
					});
			},
			// 默认当前规格选中目标
			choseStandards(val, index) {
				this.activeChoose = val;
				this.specificationSellPrice = this.specificationList[index].sellPrice;
				this.specificationOriginalPrice = this.specificationList[
					index
				].originalPrice;
			},
			// 单独购买
			singleBuy() {
				if(this.goodInfo.date< 0) {
					uni.showToast({
						title: '拼团活动已结束',
						icon: 'none'
					})
					setTimeout(function() {
						uni.navigateTo({
							url: "/subpackage1/active/group-buy"
						})
					}, 2000)
					
					return false
				}
				if (this.specificationList[0].stock == 0) {
					uni.showToast({
						title: '已售罄',
						icon: 'none'
					})
					return false
				}
				this.show = true;
				this.confirmMode = 0; //用于在判断
			},
			// 拼团购买
			groupBuy() {
				if(this.goodInfo.date< 0) {
					uni.showToast({
						title: '拼团活动已结束',
						icon: 'none'
					})
					setTimeout(function() {
						uni.navigateTo({
							url: "/subpackage1/active/group-buy"
						})
					}, 2000)
					return false
				}
				if (this.specificationList[0].stock == 0) {
					uni.showToast({
						title: '已售罄',
						icon: 'none'
					})
					return false
				}
				// 判断用户是否已经参团
				if (this.isJoinGroup.code == 200) {
					this.show = true;
					this.confirmMode = 1;
				} else {
					uni.showToast({
						title: this.isJoinGroup.msg,
						icon: 'none'
					});
				}
			},
			// 去付款
			toConfirmPushShopCar() {
				let data = {
					specificationId: this.activeChoose,
					way: this.confirmMode,
					specificationName: this.specificationList.filter((item) => {
						return item.value == this.activeChoose;
					})[0].name,
					purchaseQuantity: this.numberboxCount,
					shopInfo: this.shopInfo,
					goodInfo: this.goodInfo,
				};
				uni
					.setStorage({
						key: 'orderInfo',
						data,
					})
					.then(() => {
						uni.setStorage({
							key: 'isActiveGood',
							data: true,
						});
					})
					.then(() => {
						uni.navigateTo({
							url: `/subpackage1/order/confirm-order`,
						});
						// delivery_time在提交订单时用于判断是否是及时送类型
						// 所以非及时送商品时要移除
						uni.removeStorageSync('delivery_time');
					});
			},
			cancle() {
				this.show = false;
			},
			tabChange(index) {
				this.current = index;
			},
			//添加浏览记录
			toAddBrowsing() {
				browsing.record({
					commodityId: this.goodsId
				}).then((res) => {});
			},
			shopType(type) {
				return ['自营', '旗舰店', '其他'][type];
			},
			getDetails(id) {
				uni.showLoading();
				good.getGroupDetail({
					commodityId: id
				}).then((res) => {
					this.goodInfo = {
						...res.data,
						countDown: util.openCountDown(res.data.endTime),
						date: new Date(res.data.endTime).getTime() - new Date().getTime()
					};
					if (this.interval) clearInterval(this.interval);
					this.interval = setInterval(() => {
						this.goodInfo.countDown = util.openCountDown(
							res.data.endTime.replace(/\-/g, '/')
						);
						this.goodInfo.date = new Date(res.data.endTime).getTime() - new Date().getTime()
					}, 1000);
					this.swiperList = res.data.carouselUrl.split(',').map((item) => {
						return {
							image: item,
						};
					});
					uni.hideLoading();
					shop.getShopInfo({
						shopId: res.data.shopId
					}).then((result) => {
						this.shopInfo = {
							shopBusinessTime: result.data.shopInfo.businessHours
								.split(',')
								.map((item) => {
									let format = (num) => {
										return num >= 10 ? num : `0${num}`;
									};
									let hour = new Date(item).getHours();
									let minu = new Date(item).getMinutes();
									return `${format(hour)}:${format(minu)}`;
								})
								.join('~'),
							shopPhone: result.data.shopInfo.customerServicePhone,
						};
					});
				});
			},
			isGroup() {
				good.checkGroup({
					commodityId: this.goodsId
				}).then((res) => {
					this.isJoinGroup = res;
				});
			},
		},
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
						}

						&:nth-of-type(2) {
							font-size: 40rpx;
							margin-right: 20rpx;
							font-weight: bold;
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
			width: 702rpx;
			margin-left: 24rpx;

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
					width: 570rpx;
					font-size: 30rpx;
					color: #333;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					font-weight: bold;
				}
			}

			.code {
				margin: 20rpx 0;
				font-size: 26rpx;
				color: #666;
			}
		}

		.count {
			width: 702rpx;
			line-height: 88rpx;
			margin-left: 24rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
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
		padding: 0 24rpx;
		position: fixed;
		bottom: 0;
		left: 0;

		.chat {
			border: 0;
			background-color: transparent;
			outline: 0;
			padding-top: 16rpx;
			margin: 0;
			padding-left: 0;
			padding-right: 0;
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
				background: linear-gradient(to right,
						rgb(247, 103, 76),
						rgb(247, 80, 41));
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
		height: 740rpx;
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
					font-weight: bold;
				}

				.goodsPrice {
					margin: 10rpx;
					font-size: 32rpx;
					color: #ec203f;
					font-weight: bold;
				}
			}
		}

		.standards-list::-webkit-scrollbar {
			display: none;
		}

		.standards-list {
			height: 240rpx;
			overflow: scroll;

			&_item {
				height: 62rpx;
				border-radius: 20rpx;
				border: 2rpx solid #d2d3d4;
				margin: 16rpx 0;
				text-align: center;
				line-height: 60rpx;
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
</style>
