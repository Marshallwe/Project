
<template>
  <div>
    <div style="padding: 5px 0">
      <el-input v-model="searchText" @keyup.enter.native="load" style="width: 200px">
        <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
      <el-button @click="load" round type="primary" style="margin: 5px">Search</el-button>
      <el-button @click="reset" round type="info" style="margin: 5px">Reset</el-button>
      <el-button @click="add" round type="success" style="margin: 5px">Add</el-button>
    </div>

    <el-table :data="tableData" stripe border fixed style="width: 100%">
      <el-table-column prop="id" label="product id" width="80px"></el-table-column>

      <el-table-column prop="name" label="name"></el-table-column>

      <el-table-column label="picture" width="120px">
        <template slot-scope="scope">
          <img :src="baseApi + scope.row.imgs" style="width: 90px;height: 80px">
        </template>
      </el-table-column>
      <el-table-column prop="description" label="description"></el-table-column>

      <el-table-column prop="discount" label="discount"></el-table-column>

      <el-table-column prop="sales" label="Sales volume"></el-table-column>

      <el-table-column prop="saleMoney" label="Sales volume (Yuan)"></el-table-column>

      <el-table-column prop="createTime" label="Time of creation">
        <template slot-scope="scope">
          {{scope.row.createTime.replace(" ","&nbsp;&nbsp;")}}
        </template>

      </el-table-column>

      <el-table-column label="Recommend" width="150" >
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.recommend"
              @change="handleRecommend(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
          fixed="right"
          label="operation"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle  @click="edit(scope.row)"></el-button>
          <el-popconfirm
              @confirm="del(scope.row.id)"
              title="Sure to delete?"
          >
            <el-button type="danger" icon="el-icon-delete" circle slot="reference" style="margin-left: 10px"></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 10px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[3, 5, 8, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog title="information" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">
      <el-form :model="entity">

        <el-form-item label="name" label-width="150px">
          <el-input v-model="entity.name" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="description" label-width="150px">
          <el-input v-model="entity.description" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="discount" label-width="150px">
          <el-input v-model="entity.discount" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="id of category" label-width="150px">
          <el-input v-model="entity.categoryId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="picture" label-width="150px">
          <el-upload
              class="upload-demo"
              ref="upload"
              :action="baseApi + '/file/upload'"
              :file-list="fileList"
              :on-change="handleChange"
              :limit="2"
              :on-success="handleImgSuccess"
              :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">Select a file</el-button>
            <div slot="tip" class="el-upload__tip">Only jpg/png files can be uploaded, and no more than 500kb</div>
          </el-upload>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import API from '../../../utils/request'
const url = "/api/good/"

export default {
  name: "Goods",
  data() {
    return {
      baseApi: this.$store.state.baseApi,
      fileList: [],
      options: [],
      searchText: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      dialogFormVisible: false
    };
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()

  },
  methods: {
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleRecommend(good){
      API.get(url + "recommend", {
        params: {
          id: good.id,
          isRecommend : good.recommend,
        }
      }).then(res => {
        if(res.code==='200'){
          this.$message.success("Modification is successful")
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    load() {
       API.get(url + "fullPage", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            searchText: this.searchText
          }
       }).then(res => {
          this.tableData = res.data.records
          this.total = res.data.total
       })
    },
    reset(){
      this.searchText = '';
      this.load()
    },
    add() {
      this.$router.push("goodInfo")
    },
    edit(obj) {
      this.entity = JSON.parse(JSON.stringify(obj))
      this.$router.push({name: 'goodInfo',query:{good: JSON.stringify(this.entity)}})
    },
    handleImgSuccess(res){
      this.entity.imgs = res.data;
      API.post(url, this.entity).then(res2 => {
        if (res2.code === '200') {
          this.$message({
            type: "success",
            message: "success"
          })
        } else {
          this.$message({
            type: "error",
            message: res2.msg
          })
        }
        this.load()
        this.dialogFormVisible = false
      })
    },
    save() {
      if(this.fileList.length!==0){
        this.$refs.upload.submit();
      }else{
        API.post(url, this.entity).then(res2 => {
          if (res2.code === '200') {
            this.$message({
              type: "success",
              message: "success"
            })
          } else {
            this.$message({
              type: "error",
              message: res2.msg
            })
          }
          this.load()
          this.dialogFormVisible = false
        })
      }
    },
    del(id) {
      API.delete(url + id).then(res => {
        this.$message({
          type: "success",
          message: "success"
        })
        this.load()
      })
    },
    handleChange(file, fileList){
      this.fileList = fileList.slice(-3);
    },

  }

};
</script>
