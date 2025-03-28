
<template>
  <div>

    <div style="text-align: left">
      <el-button @click="add" round type="success" style="margin: 10px;width: 150px">
        <i class="el-icon-circle-plus"style="padding-right: 6px"></i>Add</el-button>
    </div>

    <div>
      <el-table :data="tableData" stripe border fixed style="width: 100%;margin: 2px auto">
        <el-table-column width="400" label="Product">
          <template slot-scope="scope">
            <a :href="'/goodView/'+scope.row.goodId">{{scope.row.goodName}}</a>
          </template>
        </el-table-column>

        <el-table-column width="350"  label="Picture" >
          <template  slot-scope="scope">
            <img :src="baseApi + scope.row.img" width="200" height="180" />
          </template>
        </el-table-column>
        <el-table-column prop="showOrder" width="150" label="Sequence of rotation"></el-table-column>
        <el-table-column
            fixed="right"
            label="Operation"
            width="400">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit"   @click="edit(scope.row)"></el-button>
            <el-popconfirm
                @confirm="del(scope.row.id)"
                title="Sure to delete?"
            >
              <el-button type="danger" icon="el-icon-delete"  slot="reference" style="margin-left: 10px"></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>
    </div>

    <el-dialog title="Information" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">

      <el-form :model="entity">

        <el-form-item label="product id" label-width="150px">
          <el-input v-model="entity.goodId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="Sequence of rotation" label-width="150px">
          <el-select v-model="entity.showOrder">
            <el-option v-for="index in tableData.length" :key="index" :label="index" :value="index">
            </el-option>
          </el-select>
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
const url = "/api/carousel/"

export default {
  name: "Carousel",
  data() {
    return {
      baseApi: this.$store.state.baseApi,
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
    load() {
      API.get(url).then(res => {
        this.tableData = res.data || []
      })
    },
    add() {
      this.entity = {}
      this.tableData.length++;
      this.dialogFormVisible = true
    },
    edit(row) {
      this.entity = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    save() {
      if(this.entity.goodId == undefined || this.entity.goodId === "") {
          this.$message.error("The item id cannot be empty")
          return;
      }
      if(this.entity.showOrder == undefined) {
          this.$message.error("The carousel order cannot be empty")
          return;
      }

      API.post(url, this.entity).then(res => {
        if (res.code === '200') {
          this.$message.success("Save successfully")
          this.load()
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      API.delete(url + id).then(res => {
        if(res.code==='200'){
          this.$message({
            type: "success",
            message: "Success",
          });
          this.load();
        }else {
          this.$message.error(res.msg);
        }
      })
    }
  },
};
</script>
