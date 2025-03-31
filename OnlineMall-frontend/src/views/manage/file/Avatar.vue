
<template>
  <div>
    <el-table stripe border fixed :data="tableData" background-color="black" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" ></el-table-column>
      <el-table-column  label="Picture of head" width="150" >
        <template   slot-scope="scope">
          <img :src="baseApi + scope.row.url"  min-width="100" height="100" />
        </template>
      </el-table-column>

      <el-table-column prop="type" label="Type of file" width="180" ></el-table-column>
      <el-table-column prop="size" label="File size" width="180" ></el-table-column>

      <el-table-column label="operation">
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
              @click="handleDelete(scope.row.id)"
          >Delete
          </el-button>
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
  name: "Avatar",
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
    load(){
      this.request.get("/avatar/page",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
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
    //删除
    handleDelete(id){
      this.$confirm('Are you sure to delete the file?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.request.delete("/avatar/"+id).then(res=>{
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
  },
}
</script>
