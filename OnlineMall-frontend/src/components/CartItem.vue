
<template>
  <div>
    <div class="header" style="padding-left: 25px;">
      <span style="line-height: 40px">{{ cart.createTime }}</span>
    </div>
    <div class="body">

      <div style="display: inline-block;margin-right: 20px">
        <router-link :to="'goodview/'+cart.goodId">
          <img :src="baseApi + cart.img" style="width: 100px;height:100px">
        </router-link>
      </div>

      <div style="display: inline-block;line-height: 40px" >
        <table>
          <tr>
            <th> product </th>
            <th> Specification </th>
            <th> price </th>
            <th> Quantity </th>
            <th> Total price </th>
            <th> operation </th>
          </tr>

          <tr>
            <a :href="'goodview/'+cart.goodId">
              <td>{{ cart.goodName }}</td>
            </a>
            <td>{{cart.standard}}</td>
            <td>{{realPrice}}</td>
            <td>
              <el-button size="mini" @click="countChangeFlag=true" v-if="!countChangeFlag">
                {{cart.count}}
              </el-button>
              <el-input-number v-model="cart.count" :min="1" :max="cart.store" v-if="countChangeFlag" style="width: 120px" ></el-input-number>
              </td>
            <td>{{totalPrice}}</td>
            <td>
              <el-button size="mini" type="success" @click="pay">pay</el-button>
              <el-popconfirm
                  @confirm="del"
                  title="Sure to delete?"
              >
                <el-button size="mini" type="danger" icon="el-icon-delete" slot="reference" style="margin-left: 10px"></el-button>
              </el-popconfirm>
            </td>

          </tr>

        </table>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "CartItem",
  props:{
    cart: Object,
    countChangeFlag: false,
  },
  created() {

  },
  data(){
    return{

      baseApi: this.$store.state.baseApi,
    }
  },
  computed:{
    totalPrice:function () {
      return (this.realPrice * this.cart.count).toFixed(2)
    },
    realPrice: function (){
      return (this.cart.price * this.cart.discount)
    }
  },
  methods:{
    del(id){
      this.request.delete("/api/cart/"+this.cart.id).then(res=>{
        if(res.code==='200'){
          this.$message.success("Successful deletion")
          this.$emit('delete',this.cart.id)
        }
      })
    },
    pay(){
      let good = {id: this.cart.goodId,name: this.cart.goodName,imgs: this.cart.img,discount: this.cart.discount}
      this.$router.push({name: 'preOrder',query: {good: JSON.stringify(good), realPrice: this.realPrice, num: this.cart.count, standard: this.cart.standard, cartId: this.cart.id}})
    },
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
