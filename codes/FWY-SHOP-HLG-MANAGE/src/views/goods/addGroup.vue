<template>
  <div class="page">
    <el-card style="margin-bottom: 10px">
      <div class="header">
        <div class="header__title">拼团商品</div>
      </div>
    </el-card>
    <el-card class="container">
      <el-form
        ref="form"
        :model="formData"
        :rules="formRule"
        class="demo-form-inline"
        label-width="100px"
      >
        <div class="inline-form">
          <el-form-item
            prop="commodityName"
            label="商品名称"
            class="inline-form_item"
          >
            <el-input
              v-model="formData.commodityName"
              :disabled="disabled"
              placeholder="商品名称"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="商品编号"
            class="inline-form_item"
            prop="commodityCode"
          >
            <el-input
              v-model="formData.commodityCode"
              :disabled="disabled"
              placeholder="商品编号"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="商品条形码" class="inline-form_item">
            <el-input placeholder="商品条形码" :disabled="disabled"></el-input>
          </el-form-item> -->
          <el-form-item prop="typeId" label="商品类型" class="inline-form_item">
            <el-select
              :disabled="disabled"
              v-model="formData.typeId"
              placeholder="商品类型"
            >
              <el-option
                v-for="item in goodTypeList"
                :key="item.id"
                :label="item.classificationName"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            prop="brandId"
            label="商品品牌"
            class="inline-form_item"
          >
            <el-select
              :disabled="disabled"
              v-model="formData.brandId"
              placeholder="商品品牌"
            >
              <el-option
                v-for="item in brandList"
                :key="item.id"
                :label="item.brandName"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="拼团人数" class="inline-form_item">
            <el-input
              type="number"
              v-model="groupNum"
              placeholder="拼团人数"
              :disabled="disabled"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item
            label="已售数量"
            prop=""
            class="inline-form_item"
            v-if="mode == '查看'"
          >
            <el-input placeholder="已售数量"></el-input>
          </el-form-item>
          <el-form-item
            label="剩余库存"
            class="inline-form_item"
            v-if="mode == '查看'"
          >
            <el-input placeholder="剩余库存"></el-input>
          </el-form-item> -->
        </div>
        <el-form-item label="起止时间" required>
          <el-date-picker
            v-model="timeArea"
            :disabled="disabled"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-card class="goods-card">
          <div slot="header" class="add-specifications">
            <span>商品规格</span>
            <el-button
              type="primary"
              size="small"
              :disabled="disabled"
              @click="addDistribution"
              >新增规格</el-button
            >
          </div>
          <el-table
            :data="formData.voList"
            style="width: 100%"
            max-height="250"
          >
            <el-table-column
              v-for="item in tableHead"
              :key="item"
              :prop="item.prop"
              :label="item.label"
              :formatter="item.formatter"
            >
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
              <template slot-scope="scope" style="display: flex">
                <el-button
                  :disabled="disabled"
                  size="small"
                  @click="delDistribution(scope.row)"
                >
                  删除
                </el-button>
                <el-button
                  :disabled="disabled"
                  size="small"
                  @click="updateDistribution(scope.row)"
                >
                  编辑
                </el-button>
                <el-button
                  :disabled="disabled"
                  size="small"
                  @click="readDistribution(scope.row)"
                >
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-form-item label="商品封面图" required>
          <upload
            @bindinputChange="uploadCoverImg"
            :imageUrl="formData.coverUrl"
            :isOnly="true"
          >
          </upload>
        </el-form-item>
        <el-form-item label="商品展示图" required>
          <upload
            @bindinputChange="uploadImg"
            :disabled="disabled"
            :oldfileList="formData.carouselUrl"
            :isOnly="false"
            :limit="9"
          >
          </upload>
        </el-form-item>
        <el-form-item label="商品服务" required>
          <el-select multiple v-model="formData.serviceId">
            <el-option
              v-for="item in goodServiceList"
              :key="item.id"
              :label="item.serviceName"
              :value="item.id"
              :disabled="disabled"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品详情">
          <Ueditor ref="editor"></Ueditor>
        </el-form-item>
      </el-form>
    </el-card>
    <el-button
      :disabled="disabled"
      type="primary"
      style="margin-top: 10px"
      @click="submitForm"
      >确 定</el-button
    >
    <el-dialog
      :title="specificationsTitle"
      :visible.sync="addSpecificationsDialog"
    >
      <el-form
        ref="specificationsForm"
        :rules="specificationsFormRules"
        :model="specificationsFormData"
        label-width="120px"
      >
        <el-form-item label="规格名称" prop="specificationName">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.specificationName"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="规格条形码">
          <el-input
            v-model="specificationsFormData.barCode"
            :disabled="specificationDisabled"
          ></el-input>
        </el-form-item>
        <el-form-item label="售价" prop="sellPrice">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.sellPrice"
            oninput="value=value.toString().match(/^\d+(?:\.\d{0,2})?/)"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="拼团价" prop="groupWorkPrice">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.groupWorkPrice"
            oninput="value=value.toString().match(/^\d+(?:\.\d{0,2})?/)"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>

        <el-form-item label="单位" prop="unit">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.unit"
            placeholder="例如：袋、个"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="商品总库存" prop="stock">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.stock"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="规格图片" required="">
          <upload
            @bindinputChange="uploadSpecificationImg"
            :imageUrl="specificationsFormData.specificationPicture"
            :isOnly="true"
          >
          </upload>
        </el-form-item> -->
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
        v-if="specificationsTitle != '查看'"
      >
        <el-button @click="addSpecificationsDialog = false">取 消</el-button>
        <el-button type="primary" @click="addSpecificationsDialogClick"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import serve from '@/api/goods/index';
import goodType from '@/api/goodType/index';
import brand from '@/api/brand/index';
import goodService from '@/api/goodService/index';
import util from '@/assets/util/util';
export default {
  components: {
    Ueditor: () => import('@/components/UEditor'),
    upload: () => import('@/components/upload/uploadIndex'),
  },
  created() {
    this.mode = this.$route.query.mode;
    this.disabled = this.mode == 2;
    this.commodityId = this.$route.query.id;
    this.userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
    this.getGoodsTypeList();
    this.getBrandList();
    this.getGoodServiceList();
    if (this.mode != 0) {
      this.getDetail();
    }
  },
  watch:{
    addSpecificationsDialog(val) {
      if(!val) {
        this.specificationsFormData ={type: 0,}
      }
    }
  },
  data() {
    return {
      disabled: false,
      mode: 0, // 0 新增  1 编辑  2 查看
      userInfo: {},
      formData: {
        voList: [],
      },
      tableHead: [
        {
          label: '规格名称',
          prop: 'specificationName',
        },
        {
          label: '拼团价',
          prop: 'groupWorkPrice',
        },
        
        {
          label: '售价',
          prop: 'sellPrice',
        },
        {
          label: '库存',
          prop: 'stock',
        },
      ],
      addSpecificationsDialog: false,
      specificationsTitle: '',
      specificationsFormRules: {
        specificationName: [
          { required: true, message: '请填写名称', trigger: 'change' },
        ],
        sellPrice: [
          { required: true, message: '请填写售价', trigger: 'change' },
        ],
        unit: [{ required: true, message: '请填写单位', trigger: 'change' }],
        stock: [{ required: true, message: '请填写库存', trigger: 'change' }],
        groupWorkPrice: [{ required: true, message: '请填写拼团价', trigger: 'change' }],
      },
      specificationsFormData: {
        type: 0,
      },
      goodTypeList: [],
      brandList: [],
      goodServiceList: [],
      carouselUrl: '',
      formRule: {
        commodityName: [
          { required: true, message: '请填写名称', trigger: 'change' },
        ],
        commodityCode: [
          { required: true, message: '请填写编号', trigger: 'change' },
        ],
        typeId: [{ required: true, message: '请选择类型', trigger: 'change' }],
        brandId: [{ required: true, message: '请选择品牌', trigger: 'change' }],
      },
      timeArea: [],
      groupNum: 0,
    };
  },
  methods: {
    getDetail() {
      serve.getDetails(this.commodityId).then((res) => {
        this.formData = {
          ...res.data,
          carouselUrl: res.data.carouselUrl.split(',').map((item, index) => {
            return {
              url: item,
              name: index,
            };
          }),
          serviceId: res.data.serviceId.split(','),
        };
        this.carouselUrl = res.data.carouselUrl;
        this.timeArea = [
          new Date(res.data.startTime),
          new Date(res.data.endTime),
        ];
        this.groupNum = res.data.groupNum;
        setTimeout(() => {
          this.$refs.editor.setContent(res.data.commodityDetail);
        }, 500);
      });
    },
    getGoodServiceList() {
      goodService.getList().then((res) => {
        this.goodServiceList = res.data;
      });
    },
    getBrandList() {
      brand.getList().then((res) => {
        this.brandList = res.data;
      });
    },
    getGoodsTypeList() {
      goodType.getList().then((res) => {
        this.goodTypeList = res.data;
      });
    },
    addDistribution() {
      this.addSpecificationsDialog = true;
      this.specificationDisabled = false;
      this.specificationsTitle = '新增';
      this.specificationsFormData = {};
      this.$refs['specificationsForm'].resetFields();
    },
    // // 规格的增删查改
    // addDistribution() {
    //   this.addSpecificationsDialog = true;
    //   this.specificationDisabled = false;
    //   this.specificationsTitle = '新增';

    //   this.specificationsFormData = {};
    // },
    delDistribution(row) {
      this.$confirm('确认要删除此规格？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        let index = this.formData.voList.findIndex((item) => {
          return item.commodityId == row.commodityId;
        });
        this.formData.voList.splice(index, 1);
      });
    },
    updateDistribution(row) {
      this.addSpecificationsDialog = true;
      this.specificationDisabled = false;
      this.specificationsTitle = '编辑';
      this.specificationsFormData = row;
    },
    readDistribution(row) {
      this.addSpecificationsDialog = true;
      this.specificationDisabled = true;
      this.specificationsTitle = '查看';
      this.specificationsFormData = row;
    },
    // 新增/编辑规格
    addSpecificationsDialogClick() {
      this.$refs['specificationsForm'].validate((valid) => {
        if (valid) {
          let index = this.formData.voList.findIndex((item) => {
            return item.id == this.specificationsFormData.id;
          });
          if (this.specificationsTitle == '新增') {
            if (this.formData.voList.length >= 1) {
              this.$message('拼团商品只能有一个规格');
              return;
            }
            this.formData.voList.unshift(this.specificationsFormData);
          } else if (this.specificationsTitle == '编辑') {
            let obj = this.formData.voList[0];
            console.log(obj)
            if(!obj.sellPrice || obj.sellPrice == 0) {
              this.$message('售价不能等于0');
              return;
            }
            if(!obj.groupWorkPrice || obj.groupWorkPrice == 0) {
              this.$message('拼团价不能等于0');
              return;
            }
            this.formData.voList.splice(index, 1, this.specificationsFormData);
          }
          this.addSpecificationsDialog = false;
        }
      });
    },
    // 文件上传
    uploadSpecificationImg(img) {
      // 规格图片
      this.specificationsFormData.specificationPicture = img;
    },
    uploadCoverImg(img) {
      // 封面图
      this.formData.coverUrl = img;
    },
    uploadImg(img) {
      // 商品展示图
      this.carouselUrl = img.map((item) => item.url).join(',');
    },
    // 表单提交
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (!this.timeArea) {
          this.$message('请选择起止时间');
          return;
        }
        if (this.formData.serviceId.length == 0) {
          this.$message('请选择商品服务');
          return;
        }
        let data = {
          ...this.formData,
          commodityType: 4,
          commodityDetail: this.$refs.editor.getContent(),
          specificationDtoList: [{ ...this.formData.voList[0], type: 0 }],
          carouselUrl: this.carouselUrl,
          serviceId: this.formData.serviceId.join(','),
          typeName: this.goodTypeList.filter((item) => {
            return item.id == this.formData.typeId;
          })[0].classificationName,
          commodityExpandDto: {
            groupNum: this.groupNum,
            startTime: util.dateFormat(this.timeArea[0], 'yyyy-mm-dd hh:MM:ss'),
            endTime: util.dateFormat(this.timeArea[1], 'yyyy-mm-dd hh:MM:ss'),
          },
        };
        delete data.voList;
         if(Object.values(data.specificationDtoList[0]).length < 2) {
          return this.$message('请填写商品规格')
        }
        if (valid) {
          if (this.mode == 0) {
            // 新增
            serve.add(data).then((res) => {
              if (res.code == 200) {
                this.$message.success(res.msg);
                this.$router.push('/home/goods/groupGood');
              } else {
                this.$message.error(res.msg);
              }
            });
          } else if (this.mode == 1) {
            // 编辑
            serve
              .update({
                ...data,
                commodityId: this.commodityId,
              })
              .then((res) => {
                if (res.code == 200) {
                  this.$message.success(res.msg);
                  // this.getDetail();
                  this.$router.push('/home/goods/groupGood');
                } else {
                  this.$message.error(res.msg);
                }
              });
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
  },
};
</script>
<style lang="less" scoped>
.container {
  height: 76vh;
  overflow-y: auto;
}
.inline-form {
  display: flex;
  flex-wrap: wrap;
  &_item {
    flex: 0 0 45%;
  }
}
.goods-card {
  margin-bottom: 30px;
}
.add-specifications {
  width: 100%;
  display: flex;
  justify-content: space-between;
}
</style>
