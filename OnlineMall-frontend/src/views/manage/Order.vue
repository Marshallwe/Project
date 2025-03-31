<template>
  <div>
    <div>

      <el-select v-model="searchMode" placeholder="Please select the order type" style="width: 150px;margin-right: 10px">
        <el-option value="paid" label="paid"></el-option>
        <el-option value="shipped" label="shipped"></el-option>
        <el-option value="received" label="received"></el-option>
      </el-select>

      <el-input v-model="searchText" @keyup.enter.native="load" style="width: 200px">
        <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
      <el-button @click="load" type="primary" style="margin: 10px">Search</el-button>
      <el-button @click="reset" type="info" style="margin: 10px">Reset</el-button>

    </div>

    <el-table :data="tableData" border stripe fixed style="width: 100%">
      <el-table-column prop="id" label="ID" width="50" sortable> </el-table-column>
      <el-table-column prop="orderNo" label="Order number" width="200"></el-table-column>
      <el-table-column prop="totalPrice" label="Total price" width="100"></el-table-column>
      <el-table-column prop="userId" label="user id" width="100"></el-table-column>
      <el-table-column prop="linkUser" label="link user" width="150"></el-table-column>
      <el-table-column prop="linkPhone" label="link phone"></el-table-column>
      <el-table-column prop="linkAddress" label="link address" width="300"></el-table-column>

      <el-table-column prop="state" label="state" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.state==='paid'">{{scope.row.state}}</el-tag>
          <el-tag type="primary" v-if="scope.row.state==='shipped'">{{scope.row.state}}</el-tag>
          <el-tag type="info" v-if="scope.row.state==='received'">{{scope.row.state}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="create time"></el-table-column>

      <el-table-column
          fixed="right"
          label="operation"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="mini"  @click="showDetail(scope.row)">details</el-button>
          <el-popconfirm
              @confirm="delivery(scope.row)"
              title="Are you sure about delivery?"
              v-if="scope.row.state==='paid'"
          >
            <el-button type="primary" size="mini" slot="reference" style="margin-left: 10px">Deliver goods</el-button>
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

    <el-dialog :visible.sync="dialogFormVisible">
      <el-table :data="detail" background-color="black" >
        <el-table-column  label="picture" width="150" >
          <template   slot-scope="scope">
            <img :src="baseApi + scope.row.img"  min-width="100" height="100" />
          </template>
        </el-table-column>

        <el-table-column prop="goodId" label="product id"  ></el-table-column>
        <el-table-column prop="goodName" label="name"  ></el-table-column>
        <el-table-column prop="standard" label="standard"  ></el-table-column>
        <el-table-column prop="price" label="price"  ></el-table-column>
        <el-table-column prop="discount" label="discount"></el-table-column>

        <el-table-column label="Real price" >
          <template slot-scope="scope">
            {{scope.row.price * scope.row.discount}}
          </template>
        </el-table-column>
        <el-table-column prop="count" label="number" ></el-table-column>
        <el-table-column label="total price" >
          <template slot-scope="scope">
            {{scope.row.price * scope.row.discount * scope.row.count }}
          </template>
        </el-table-column>

      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import API from '../../utils/request'
const url = "/api/order/"

export default {
  name: "Order",
  data() {
    return {
      options: [],
      searchMode: '',
      searchText: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 8,
      entity: {},
      total: 0,
      dialogFormVisible: false,
      detail:[],
      baseApi: this.$store.state.baseApi,
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
    reset(){
      this.searchMode = '';
      this.searchText = '';
      this.load()
    },
    load() {
       API.get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            orderNo: this.searchText,
            state: this.searchMode
          }
       }).then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total
       })
    },
    showDetail(row){
        this.request.get("/api/order/orderNo/"+row.orderNo).then(res=>{
          if(res.code==='200'){
            this.detail=[];
            this.detail.push(res.data);
            this.dialogFormVisible = true;
          }
        })
    },
    delivery(order){
        this.request.get("/api/order/delivery/"+order.orderNo).then(res=>{
          if(res.code==='200'){
            this.$message.success("Successful delivery");
            order.state = 'shipped'
          }
        })
    },

  },
};
</script>

