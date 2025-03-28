
<template>
  <div>
    <div class="demo-input-size">
      <el-input placeholder="Please enter a file name" prefix-icon="el-icon-search" style="width: 250px;padding-right: 5px" v-model="fileName"></el-input>
      <el-button round type="primary" @click="search">Search</el-button>
      <el-button round type="info" @click="reload">Reset</el-button>
    </div>

    <div style="padding-top: 10px">
      <el-upload action="http://localhost:8888/file/upload" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button style="margin: 5px;width: 100px" round type="warning">
          <i class="el-icon-upload2" style="padding-right: 6px"></i>Upload
        </el-button>
      </el-upload>
      <el-button  style="margin: 5px;width: 120px"round type="danger" @click="delBatch" >
        <i class="el-icon-remove" style="padding-right: 6px"></i>Batch deletion
      </el-button>
    </div>

    <el-table stripe border fixed :data="tableData" background-color="black" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" ></el-table-column>
      <el-table-column prop="name" label="Name of file" width="500" ></el-table-column>
      <el-table-column prop="type" label="Type of file" width="100" ></el-table-column>
      <el-table-column prop="size" label="File size" width="100" ></el-table-column>
      <el-table-column label="Operation">

        <template slot-scope="scope">
          <a :href="baseApi + scope.row.url">
            <el-button
              size="mini"
              type="success"
              >Download
            </el-button>
          </a>
          <el-button
              size="mini"
              type="danger"
              style="margin-left: 10px"
              @click="handleDelete(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="flex: 0 0 auto">
      <el-pagination
          :current-page="currentPage"
          :page-sizes="[3, 5, 8, 10]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentPage"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "File",
  created() {
    this.load();
  },
  data(){
    return{
      baseApi: this.$store.state.baseApi,
      tableData: [],
      total: 0,
      pageSize: 5,
      currentPage: 1,
      fileName: '',

      multipleSelection: []
    }
  },
  methods:{
    handleSizeChange(pageSize){
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentPage(currentPage){
      this.currentPage = currentPage;
      this.load();
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    handleFileUploadSuccess() {
      this.$message.success("Upload is successful");
      this.load();
    },
    handleEnable(row){
      this.request.get("/file/enable",{params:{"id": row.id, "enable": row.enable}}).then(res=>{
        if(res.code==='200'){
          this.$message({
            type: "success",
            message: "Modification is successful",
            duration: 3000
          });
          this.load();
        }else {
          this.$message.error(res.msg);
        }
      })
    },

    load(){
      this.request.get("/file/page",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          fileName: this.fileName,
        }
      }).then(res=>{
            if(res.code==='200'){
              this.tableData = res.data.records;
              for(let s of this.tableData){
                let size = s.size;
                if(size<1024){
                  s.size = size+' Kb';
                }else if(size >1024 && size < 1024*1024){
                  s.size = (size / 1024).toFixed(2) +' Mb'
                }else{
                  s.size = size /1024/1024 .toFixed(2)+' Gb'
                }
              }
              this.total = res.data.total;
            }
          }
      )
    },

    search(){
      this.currentPage = 1;
      this.load();
    },

    reload(){
      this.fileName='';
      this.load()
    },
    handleDelete(id){
      this.$confirm('Are you sure to delete the file?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.request.delete("/file/"+id).then(res=>{
          if(res.code==='200'){
            this.$message({
              type: "success",
              message: "Success",
              duration: 3000
            });
            this.load();
          }else {
            this.$message.error(res.msg);
          }
        })
      })
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id);
      this.$confirm('Are you sure to delete these users?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.request.post("/file/del/batch",ids).then(res=>{
          if(res.code==='200'){
            this.$message({
              type: "success",
              message: "Success",
              duration: 3000
            });
            this.load();
          }else {
            this.$message.error(res.msg);
          }
        })
      })
    }
  },
}
</script>

