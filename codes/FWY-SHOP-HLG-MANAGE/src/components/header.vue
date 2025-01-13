<!--
 * @Author: 半山
 * @Date: 2020-07-07 17:03:52
 * @LastEditTime: 2022-03-26 17:24:18
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \jujubetwin\src\components\HelloWorld.vue
-->
<template>
  <div class="header">
    <div class="card_body">
      <div style="display: flex; align-items: center">
        <img class="logo" src="../assets/icon/icn-white.jpg" alt="" />
        <span
          style="
            font-family: Microsoft YaHei;
            font-weight: bold;
            color: #ffffff;
          "
          >商城管理后台</span
        >
      </div>
      <audio
        style="z-index: -1"
        ref="audio"
        src="../assets/dingdong.mp3"
        controls
        autoplay
      ></audio>

      <div style="display: flex; align-items: center">
        <div
          class="msg"
          @click="msgHandle"
          style="cursor: pointer; margin-right: 5px"
        >
          <el-badge :is-dot="num" class="item">
            <i style="font-size: 16px" class="el-icon-bell"></i>
          </el-badge>
        </div>
        <!-- <i class="el-icon-bell"></i> -->
        <div style="margin-left: 5px; color: #fff;margin: 0 10px;">
          {{ nickname }} 欢迎您！
        </div>
        <i class="backInfo el-icon-switch-button" @click="backAdmin"></i>
      </div>
    </div>
    <!-- <div ref="audio"></div> -->
    <el-drawer
      title="通知列表"
      :visible.sync="msgVisible"
      :direction="direction"
      :modal="false"
      :before-close="handleClose"
      size="20%"
    >
      <el-row style="display: flex; justify-content: center">
        <!-- <el-button type="primary" size="mini" plain @click="handleBtn(0)"
          >未读</el-button
        >
        <el-button type="success" size="mini" plain @click="handleBtn(1)"
          >已读</el-button
        > -->
      </el-row>
      <div id="box">
        <div
          class="box"
          v-infinite-scroll="load"
          infinite-scroll-disabled="disabled"
        >
          <ul class="list">
            <li
              v-for="(item, index) in records"
              :key="index"
              class="list-item"
              @click="onClickMeg(item)"
            >
              <strong
                >消息：<i></i>
                <el-badge
                  :is-dot="item.isRead == 0"
                  class="item"
                  style="float: right"
                  ><span style="color: rgba(0, 0, 0, 0.24); float: right">{{
                    item.createTime
                  }}</span></el-badge
                >
                <!-- <span style="color:rgba(0, 0, 0, 0.24);float:right">{{item.content.time}}</span> --> </strong
              ><br />
              <strong
                >内容： <i>{{ item.content.content }}</i></strong
              ><br />
            </li>
          </ul>
          <p
            v-if="loading"
            style="margin-top: 10px; text-align: center"
            class="loading"
          >
            <span></span>
          </p>
          <p
            v-if="noMore"
            style="
              margin-top: 10px;
              font-size: 13px;
              color: #ccc;
              text-align: center;
            "
          >
            没有更多了
          </p>
        </div>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import moment from "moment";
import req from "@/api/config/config";
import msg from "@/api/message/index";
export default {
  name: "HelloWorld",
  data() {
    return {
      userInfo: {},
      num: 0,
      searchVal: "",
      time: "",
      nickname: "",
      num: false,
      msgVisible: false,
      loading: false,
      direction: "rtl",
      records: [],
      totalPages: "",
      params: {
        current: 1,
        size: 999,
      },
      data: {},
      mgsInfo: [],
    };
  },
  computed: {
    noMore() {
      return this.params.current >= this.totalPages;
    },
    disabled() {
      return this.loading || this.noMore;
    },
    getWsMsg() {
      return this.$store.state.webSocketMsg;
    },
    playAudio() {
      this.$refs.audio.play();
    },
  },
  created() {
    let _ = require("lodash");
    this.getNowTime();
    this.findUserInfo();
    this.createWebSockets();
    this.mgsInfo = JSON.parse(sessionStorage.getItem("msg"));
    let params = {
      current: 1,
      size: 999,
    };
    this.msgFound(params);
  },
  watch: {
    getWsMsg: {
      handler: function(newVal) {
        console.log(newVal);
        let that = this;
        this.$nextTick(() => {
          that.$refs.audio.play();
        });

        //  this.playAudio()
        // alert('接收到webSocket推送'+ newVal)

        // setTimeout(() => {
        //   if(this.mgsInfo.length>0){
        //     audio.play()
        //   }
        // }, 3000);
      },
      deep: true,
      immediate: true,
    },
  },
  methods: {
    //延迟调用
    getOnkeyDown() {
      document.onkeydown = function(e) {
        var key = window.event.keyCode;

        if (key != 13) {
          let path = e.view.location.hash.includes("addGood"); //获取到当前路由,判断是否是添加商品的路由
          console.log(path, "89899989");
          _self.num++;
          console.log(_self.num);

          // setTimeout(() => {
          //   if(_self.num==78&&!path){
          //     console.log('7777');
          //    _self.changfouce()
          // }
          // }, 1000);
        } else {
        }
      };
    },
    //聚焦
    changfouce() {
      this.$nextTick(() => {
        this.$refs.seach_input.focus();
      });
    },
    dateTime(rowData) {
      var d = new Date(rowData);
      var a =
        d.getFullYear() +
        "-" +
        (d.getMonth() + 1 < 10 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1) +
        "-" +
        (d.getDate() < 10 ? "0" + d.getDate() : d.getDate()) +
        " " +
        (d.getHours() < 10 ? "0" + d.getDate() : d.getDate()) +
        ":" +
        (d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes()) +
        ":" +
        (d.getSeconds() < 10 ? "0" + d.getSeconds() : d.getSeconds());
      return a;
    },
    handleBtn(val) {
      (this.params = {
        current: 1,
        size: 10,
        isRead: val,
      }),
        (this.records = []);
      this.msgFound();
      this.load();
    },
    load() {
      //滑到底部时进行加载
      this.loading = true;
      setTimeout(() => {
        this.params.current += 1; //页数+1
        this.msgFound(); //调用接口，此时页数+1，查询下一页数据
      }, 1000);
    },
    msgFound() {
      msg.queryManageMsgPage(this.params).then((res) => {
        if (res.code === 200) {
          this.records = res.data.records;
          this.totalPages = res.data.pages;
          this.loading = false;
          this.records = this.records.map((item) => {
            let content = JSON.parse(item.content);

            return {
              ...item,
              content: content,
            };
          });
        }
      });
    },
    //连接websocket
    // getWebSocket() {
    //   this.data = JSON.parse(sessionStorage.getItem("userInfo"));
    //   var ws = new WebSocket(`ws://${req.base}/websocket/${this.data.id}`);
    //   ws.onmessage = (e) => {
    //     let data = JSON.parse(e.data);
    //     this.$notify({
    //       title: "通知消息",
    //       dangerouslyUseHTMLString: true,
    //       message: `<strong>内容：<i>${data.content}</i></strong><br/>`,
    //       duration: 8000,
    //       onClick: () => {
    //         this.onClickMeg(data.id);
    //       },
    //     });
    //   };
    //   ws.onopen = function (params) {
    //     console.log(params);
    //   };
    //   ws.onerror = function (params) {
    //     console.log(params);
    //   };
    //   ws.onclose = function (params) {
    //     console.log(params);
    //   };
    // },
    handleClose() {
      this.msgVisible = false;
    },
    msgHandle() {
      this.records = [];
      this.params.current = 1;
      this.msgVisible = true;
      this.msgFound();
    },
    findUserInfo() {
      this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      this.nickname = this.userInfo.nickname || this.userInfo.loginAccount;
    },
    getNowTime() {
      let time = new Date();
      time = moment(time).format("YYYY-MM-DD HH:mm:ss");
      let week = moment(time).day();
      let weeks = [
        "星期日",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六",
      ][week];
      this.time = time + " " + weeks;
      setTimeout(() => {
        this.getNowTime();
      }, 1000);
    },
    backAdmin() {
      this.$confirm("确认退出到登录页？")
        .then((_) => {
          sessionStorage.setItem("Token", "");
          this.$router.push("/");
          var ws = new WebSocket(`ws://${req.base}/websocket/${this.data.id}`);
          ws.onclose = function(e) {
            ws.close();
          };
        })
        .catch((_) => {});
    },
    onClickMeg(item) {
      msg.setMsgRead({ id: item.id }).then((res) => {
        if (res.code == 200) {
          this.msgFound();
          console.log(item, "type----");
          // if (item.content.type == 0) {
          //   this.$router.push("/home/order/manager");
          // } else if (item.content.type == 1) {
          //   this.$router.push("/home/order/selftake");
          // }else if (item.content.type == 2) {
          //   this.$router.push("/home/order/timely");
          // }else if (item.content.type == 4) {
          //   this.$router.push("/home/order/timely");
          // }
        }
      });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
.item {
  height: 16px;
  display: flex;
  line-height: 16px;
  .el-icon-bell {
    line-height: 16px;
    color: #fff;
  }
}
.header {
  height: 50px;
  width: 100%;
  position: relative;
  z-index: 5;
  // padding: 15px 15px 0;
  box-sizing: border-box;
  display: flex;
  justify-content: flex-end;
}
.card_body {
  height: 100%;
  background: rgb(43, 60, 104);
  line-height: 50px;
  display: flex;
  justify-content: space-between;
  flex: 1;
  padding: 0 30px;
  border-bottom: 1px solid #fff;
}
.header_logo {
  width: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.logo {
  height: 38px;
  margin-right: 10px;
}
.logo_title {
  font-size: 24px;
}
.backInfo {
  cursor: pointer;
  color: #fff;
}
#box {
  width: 100%;
  height: 80%;
  position: absolute;
  overflow-y: auto;
}
.box {
  width: 100%;
  margin: 0 auto;
}
.list {
  padding: 0;
  font-size: 14px;
}
.list-item {
  width: 100%;
  display: block;
  overflow: hidden;
  // text-overflow: ellipsis;
  white-space: wrap;
  list-style: none;
  padding: 15px;
  box-sizing: border-box;
  // height: 70px;
  // line-height: 70px;
  border-bottom: 1px solid #e7e7e7;
  cursor: pointer;
}
/deep/ .el-drawer__header {
  margin-bottom: 20px;
  span:focus {
    outline: none;
  }
}
.el-drawer:focus {
  outline: none !important;
}
/deep/ .el-drawer__close-btn:focus {
  outline: none;
}
/deep/ .el-drawer {
  width: 20% !important;
  height: 92%;
  margin-top: 80px;
}
.item {
  // margin-top: 3px;
}
.msg {
  height: 80px;
  display: flex;
  line-height: 80px;
  align-items: center;
  margin-right: 10px;
}
/deep/.el-badge__content.is-fixed.is-dot {
  right: 0px;
}
</style>
