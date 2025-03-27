<template>
  <div>
    <div class="header" style="padding-left: 25px;">
      <span style="line-height: 40px">{{order.create_time}}</span>
      <span style="line-height: 40px;margin-left: 30px">Order No. :{{order.order_no}}</span>
    </div>
    <div class="body">
      <div style="display: inline-block;margin-right: 20px">
        <router-link :to="'goodview/'+order.good_id">
          <img :src="baseApi + order.imgs" style="width: 100px;height:100px">
        </router-link>
      </div>
      <div style="display: inline-block;line-height: 40px" >
        <table>
          <tr>
            <th> product </th>
            <th> Specification </th>
            <th> Quantity </th>
            <th> Total price </th>
            <th> Consignee </th>
            <th> Order status </th>
          </tr>
          <tr>
            <a :href="'goodview/'+order.good_id">
              <td>{{order.good_name}}</td>
            </a>
            <td>{{order.standard}}</td>
            <td>{{order.count}}</td>
            <td>{{order.total_price}}</td>
            <el-popover
                placement="bottom-start"
                width="200"
                trigger="hover"
                :content=address>
              <td slot="reference" style="color: #42b983">{{ order.link_user }}</td>
            </el-popover>
            <template v-if="order.state==='Have been shipped'">
              <td style="color: #42b983">{{order.state}}</td>
              <td>
                <el-button style="margin-left: 20px;" size="mini" type="primary" @click="receive">Confirm receipt of goods</el-button>
              </td>
            </template>

            <template v-else-if="order.state==='Goods received'">
              <td style="color: #42b983"><a class="el-icon-check"></a>{{order.state}}</td>
            </template>

            <template v-else-if="order.state==='Paid for'">
              <td style="color: #3b62f8"> {{order.state}}</td>
              <td>
                <el-button size="mini" type="info" plain disabled>Waiting for delivery</el-button>
              </td>
            </template>

            <template v-else>
              <td>{{order.state}}</td>
              <td>
                <el-button style="margin-left: 20px" size="mini" type="success" @click="pay">Go to pay</el-button>
              </td>
            </template>

          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "OrderItem",
  props:{
    order: Object,
  },
  created() {
  },
  data(){
    return{
      address: 'Phone:'+this.order.link_phone+' Address:'+this.order.link_address,
      baseApi: this.$store.state.baseApi,
    }
  },
  methods:{
    pay(){
      this.$router.push({path: 'pay',query:{money: this.order.total_price,orderNo: this.order.order_no}})
    },
    receive(){

      this.$confirm('Confirm receipt of goods?', 'Tip', {
        confirmButtonText: 'Sure',
        cancelButtonText: 'Cancel',
        type: 'info'
      }).then(() => {

        this.request.get("/api/order/received/"+this.order.order_no).then(res=>{
          if(res.code==='200'){
            this.$message.success("Successful receipt of goods");
            this.order.state='Goods received'
          }
        })
      })

    }
  }
}
</script>

<style scoped>
.header{
  background-color: #e75c09;
  height: 40px;
}
.body{
  background-color: white;
  padding: 20px;
}
th,td{

  width: 120px;
  text-align: center;
}
th{
  font-size: 15px;
  color: #3472a6;
  font-weight: normal;
}
</style>
