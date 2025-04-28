<template>
  <div style="margin-top: 10px; width: 90%; margin: 10px auto">
    <div style="background-color: white; padding: 10px; border-radius: 12px">
      <div style="
          padding: 10px;
          margin-bottom: 20px;
          border-bottom: 1px solid #eee;
        "
      >
        <div style="
            font-size: 20px;
            border-bottom: 2px solid dodgerblue;
            padding-bottom: 10px;
            margin-bottom: 20px;
          "
        >
          Address of delivery
          <el-button style="height: 25px; padding: 5px" @click="addAddress">+</el-button>
        </div>
        <template v-for="(item, index) in addressData">
          <address-box
            :address="item"
            :key="index"
            :class="index === checkedIndex ? 'active' : ' '"
            style="margin-right: 20px;cursor:pointer;"
            @edit="editAddress(item)"
            @delete="deleteAddress(item)"
            @click.native="select(index)"
          ></address-box>
        </template>
      </div>
      <el-dialog title="Address information" :visible.sync="dialogFormVisible">
        <el-form label-width="90px" style="padding: 0 60px">
          <el-form-item label="linkUser">
            <el-input v-model="address.linkUser" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="linkPhone">
            <el-input v-model="address.linkPhone" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="linkAddress">
            <el-input
              v-model="address.linkAddress"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveAddress">Confirm</el-button>
        </div>
      </el-dialog>

      <el-table :data="goods" stripe style="width: 100%">
        <el-table-column label="Picture of product" width="150">
          <template slot-scope="scope">
            <el-image
              :src="baseApi + scope.row.imgs"
              style="width: 100px; height: 100px"
              fit="contain"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="name"></el-table-column>
        <el-table-column prop="standard" label="standard"></el-table-column>
        <el-table-column prop="realPrice" label="realPrice"></el-table-column>
        <el-table-column prop="num" label="number"></el-table-column>
        <el-table-column label="价格">
          <template slot-scope="scope">
            {{ (scope.row.realPrice * scope.row.num).toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 10px">
        <div style="background-color: white; padding: 10px">
          <div style="color: red; text-align: right">
            <div>
              <span>Total price:</span>
              <span>￥ {{ sumPrice }}</span>
            </div>
            <div style="text-align: right; color: #999; font-size: 12px">
              A discount: ￥{{ sumDiscount }}
            </div>
            <div style="padding: 10px 0">
              <el-button
                style="background-color: red; color: white; width: 100px"
                @click="submitOrder"
                >submit order</el-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import API from "@/utils/request";
import addressBox from "@/components/AddressBox";
export default {
  name: "cart",
  data() {
    return {
      baseApi: this.$store.state.baseApi,
      userId: 0,
      addressData: [],
      address: {},
      checkedIndex: 0,
      dialogFormVisible: false,
      good: {},
      realPrice: -1,
      goods: [],
      cartId: "",
    };
  },
  components: {
    "address-box": addressBox,
  },
  created() {
    this.loadAddress();

    this.good = JSON.parse(this.$route.query.good);
    this.good.realPrice = this.$route.query.realPrice;
    this.good.num = this.$route.query.num;
    this.good.standard = this.$route.query.standard;
    this.cartId = this.$route.query.cartId;
    this.goods.push(this.good);
  },
  computed: {
    sumPrice: function () {
      let sum = 0;
      this.goods.forEach(function (good) {
        sum += good.realPrice * good.num;
      });
      return sum.toFixed(2);
    },
    sumDiscount: function () {
      let sum = 0;
      this.goods.forEach(function (good) {
        sum += (good.realPrice / good.discount - good.realPrice) * good.num;
      });
      return sum.toFixed(2);
    },
  },
  methods: {
    select(index) {
      this.checkedIndex = index;
    },
    addAddress() {
      this.address = {};
      this.dialogFormVisible = true;
    },
    editAddress(item) {
      this.address = { ...item };
      this.dialogFormVisible = true;
    },
    deleteAddress(item) {
      this.$confirm("Are you sure to delete this address?", "Tip", {
        confirmButtonText: "Confirm",
        cancelButtonText: "Cancel",
        type: "warning",
      }).then(() => {
        API.delete("api/address/" + item.id).then((res) => {
          if (res.code === "200") {
            this.$message.success("Delete address successfully");
            this.loadAddress();
          }
        });
      });
    },
    saveAddress() {
      this.address.userId = this.userId;
      const method = this.address.id ? API.put : API.post;
      const url = this.address.id ? "/api/address" : "/api/address";

      method(url, this.address).then((res) => {
        if (res.code === "200") {
          this.$message.success("Success");
          this.loadAddress();
          this.dialogFormVisible = false;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    loadAddress() {
      API.get("/userid").then((res) => {
        this.userId = res;
        API.get("/api/address/" + res).then((res) => {
          if (res.code === "200") {
            this.addressData = res.data;
          }
        });
      });
    },
    submitOrder() {
      let address = this.addressData[this.checkedIndex];
      if (!address) {
        this.$message({
          type: "warning",
          message: "Please select the delivery address!",
        });
        return;
      }

      API.post("/api/order", {
        totalPrice: this.sumPrice,
        linkUser: address.linkUser,
        linkPhone: address.linkPhone,
        linkAddress: address.linkAddress,
        state: "To be paid",
        goods: JSON.stringify(this.goods),
        cartId: this.cartId,
      }).then((res) => {
        if (res.code === "200") {
          let orderNo = res.data;
          this.$router.replace({
            path: "pay",
            query: { money: this.sumPrice, orderNo: orderNo },
          });
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
        }
      });
    },
  },
};
</script>

<style scoped>
.active {
  border: black 5px solid;
}
</style>
