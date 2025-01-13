<template>
  <div class="page">
    <el-button
      @click="startCountdown()"
      v-if="!isCountdown"
      type="primary"
      style="margin-left: 6px"
      >{{ buttonText }}</el-button
    >
    <el-button
      style="margin-left: 6px; width: 112px;height:54px; color: #DCDFE6"
      v-if="isCountdown"
      >{{ count }}</el-button
    >
  </div>
</template>

<script>
export default {
  props: {
    countdown: {
      type: Number,
      default: 60,
    },
    buttonText: {
      type: String,
      default: '获取验证码',
    },
    callback: {
      type: Function,
    },
    /**
     * 若在特定条件下才能触发倒计时，将这个条件传入
     *   例如：获取手机验证码，需要输入手机号才能获取
     *         此时将手机号传入isError属性
     * */
    isError: {
      type: String,
      default: '',
    },
  },

  data() {
    return {
      isCountdown: false,
      count: 0,
    };
  },

  methods: {
    startCountdown() {
      this.callback();
      if (!this.isError) {
        return false;
      }
      this.count = this.countdown;
      this.isCountdown = true;
      let interval = setInterval(() => {
        this.count--;
        if (this.count == 0) {
          this.isCountdown = false;
          this.count = this.countdown;
          clearInterval(interval);
        }
      }, 1000);
    },
  },
};
</script>

<style lang="less" scoped>
.el-button--primary {
  height: 54px;
  color: #fff;
  background-color: #0082cc;
  border-color: #0082cc;
}
</style>
