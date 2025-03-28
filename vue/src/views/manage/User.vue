<template>
<div>

  <div class="demo-input-size">
    <el-select v-model="searchMode" placeholder="Please select" style="width: 150px;margin-right: 10px">
      <el-option value="id" label="user id"></el-option>
      <el-option value="username" label="username"></el-option>
      <el-option value="nickname" label="nickname"></el-option>
    </el-select>
    <el-input v-if="searchMode==='id'" placeholder="Please enter the user id" prefix-icon="el-icon-search" style="width: 250px;padding-right: 5px" v-model="searchParams.id"></el-input>
    <el-input v-if="searchMode==='nickname'" placeholder="Please enter a nickname" prefix-icon="el-icon-search" style="width: 250px;padding-right: 5px" v-model="searchParams.nickname"></el-input>
    <el-button round type="primary" @click="search">Search</el-button>
    <el-button round type="info" @click="reload">Reset</el-button>
  </div>

  <div style="padding-top: 10px">
    <el-button style="margin: 5px;width: 100px" round type="success" @click="handleAdd"><i class="el-icon-circle-plus"style="padding-right: 6px"></i>Add</el-button>
    <el-button style="margin: 5px;width: 120px" round type="danger" @click="delBatch"><i class="el-icon-remove" style="padding-right: 6px"></i>Batch deletion</el-button>
  </div>

  <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible">
    <el-form label-width="50px" style="padding: 0 60px">
      <el-form-item label="nickname">
        <el-input v-model="user.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="role">
        <el-select v-model="user.role" placeholder="Please select">
          <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="phone">
        <el-input v-model="user.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="email">
        <el-input v-model="user.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="address">
        <el-input v-model="user.address" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">Cancel</el-button>
      <el-button type="primary" @click="save">Confirm</el-button>
    </div>
  </el-dialog>

  <el-table stripe border fixed size="medium" :data="tableData" background-color="black" @selection-change="handleSelectionChange">
    <el-table-column type="selection" ></el-table-column>
      <el-table-column prop="id" label="id" width="50" ></el-table-column>
    <el-table-column prop="username" label="username" width="100" ></el-table-column>

    <el-table-column label="role" width="100" >
      <template slot-scope="scope">
        <span v-if="scope.row.role==='user'">user</span>
        <span v-if="scope.row.role==='admin'">admin</span>
      </template>
    </el-table-column>

    <el-table-column prop="nickname" label="nickname" width="100" ></el-table-column>
    <el-table-column prop="phone" label="phone" width="180" ></el-table-column>
    <el-table-column prop="email" label="email" width="180" ></el-table-column>
    <el-table-column prop="address" label="address" width="200" ></el-table-column>

    <el-table-column label="operation">
      <template slot-scope="scope">
        <el-button
            size="mini"
            type="success"
            @click="handleEdit(scope.row)">Edit</el-button>
        <el-button
            size="mini"
            type="danger"
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
  name: "User",

  created() {
    this.load();
  },
  data(){
    return{
      tableData: [],
      roleOptions: [{
        value: 'admin',
        label: 'admin'
      }, {
        value: 'user',
        label: 'user'
      },
      ],
      roleValue: '',
      total: 0,
      pageSize: 5,
      currentPage: 1,
      searchMode: 'id',
      searchParams:{
        id: '',
        username: '',
        nickname: ''
      },
      dialogFormVisible: false,
      dialogTitle: '',
      user: {},
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
      this.request.get("/user/page",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          id: this.searchParams.id,
          username: this.searchParams.username,
          nickname: this.searchParams.nickname
        }
      }).then(res=>{
            if(res.code==='200'){
              this.tableData = res.data.records;
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
      this.searchParams.id='';
      this.searchParams.username='';
      this.searchParams.nickname='';
      this.load()
    },
    save(){
      this.dialogTitle='newuser'
      this.request.post("/user",this.user).then(res=>{
        if(res.code==='200'){
          this.$message.success("save success");
          this.dialogFormVisible = false;
          this.load();
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd(){
      this.dialogTitle='newuser'
      this.dialogFormVisible = true;
      this.user = {};

    },
    handleEdit(row){
      this.user = JSON.parse(JSON.stringify(row));
      this.dialogTitle='edituser';
      this.dialogFormVisible = true;
    },
    handleDelete(id){
      this.$confirm('Are you sure to delete the user?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.request.delete("/user/"+id).then(res=>{
          if(res.code==='200'){
            this.$message({
              type: "success",
              message: "success",
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
      this.$confirm('Are you sure to delete the user?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.request.post("/user/del/batch",ids).then(res=>{
          if(res.code==='200'){
            this.$message({
              type: "success",
              message: "success",
              duration: 3000
            });
            this.load();
          }else {
            this.$message.error(res.msg);
          }
        })
      })
    }
  }
}
</script>
