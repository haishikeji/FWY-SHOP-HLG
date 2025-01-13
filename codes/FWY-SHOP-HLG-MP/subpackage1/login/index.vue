<template>
	<view class="page">
		<img class="head" src="../../static/logo.png" />
		<view class="dev-name">商城</view>
		<view class="msg">程序由该平台开发，请确认授权以下信息</view>
		<view class="msg1">获得你的公开信息（昵称、头像等）</view>
		<button class="button" @click="wxLogin">
			<u-icon name="weixin-fill" color="#fff" size="40"></u-icon>
			<span style="margin-left: 10rpx">微信授权登录</span>
		</button>
		<view class="button1" @click="cancle">取消</view>
	</view>
</template>
<script>
	import wxlogin from '../../api/wxLogin/index';
	import user from '../../api/user/index';
	export default {
		onLoad() {},
		data() {
			return {};
		},
		computed: {},
		methods: {
			wxLogin() {
				uni.getUserProfile({
					desc: '资料用于个人中心展示',
					success: (userInfo) => {
						console.log(userInfo, '获取信息到了吗？');
						// 登录
						uni.login({
							success: (res) => {
								// 使用code获取openId
								wxlogin
									.login({
										code: res.code,
									})
									.then((result) => {
										console.log(result, '获取code到了吗？');
										return wxlogin.authorizeLogin({
											avatarUrl: userInfo.userInfo.avatarUrl,
											gender: userInfo.userInfo.gender,
											nickName: userInfo.userInfo.nickName,
											openId: result.data.openid,
										});
									})
									.then((res) => {
										console.log(res, '授权是否成功');
										// 保存token到storage
										return uni
											.setStorage({
												key: 'token',
												data: res.data.token,
											})
											.then(() => {
												return uni.setStorage({
													key: 'userInfo',
													data: res.data
														.loginAppletUserVo,
												});
											})
											.then(() => {
												uni.reLaunch({
													url: `/pages/index/index`,
												});
											});
									})
									.catch((err) => {
										uni.hideLoading();
										uni.showToast({
											title: err,
											icon: 'none'
										});
									});
							},
						});
					},
					fail: (result) => {
						uni.hideLoading();
						uni.showToast({
							title: '微信登录授权失败',
							icon: 'none',
						});
					},
				});
			},
			cancle() {
				uni.reLaunch({
					url: `/pages/index/index`,
				});
			},
		},
	};
</script>

<style lang="scss" scoped>
	* {
		font-family: Microsoft YaHei;
	}

	.page {
		display: flex;
		flex-direction: column;
		align-items: center;

		.head {
			width: 144rpx;
			height: 144rpx;
			margin-top: 215rpx;
		}

		.dev-name {
			font-size: 30rpx;
			color: #101010;
			font-family: Microsoft YaHei;
			margin-top: 20rpx;
		}

		.msg {
			color: #101010;
			margin-top: 157rpx;
			font-weight: bold;
			font-size: 32rpx;
		}

		.msg1 {
			color: #aeaeae;
			font-size: 30rpx;
			margin-top: 40rpx;
		}

		.button {
			width: 588rpx;
			height: 88rpx;
			background-color: #07c160;
			display: flex;
			justify-content: center;
			align-items: center;
			color: #fff;
			font-size: 32rpx;
			margin-top: 78rpx;
			border-radius: 10rpx;
		}

		.button1 {
			width: 588rpx;
			height: 88rpx;
			background-color: #eee;
			display: flex;
			justify-content: center;
			align-items: center;
			color: #666;
			font-size: 32rpx;
			margin-top: 30rpx;
			border-radius: 10rpx;
		}
	}
</style>