
<template>
  <div style="width: 1000px; margin: 50px auto">
    <el-form :model="good">
      <el-form-item label="name" label-width="150px">
        <el-input
          v-model="good.name"
          autocomplete="off"
          style="width: 80%"
        ></el-input>
      </el-form-item>
      <el-form-item label="description" label-width="150px">
        <el-input
          v-model="good.description"
          autocomplete="off"
          style="width: 80%"
        ></el-input>
      </el-form-item>
      <el-form-item label="Specifications" label-width="150px">
        <el-tag
          closable
          @close="handleClose(standard)"
          :disable-transitions="true"
          style="margin-right: 10px"
          v-for="(standard, index) in standards"
          :key="index"
          @click="editStandard(index)"
          >{{ standard.value }}</el-tag
        >
        <el-button icon="el-icon-plus" circle @click="addStandard"></el-button>
      </el-form-item>

      <el-form-item label="discount" label-width="150px">
        <el-input
          v-model="good.discount"
          autocomplete="off"
          style="width: 80%"
        ></el-input>
      </el-form-item>

      <el-form-item label="Classification" label-width="150px">
        <el-select v-model="good.categoryId" placeholder="Please select">
          <el-option
            v-for="item in categoryItems"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Picture of product" label-width="150px">
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="baseApi + '/file/upload'"
          :file-list="fileList"
          :on-change="handleChange"
          :limit="1"
          :on-remove="handleRemove"
          :on-success="handleImgSuccess"
          :auto-upload="false"
        >
          <el-button slot="trigger" size="small" type="primary"
            >Select a file</el-button
          >
          <div slot="tip" class="el-upload__tip">
            Only one jpg/png file can be uploaded, and it should not exceed 500kb
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label-width="150px">
        <el-button type="success" @click="submit">Submit</el-button>
      </el-form-item>
    </el-form>
    <el-dialog
      title="Specification Information"
      :visible.sync="dialogFormVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="standard">
        <el-form-item label="Name of specification" label-width="150px">
          <el-input
            v-model="standard.value"
            autocomplete="off"
            style="width: 80%"
          ></el-input>
        </el-form-item>
        <el-form-item label="Price" label-width="150px">
          <el-input
            v-model="standard.price"
            autocomplete="off"
            style="width: 80%"
          ></el-input>
        </el-form-item>
        <el-form-item label="Stock in stock" label-width="150px">
          <el-input
            v-model="standard.store"
            autocomplete="off"
            style="width: 80%"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveStandard">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import API from "@/utils/request";
const url = "/api/good";
export default {
  name: "GoodInfo",
  data() {
    return {
      baseApi: this.$store.state.baseApi,
      good: {
        discount: "1",
      },
      standards: [],
      index: 0,
      standard: {},
      fileList: [],
      dialogFormVisible: false,
      categoryItems: [],
      checkedCategory: {},
    };
  },
  created() {
    this.request.get("/api/category").then((res) => {
      if (res.code === "200") {
        this.categoryItems = res.data;
      }
      if (this.$route.query.good) {
        this.good = JSON.parse(this.$route.query.good);
        this.fileList = [{ name: "Original image", url: this.good.imgs }];
        this.checkedCategory = this.categoryItems[this.good.categoryId];
        this.request.get(url + "/standard/" + this.good.id).then((res) => {
          if (res.code === "200") {
            let standards = JSON.parse(res.data);
            this.standards = standards;
          } else {
          }
        });
      }
    });
  },
  methods: {

    editStandard(index) {
      this.standard = this.standards[index];
      this.index = index;
      this.dialogFormVisible = true;
    },
    addStandard() {
      this.standard = {};
      this.index = this.standards.length;
      this.dialogFormVisible = true;
    },
    saveStandard() {
      if (this.standard.value == undefined || this.standard.value == "") {

        this.$message({
          type: "error",
          message: "Please enter specification name",
          showClose: true,
        });
        return;
      }
      if (this.standard.price == undefined || this.standard.price == "") {
        this.$message({
          type: "error",
          message: "Please enter the price",
          showClose: true,
        });
        return;
      }
      if (this.standard.store == undefined || this.standard.store == "") {
        this.$message({
          type: "error",
          message: "Please enter the inventory",
          showClose: true,
        });
        return;
      }
      this.standards[this.index] = this.standard;
      this.dialogFormVisible = false;
    },
    handleClose(standard) {
      this.standards.splice(this.standards.indexOf(standard), 1);
    },

    handleImgSuccess(res) {
      this.good.imgs = res.data;
      this.save();
    },
    handleChange(file, fileList) {
      this.fileList = fileList.slice(-3);
    },
    handleRemove() {
      this.fileList.pop();
    },

    submit() {
      if (this.good.name == undefined || this.good.name.trim() == "") {
        this.$message({
          type: "error",
          message: "Please enter the product name",
          showClose: true,
        });
        return false;
      }
      if (
        this.good.description == undefined ||
        this.good.description.trim() == ""
      ) {
        this.$message({
          type: "error",
          message: "Please enter the product description",
          showClose: true,
        });
        return false;
      }
      if (this.standards.length === 0) {
        this.$message({
          type: "error",
          message: "Please add at least one specification",
          showClose: true,
        });
        return false;
      }
      if (this.good.discount == undefined || this.good.discount === "") {
        this.$message({
          type: "error",
          message: "Please enter the product discount",
          showClose: true,
        });
        return false;
      }
      if (
        this.good.categoryId == undefined
      ) {
        this.$message({
          type: "error",
          message: "Please select a product category",
          showClose: true,
        });
        return false;
      }
      if (this.fileList.length === 0) {
        this.$message({
          type: "error",
          message: "Please upload a picture",
          showClose: true,
        });
        return false;
      }
      if (this.fileList[0].status === "ready") {
        this.$refs.upload.submit();
      } else if (this.fileList[0].status === "success") {
        this.save();
      }
    },
    save() {
      API.post(url, this.good).then((res2) => {
        if (res2.code === "200") {
          this.good.id = res2.data;
          this.request
            .post(url + "/standard?goodId=" + this.good.id, this.standards)
            .then((res) => {
              if (res.code === "200") {
                this.$message({
                  type: "success",
                  message: "Success",
                  showClose: true,
                });
                this.$router.go(-1);
              } else {
                this.$message({
                  type: "error",
                  message: "Fail",
                  showClose: true,
                });
              }
            });
        } else {
          this.$message({
            type: "error",
            message: res2.msg,
          });
        }
      });
    },
  },
};
</script>

<style scoped>
</style>
