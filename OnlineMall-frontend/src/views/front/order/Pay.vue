<template>
<div style="margin-top: 10px;width: 50%;margin: 10px auto;background-color: white;text-align: center">
  <table style="margin: 20px auto">
    <tr>
      <th>Order No.</th>
      <td>{{orderNo}}</td>
    </tr>

    <tr>
      <th>Amount of money</th>
      <td>{{money}} Yuan</td>
    </tr>

  </table>
  <hr style="width: 280px" />
  <div>Ï、
    <span>Method of payment:</span>
  </div>
  <br>
  <img src="../../../resource/img/WeChat.png" style="width: 45px;height: 45px;margin-right: 60px;cursor:pointer;" @click="pay">
  <img src="../../../resource/img/AliPay.png" style="width: 50px;height: 50px;cursor:pointer;" @click="pay">
</div>
</template>

<script>
export default {
  name: "Pay",
  data(){
    return{
      userId: 0,
      money1: 0,
      orderNo: '',
    }
  },
  created() {
    this.money = parseFloat(this.$route.query.money).toFixed(2);
    this.orderNo = this.$route.query.orderNo;
  },

  methods:{
    pay(){
      this.request.get("/api/order/paid/"+this.orderNo).then(res=>{
        if(res.code==='200'){
          alert("You successfully paid "+this.money+" yuan")
          this.$router.replace("/orderlist")
        }else{
          this.$message.error(res.msg)
        }
      })

    }
  }

}
</script>

<style scoped>
tr{
  line-height:40px;
}

</style>
