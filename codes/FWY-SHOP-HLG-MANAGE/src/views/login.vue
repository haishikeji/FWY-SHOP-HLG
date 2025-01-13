<template>
  <div class="login_bg">
    <div class="content_body">
      <div class="code_login" v-show="!forget">
        <p style="margin:30px 0 50px 0;font-size:32px;font-weight:500">
          商城管理后台
        </p>
        <el-form
          :model="loginForm"
          ref="loginForm"
          :rules="loginFormRules"
          label-width="55px"
          label-position="left"
          hide-required-asterisk
          v-show="!forget"
        >
          <el-form-item prop="loginAccount" label="账号">
            <el-input
              type="text"
              v-model="loginForm.loginAccount"
              placeholder="请输入账号"
              clearable
              class="qer_input"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input
              v-model="loginForm.password"
              placeholder="请输入密码"
              type="password"
              clearable
              show-password
              class="qer_input"
            ></el-input>
          </el-form-item>
          <!-- <div class="vc-line">
            <el-form-item prop="QRcode" label="验证码">
              <el-input
                type="text"
                class="vcCode"
                placeholder="请输入验证码"
                :maxlength="4"
                v-model="loginForm.QRcode"
                clearable
                @keyup.native.enter="handleLogin('loginForm')"
                style="width: 170px;margin-right:5px"
              ></el-input>
            </el-form-item>
            <div class="ImgBox" style="cursor: pointer;" @click="getQRCode">
              <el-image :src="YzCode" v-loading="codeLoading">
                <div slot="error" class="image-slot"></div>
              </el-image>
            </div>
          </div> -->
        </el-form>
        <div class="login">
          <el-button
            type="primary"
            class="login_btn"
            @click="handleLogin('loginForm')"
            >登录</el-button
          >
        </div>
      </div>
      <div class="code_login" v-show="forget">
        <p style="margin-bottom:30px;font-size:48px;font-weight:bold">
          找回密码
        </p>
        <el-form
          :model="forgetForm"
          ref="forgetForm"
          :rules="forgetFormRules"
          label-width="70px"
          label-position="left"
          hide-required-asterisk
          v-show="forget"
        >
          <el-form-item prop="phone" label="手机号">
            <el-input
              type="text"
              v-model="forgetForm.phone"
              placeholder="请输入手机号"
              clearable
              class="qer_input"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password" label="新密码">
            <el-input
              v-model="forgetForm.password"
              placeholder="请输入新密码"
              type="password"
              clearable
              show-password
              class="qer_input"
            ></el-input>
          </el-form-item>
          <el-form-item prop="confirmpassword" label="确认密码">
            <el-input
              v-model="forgetForm.confirmpassword"
              placeholder="请确认新密码"
              type="password"
              clearable
              show-password
              class="qer_input"
            ></el-input>
          </el-form-item>
          <div class="vc-line">
            <el-form-item prop="optCode" label="验证码">
              <el-input
                type="text"
                class="vcCode"
                placeholder="请输入验证码"
                v-model="forgetForm.optCode"
                clearable
                @keyup.native.enter="handleLogin('forgetForm')"
                style="width: 170px;margin-right:5px"
              ></el-input>
            </el-form-item>
            <countdown-button
              :callback="oldPhoneVerifyCode"
              :isError="forgetForm.phone"
            ></countdown-button>
          </div>
        </el-form>
        <div
          @click="goBackLogin"
          style="margin:10px 0;cursor:pointer;color:#0082cc"
        >
          返回登录
        </div>
        <div class="login">
          <el-button
            type="primary"
            class="login_btn"
            @click="handleLogin('forgetForm')"
            >确认</el-button
          >
        </div>
      </div>
      <!-- <div class="shadow-white"></div>
    <div class="shadow-blue"></div> -->
    </div>
  </div>
</template>
<script>
import serve from "@/api/system/menu";
import login from "@/api/login/login";
import common from "@/api/common/common";
const _import = (file) => () => import("@/views/" + file + ".vue");
export default {
  components: {
    countdownButton: () =>
      import("@/components/countdownButton/countdownButton"),
    commenFoot: () => import("@/components/commen_foot"),
  },
  name: "login",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.forgetForm.confirmpassword !== "") {
          this.$refs.forgetForm.validateField("confirmpassword");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.forgetForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      forget: false,
      loginForm: {
        loginAccount: "",
        password: "",
        QRcode: "1234",
      },
      forgetForm: {
        phone: "",
        password: "",
        confirmpassword: "",
        optCode: "",
      },
      YzCode: "",
      codeLoading: "",
      loginFormRules: {
        loginAccount: [
          { required: true, message: "请输入登录名称", trigger: "blur" },
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: "请输入登录密码", trigger: "blur" },
          {
            min: 6,
            max: 15,
            message: "长度在 6 到 15 个字符",
            trigger: "blur",
          },
        ],
        // QRcode: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
      forgetFormRules: {
        phone: [{ required: true, message: "请输入登录名称", trigger: "blur" }],
        // 验证密码是否合法
        password: [
          { required: true, message: "请输入登录密码", trigger: "blur" },
          {
            min: 6,
            max: 15,
            message: "长度在 6 到 15 个字符",
            trigger: "blur",
          },
          { validator: validatePass, trigger: "blur" },
        ],
        confirmpassword: [
          { required: true, message: "请输入登录密码", trigger: "blur" },
          {
            min: 6,
            max: 15,
            message: "长度在 6 到 15 个字符",
            trigger: "blur",
          },
          { validator: validatePass2, trigger: "blur" },
        ],
        // optCode: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
    };
  },
  created() {
    // this.getQRCode();
  },
  mounted() {},
  methods: {
    //返回登录
    goBackLogin() {
      this.forget = false;
    },
    //手机验证码
    oldPhoneVerifyCode() {
      if (!this.forgetForm.phone) {
        this.$message.warning("请输入手机号");
        return false;
      }
      let params = {
        phone: this.forgetForm.phone,
      };
      login.phoneCode(params).then((res) => {
        if (res.code === 200) {
          this.$message.success(res.msg);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    fogetterPassword() {
      this.forget = true;
    },
    // 获取验证码
    getQRCode() {
      this.$nextTick(() => {
        this.codeLoading = true;
      });
      // var time = new Date().getTime()
      this.YzCode = "";
      // this.codeLoading = false;
      login.verifyCode().then((res) => {
        this.YzCode = res.request.responseURL;
        this.codeLoading = false;
      });
    },
    /**
     * 登录
     */
    handleLogin(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          if (formName == "loginForm") {
            let data = {
              loginName: this.loginForm.loginAccount,
              password: this.loginForm.password,
              verifyCode: this.loginForm.QRcode,
            };
            login.login(data).then((res) => {
              if (res.code == 200) {
                this.getUserInfo();
                this.getMenu();
              } else {
                this.$message.error("登录失败");
                this.getQRCode();
              }
            });
          }
          if (formName == "forgetForm") {
            let data = {
              phone: this.forgetForm.phone,
              newPassword: this.forgetForm.password,
              optCode: this.forgetForm.optCode,
            };
            login.findPassword(data).then((res) => {
              if (res.code == 200) {
                this.forget = false;
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
      this.getMenu();
    },
    //获取用户登录信息
    getUserInfo() {
      login.getInfo().then((res) => {
        if (res.code == 200) {
          sessionStorage.setItem("userInfo", JSON.stringify(res.data));
        }
      });
    },
    getMenu() {
      serve.getMenu().then((res) => {
        if (res.code == 200) {
          let menu = this.getmenu(res.data);
          let souceMenu = common.commenRouter;
          menu = [...menu, ...souceMenu];
          let router = [
            {
              path: "/home",
              name: "home",
              component: _import("home"),
              children: menu,
            },
          ];
          this.$router.addRoutes(router);
          let menuString = JSON.stringify(router);
          sessionStorage.setItem("routerlink", menuString);
          this.$store.commit("change", 10);
          sessionStorage.setItem("isLogin", 1);
          this.$router.push("/home/" + menu[0].path);
        }
      });
    },
    getmenu(list) {
      let res = [];
      for (const a of list) {
        if (Array.isArray(a.list) && a.list.length > 0) {
          res = res.concat(this.getmenu(a.list));
        } else {
          if (a.path) {
            let url = a.path;
            if (
              url.indexOf("complaint") >= 0 ||
              url.indexOf("babyList") >= 0 ||
              url.indexOf("autismList") >= 0 ||
              url.indexOf("goods") >= 0
            ) {
              let obj = {
                path: a.path,
                name: a.path,
                component: _import(a.path),
                meta: {
                  title: a.menuName,
                  isKeep: true,
                  icon: "s-home",
                },
              };
              res.push(obj);
            } else {
              let obj = {
                path: a.path,
                name: a.path,
                component: _import(a.path),
                meta: {
                  title: a.menuName,
                  isKeep: false,
                  icon: "s-home",
                },
              };
              res.push(obj);
            }
          }
        }
      }
      return res;
    },
  },
};
</script>
<style lang="less" scoped>
.body_item {
  width: 20%;
  height: 32rem;
  background: #ffffff;
}
.login_btn {
  width: 100%;
  height: 3.5rem;
  background: #0082cc;
  margin: 30px 0 50px 0;
}
.login_bg {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  overflow: hidden;
  background: linear-gradient(163.85deg, #4b94f2 0%, #4393fa 100%);
}
.header {
  flex: 0 0 12%;
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: center;
  text-align: center;
  .manager_logo {
    width: 94px;
    height: 100px;
  }
  span {
    font-size: 36px;
    font-family: FZLanTingHeiS-B-GB;
    color: #ffffff;
    font-weight: bold;
  }
}
.content_body {
  width: 500px;
  display: flex;
  position: relative;
  justify-content: center;
  margin: 200px auto 0 auto;
}
.backage-img {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}
.code_login {
  background: #ffffff;
  width: 500px;
  // height: 500px;
  padding: 0 3.125rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  border-radius: 10px;
  margin: 0 auto;
  text-align: center;
}
.login {
  text-align: center;
}
.vc-line {
  display: flex;
  justify-content: space-between;
  .ImgBox {
    width: 7.68rem;
    height: 3.5rem;
    background: #dbdbdb;
    opacity: 0.47;
    border-radius: 4px;
    .el-image {
      width: 100%;
      height: 100%;
    }
  }
}
.shadow-white {
  position: absolute;
  top: -800px;
  left: 300px;
  width: 300px;
  height: 2080px;
  background-color: rgba(229, 229, 229, 0.45);
  transform: rotate(30deg);
  // transform-origin: 0% 0%;
}
.shadow-blue {
  position: absolute;
  left: 1800px;
  bottom: -250px;
  width: 200px;
  height: 1000px;
  background-color: rgba(58, 147, 206, 0.45);
  transform: rotate(30deg);
}
/deep/.el-input__inner {
  height: 2.5rem;
}
</style>
