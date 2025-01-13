<template>
  <div class="hello">
    <el-select
      v-model="serchInfo"
      placeholder=""
      size="small"
      filterable
      remote
      :remote-method="serchAddress"
      reserve-keyword
      @change="selectAddress"
      style="margin-bottom:10px;"
    >
      <el-option
        v-for="item in pois"
        :key="item.id"
        :label="item.name"
        :value="item.id"
      ></el-option>
    </el-select>
    <el-amap
      vid="amapDemo"
      :center="center"
      :zoom="zoom"
      :plugin="plugin"
      :events="events"
      class="amap-demo"
    >
      <el-amap-marker
        v-for="(marker, index) in markers"
        :key="index"
        :position="marker.position"
        :events="marker.events"
        :visible="marker.visible"
        :draggable="marker.draggable"
        :vid="index"
      ></el-amap-marker>
    </el-amap>
    <!-- <p>当前经纬度：{{center}}</p>
		<p>当前地址：{{formattedAddress}}</p> -->
  </div>
</template>

<script>
import axios from "axios";
export default {
  data: function() {
    let self = this;
    return {
      pois: [], // 搜索返回的结集
      serchInfo: "", // 选择的用户信息
      //当前位置
      formattedAddress: "",
      zoom: 14,
      //当前经纬度
      center: [112.475931, 34.678512],
      markers: [
        {
          position: [112.475931, 34.678512],
          events: {
            click: () => {},
            dragend: (e) => {
              //拖拽点标记
              // 设置点标记经纬度
              self.markers[0].position = [e.lnglat.lng, e.lnglat.lat];
              // 设置地图中心经纬度
              self.center = [e.lnglat.lng, e.lnglat.lat];
              // 调用逆解码函数
              self.getaddress(self.center);
            },
          },
          visible: true,
          draggable: true,
          template: "<span>1</span>",
        },
      ],
      events: {
        moveend: () => {},
        zoomchange: () => {},
        click: (e) => {
          //点击地图

          // 设置点标记经纬度
          self.markers[0].position = [e.lnglat.lng, e.lnglat.lat];
          // 设置地图中心经纬度
          self.center = [e.lnglat.lng, e.lnglat.lat];
          // 调用逆解码函数
          self.getaddress(self.center);
        },
      },
      plugin: [
        {
          pName: "Geolocation",
          events: {
            // init(o) {
            // 	// o 是高德地图定位插件实例
            // 	o.getCurrentPosition((status, result) => {
            // 		console.log("123123",result)
            // 		if (result && result.position) {
            // 			self.lng = result.position.lng;
            // 			self.lat = result.position.lat;
            // 			self.center = [self.lng, self.lat];
            // 			self.markers[0].position = self.center;
            // 			self.getaddress(self.center)
            // 			self.loaded = true;
            // 			self.$nextTick();
            // 		}
            // 	});
            // }
          },
        },
        {
          pName: "Geocoder",
          events: {
            init() {
              //定位第一次逆解码   TODO 百度地图的key
              let str =
                "https://restapi.amap.com/v3/geocode/regeo?output=JSON&location=" +
                self.center[0] +
                "," +
                self.center[1] +
                "&key=422592a48734b3bf214189b09f52c0e1&radius=1000&extensions=all";
              axios.post(str).then((res) => {
                let data = res.data.regeocode;
                self.formattedAddress = data.formatted_address;
                let location = data.addressComponent.streetNumber.location.split(
                  ","
                );
                self.getaddress(location);
                // let datas = {
                // 	address: data.formatted_address,
                // 	province: data.addressComponent.province,
                // 	city: data.addressComponent.city,
                // 	area: data.addressComponent.district,
                // 	location: data.addressComponent.streetNumber.location.split(",")
                // }
                // self.selectAddressBackInfo(datas)
              });
              // o.getAddress(self.center, (status, result) => {
              // 	if (status === 'complete' && result.info === 'OK') {
              // 	self.formattedAddress=result.regeocode.formattedAddress
              // 	}

              // })
            },
          },
        },
      ],
    };
  },

  methods: {
    serchAddress(e) {
      let str =
        "https://restapi.amap.com/v3/place/text?keywords=" +
        e +
        "&output=JSON&offset=20&page=1&key=422592a48734b3bf214189b09f52c0e1&extensions=all";
      axios.post(str).then((res) => {
        this.pois = res.data.pois;
      });
    },
    selectAddress(e) {
      let list = this.pois;
      let str = list.filter((item) => {
        if (item.id === e) {
          return item;
        }
      });
      this.center = str[0].location.split(",");
      this.markers[0].position = str[0].location.split(",");
      let address =
        str[0].pname + str[0].cityname + str[0].adname + str[0].address;
      this.formattedAddress = address;
      let data = {
        address: address,
        province: str[0].pname,
        city: str[0].cityname,
        area: str[0].adname,
        location: str[0].location.split(","),
      };
      this.selectAddressBackInfo(data);
    },
    //逆解码函数
    getaddress: function(lnglat) {
      let me = this;
      let str =
        "https://restapi.amap.com/v3/geocode/regeo?output=JSON&location=" +
        lnglat[0] +
        "," +
        lnglat[1] +
        "&key=422592a48734b3bf214189b09f52c0e1&radius=1000&extensions=all";
      axios.post(str).then((res) => {
        let data = res.data.regeocode;
        me.formattedAddress = data.formatted_address;
        let datas = {
          address: data.formatted_address,
          province: data.addressComponent.province,
          city: data.addressComponent.city,
          area: data.addressComponent.district,
          location: data.addressComponent.streetNumber.location.split(","),
        };
        this.selectAddressBackInfo(datas);
      });
    },
    selectAddressBackInfo(obj) {
      this.$emit("bindinputChange", obj);
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped="scoped">
.hello {
  height: 300px;
}
</style>
