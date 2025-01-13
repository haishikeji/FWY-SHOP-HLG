<template>
  <div class="page">
    <el-card style="margin-bottom: 10px">
      <div class="header">
        <div class="header__title">新增商品</div>
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
            prop="commodityCode"
            label="商品编号"
            class="inline-form_item"
          >
            <el-input
              v-model="formData.commodityCode"
              :disabled="disabled"
              placeholder="商品编号"
            ></el-input>
          </el-form-item>

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
                  @click="delDistribution(scope.row, scope.$index)"
                >
                  删除
                </el-button>
                <el-button
                  :disabled="disabled"
                  size="small"
                  @click="updateDistribution(scope.row, scope.$index)"
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
          <el-select :disabled="disabled" multiple v-model="formData.serviceId">
            <el-option
              v-for="item in goodServiceList"
              :key="item.id"
              :label="item.serviceName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品详情">
          <Ueditor ref="editor"></Ueditor>
        </el-form-item>
      </el-form>
    </el-card>
    <el-button
      type="primary"
      :disabled="disabled"
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
        <el-form-item label="卖价" prop="sellPrice">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.sellPrice"
            oninput="value=value.toString().match(/^\d+(?:\.\d{0,2})?/)"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="建议售价" prop="originalPrice">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.originalPrice"
            oninput="value=value.toString().match(/^\d+(?:\.\d{0,2})?/)"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="网络控价">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.shopownerPrice"
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
        <el-form-item label="批发价(成本价)">
          <el-input
            :disabled="specificationDisabled"
            v-model="specificationsFormData.costPrice"
            onkeypress="return noNumbers(event)"
            oninput="value=value.toString().match(/^\d+(?:\.\d{0,2})?/)"
          >
            <template slot="append">元</template></el-input
          >
        </el-form-item>
        <el-form-item label="商品总库存" prop="stock">
          <el-input
            :disabled="specificationDisabled"
            type="number"
            v-model="specificationsFormData.stock"
          ></el-input>
        </el-form-item>
        <!--  -->
        <el-form-item label="是否设为默认">
          <el-switch
            v-model="specificationsFormData.type"
            active-color="#2375cc"
            :disabled="specificationDisabled"
            inactive-color="#eee"
            :active-value="0"
            :inactive-value="1"
          >
          </el-switch>
        </el-form-item>
        <el-form-item label="规格图片" required="">
          <upload
            @bindinputChange="uploadSpecificationImg"
            :imageUrl="specificationsFormData.specificationPicture"
            :isOnly="true"
          >
          </upload>
        </el-form-item>
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
  watch: {
    addSpecificationsDialog(val) {
      if (!val) {
        this.specificationsFormData = { type: 0, specificationPicture: '' };
      }
    },
  },
  data() {
    return {
      disabled: false,
      mode: 0, // 0 新增  1 编辑  2 查看
      userInfo: {},
      formData: {
        voList: [],
      },
      commodityId: '',
      tableHead: [
        {
          label: '规格名称',
          prop: 'specificationName',
        },
        {
          label: '成本价',
          prop: 'costPrice',
        },
        {
          label: '原价',
          prop: 'originalPrice',
        },
        {
          label: '店长价',
          prop: 'shopownerPrice',
        },
        {
          label: '售价',
          prop: 'sellPrice',
        },
        {
          label: '库存',
          prop: 'stock',
        },
        {
          label: '是否默认',
          prop: 'type',
          formatter: this.typeFormat,
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
        originalPrice: [
          { required: true, message: '请填写原价', trigger: 'change' },
        ],
        unit: [{ required: true, message: '请填写单位', trigger: 'change' }],
        stock: [{ required: true, message: '请填写库存', trigger: 'change' }],
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
      specificationsIndex: 0, // 选择当前规格idnex
    };
  },
  methods: {
    /**
     * 格式化默认字段
     */
    typeFormat(row) {
      switch (row.type) {
        case 0:
            return '默认';
        case 1:
            return '非默认';
        default:
            return '非默认';
      }
    },
    getDetail() {
      serve.getDetails(this.commodityId).then((res) => {
        this.formData = {
          ...res.data,
          carouselUrl: res.data.carouselUrl?res.data.carouselUrl.split(',').map((item, index) => {
            return {
              url: item,
              name: index,
            };
          }):[],
          serviceId: res.data.serviceId?res.data.serviceId.split(','):'',
        };
        this.carouselUrl = res.data.carouselUrl;
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
    // 规格的增删查改
    addDistribution() {
      this.addSpecificationsDialog = true;
      this.specificationDisabled = false;
      this.specificationsTitle = '新增';
      this.specificationsFormData = {};
      this.$refs.specificationsForm.resetFields();
    },
    delDistribution(row, index) {
      this.$confirm('确认要删除此规格？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        // let index = this.formData.voList.findIndex((item) => {
        //   return item.commodityId == row.commodityId;
        // });
        this.formData.voList.splice(index, 1);
      });
    },
    updateDistribution(row, index) {
      this.addSpecificationsDialog = true;
      this.specificationDisabled = false;
      this.specificationsTitle = '编辑';
      this.specificationsIndex = index;
      this.specificationsFormData = { ...row };
    },
    readDistribution(row) {
      this.addSpecificationsDialog = true;
      this.specificationDisabled = true;
      this.specificationsTitle = '查看';
      this.specificationsFormData = { ...row };
    },
    // 新增/编辑规格
    addSpecificationsDialogClick() {
      let price = parseFloat(this.specificationsFormData.sellPrice)
      if(!price || price == 0) {
        this.$message('售价不能等于0')
        return false
      }
      this.$refs['specificationsForm'].validate((valid) => {
        if (!this.specificationsFormData.specificationPicture) {
          this.$message('请上传规格图片');
          return;
        }
        if (valid) {
          console.log(this.formData.voList);
          let index = this.specificationsIndex;
          let obj = this.specificationsFormData;
          if (this.specificationsTitle == '新增') {
            let list = this.formData.voList;
            list.push(this.specificationsFormData);
            if (1 != list.length) {
              if (obj.type == 0) {
                for (let i in list) {
                  if (list.length-1 != i) {
                    list[i].type = 1;
                  }
                }
              }
              this.formData.voList = list;
            }
          } else if (this.specificationsTitle == '编辑') {
            let list = this.formData.voList;
            list.splice(index, 1, this.specificationsFormData);
            if (1 != list.length) {
              if (obj.type == 0) {
                for (let i in list) {
                  if (index != i) {
                    list[i].type = 1;
                  }
                }
              }
              this.formData.voList = list;
            }
           
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
        if (this.formData.serviceId.length == 0) {
          this.$message('请选择商品服务');
          return;
        }
        let data = {
          ...this.formData,
          commodityType: 1,
          commodityDetail: this.$refs.editor.getContent(),
          specificationDtoList: [...this.formData.voList],
          carouselUrl: this.carouselUrl,
          serviceId: this.formData.serviceId.join(','),
          typeName: this.goodTypeList.filter((item) => {
            return item.id == this.formData.typeId;
          })[0].classificationName,
        };

        delete data.voList;
        console.log(data.specificationDtoList, 123);
        if (Object.values(data.specificationDtoList[0]).length < 1) {
          return this.$message('请填写商品规格');
        } else {
          let list = data.specificationDtoList;
          let str = list.filter(item=> {
            return item.type == 0 
          })
          console.log(str)
          if(str.length == 0) {
            this.$message('商品必须有一个默认规格');
            return false
          }
        }


        if (valid) {
          if (this.mode == 0) {
            // 新增
            serve.add(data).then((res) => {
              if (res.code == 200) {
                this.$message.success(res.msg);
                this.$router.push('/home/goods/list');
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
                  this.$router.push('/home/goods/list');
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
