<template>
  <div class="upload-container">

    <el-upload
        class="upload-demo"
        :action="actionUrl"
        name="multipartFile"
        :before-upload="beforUpload"
        :on-success="onsuccesss"
        multiple
        :show-file-list="false"
        accept=".jpg,.png,.bmp,.jpeg">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">文件上传最大不能超过5M</div>
      </el-upload>
     <!-- <el-upload
            :action="actionUrl"
            name="multipartFile"
            :headers="headers"
            multiple
            list-type="picture-card"
            :on-success="onSuccess"
            :file-list="oldfileList"
            :beforeUpload="beforeAvatarUpload"
            :on-remove="handleRemove"
            accept=".jpg,.png,.bmp,.jpeg"
          >
          <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" style="font-size:10px;color:#999999">
              上传文件大小不能超过 5MB
            </div>
          </el-upload> -->
  </div>
</template>

<script>
import server from '@/api/system/plant'
// 接口根据自己后端提供的写
export default {
  props: {
    // 最大上传文件的大小
    maxFileSize: {
      type: Number,
      default: 5
    },
    buttonStyle: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      headers:{},
      actionUrl: "api/file/upload",
      dialogVisible: false,
      loading: false,
      imgSrcList: []
    }
  },
  methods: {
    /**
     * 上传成功
     */
    onsuccesss(filurlListe) {
      this.$emit('success', filurlListe.data)
    },
    beforUpload(file) {
      // 限制图片上传格式和大小
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension =
        testmsg === "jpg" ||
        testmsg === "JPG" ||
        testmsg === "png" ||
        testmsg === "jpeg" ||
        testmsg === "JPEG" ||
        testmsg === "PNG";
      const isLt50M = file.size / 1024 / 1024 < 5;
      if (!extension) {
        this.$message({
          message: "上传图片只能是jpeg / jpg / png / bpm格式!",
          type: "error"
        });
        return false; //必须加上return false; 才能阻止
      } else {
        if (!isLt50M) { 
          this.$message({
            message: "上传文件大小不能超过 5MB!",
            type: "error"
          });
          return false;
        } else {
          return extension || isLt50M;
        }
      }
    },
  }
}
</script>

<style lang="less" scoped>
.btn-box {
  text-align: right !important;
}

.img-box {
  position: relative;
  display: inline-block;
  width: 120px;
  margin-right: 10px;
  margin-bottom: 10px;
  text-align: center;
  .img {
    width: 100%;
  }
  .delete-image {
    display: none;
    .el-icon-delete-solid {
      width: 40px;
      height: 40px;
      line-height: 40px;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 20px;
      color: #000;
      font-weight: 900;
    }
  }

  &:hover {
    .delete-image {
      cursor: pointer;
      width: 100%;
      height: 100%;
      position: absolute;
      background-color: rgba(255, 255, 255, 0.6);
      display: inline-block;
    }
  }
}

</style>
