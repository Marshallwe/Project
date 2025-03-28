<template>
  <div class="main-box">
    <div>
      <div class="image-box">
        <img :src="baseApi + good.imgs" class="image" />
      </div>

      <div class="detail-box">

        <div>
          <span style="font-size: 22px"
            ><strong>{{ good.name }}</strong></span
          ><br />
        </div>
        <div style="margin-top: 20px">
          <span style="font-size: 17px;">{{
            good.description
          }}</span>
        </div>

        <div class="price-box" v-if="good.discount < 1">
          <dl>
            <div>
              <dt>Original price</dt>
              <dd style="text-decoration: line-through"><b>{{ price }}</b>Yuan</dd>
            </div>
            <div>
              <dt>Discount rate</dt>
              <dd>{{ discount }}</dd>
            </div>
            <div>
              <dt>Current price</dt>
              <dd style="color: red; font-size: 25px"><b>{{ realPrice }}</b>元</dd>
            </div>
          </dl>
        </div>
        <div class="price-box" v-if="good.discount === 1">
          <dl>
            <div>
              <dt>Price</dt>
              <dd style="color: red; font-size: 25px">￥ <b>{{ price }}</b></dd>
            </div>
          </dl>
        </div>

        <div style="margin-top: 20px">
          <span>Monthly sales:</span>
          <span>{{ good.sales }}</span
          ><br />
          <span style="height: 40px" v-if="showStore">In stock:{{ store }}</span>
        </div>
        <div
          style="margin-top: 15px; height: 50px"
          v-if="standards.length !== 0"
        >
          <el-radio-group
            v-for="(standard, index) in standards"
            v-model="checkedStandard"
            @change="change(standard)"
            :key="index"
          >
            <el-radio-button
              class="standard"
              :label="standard.value"
            ></el-radio-button>
          </el-radio-group>
        </div>
        <div style="margin-top: 20px">
          <el-input-number
            v-model="count"
            controls-position="right"
            :min="1"
            :max="store"
          ></el-input-number>
        </div>

        <div style="margin-top: 30px">
          <el-button type="success" @click="goToOrder">Buy</el-button>
          <el-button type="primary" @click="addToCart" icon="el-icon-shopping-cart-1">Add to cart</el-button>
        </div>
      </div>
    </div>

    <div id="message">
      <div class="title">Comments</div>
      <div class="wrapper">
        <div class="content">
          <el-input
              type="textarea"
              :rows="3"
              placeholder="Content of comments"
              v-model="content"
              clearable>
          </el-input>
          <el-rate v-model="score"></el-rate>
        </div>
        <div class="btn">

          <el-button type="primary" @click="submit()">Submit a review</el-button>

        </div>
        <div class="all">
            <el-card class="list"
                @mouseenter="enter(index)"
                @mouseleave="leave(index)"
                v-for="(data,index) in msg" :key="index"
            >
              <p class="content">{{data.content}} </p>
            </el-card>

        </div>
        <div class="pagination">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pagination.current"
              :page-sizes="[4,6,8,10]"
              :page-size="pagination.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="pagination.total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>

</template>

<script>

export default {
  name: "GoodView",
  data() {
    return {
      userId:null,
      baseApi: this.$store.state.baseApi,
      score: 5,
      good: {},
      goodId: Number,
      price: -1,
      isDiscount: false,
      discount: "",
      standards: [],
      checkedStandard: "",
      store: 0,
      showStore: false,
      count: 1,
      title: "",
      content: "",
      pagination: {
        current: 1,
        total: null,
        size: 4
      },
      msg: []
    };
  },
  methods: {
    getPriceRange(standards) {
      let arr = standards.map((item) => {
        return item.price;
      });
      //选择排序
      for (let i = 0; i < arr.length; i++) {
        let min = i;
        for (let j = i + 1; j < arr.length; j++) {
          if (arr[j] < arr[min]) {
            min = j;
          }
        }
        [arr[i], arr[min]] = [arr[min], arr[i]];
      }
      if (arr[0] === arr[arr.length - 1]) {
        return arr[0];
      } else {
        return arr[0] + "Yuan ~ " + arr[arr.length - 1];
      }
    },
    change(standard) {
      this.showStore = true;
      this.price = standard.price;
      this.store = standard.store;
    },
    goToOrder() {
      if (this.standards.length !== 0) {
        if (this.checkedStandard === "") {
          this.$message.warning("Please select specifications");
          return false;
        }
      }
      this.$router.push({
        name: "preOrder",
        query: {
          good: JSON.stringify(this.good),
          realPrice: this.realPrice,
          num: this.count,
          standard: this.checkedStandard,
        },
      });
    },
    addToCart() {
      if (!localStorage.getItem("user")) {
        this.$router.push("/login");
      }
      if (!this.checkedStandard) {
        this.$message.error("Please select specifications");
        return false;
      }
      this.request.get("/userid").then((res) => {
        let userId = res;
        this.userId = res;
        let cart = {
          userId: userId,
          goodId: this.goodId,
          standard: this.checkedStandard,
          count: this.count,
        };
        this.request.post("/api/cart", cart).then((res) => {
          if (res.code === "200") {
            this.$message.success("Shopping cart added successfully");
          }
        });
      });
    },
    getMsg() {
      this.request.get("/userid").then((res) => {
        this.userId = res;
      });

      this.request.get(`/messages/${this.goodId}/${this.pagination.current}/${this.pagination.size}`).then(res => {
        let status = res.code
        if(status == 200) {
          this.msg = res.data.records
          this.pagination = res.data
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getMsg()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getMsg()
    },

    submit() {
      let date = new Date()
      if(false) {
        this.$message({
          type: 'error',
          message: '1',
        })
      } else {
        this.request.post("/message", {
            title: this.title,
            goodId: this.goodId,
            content: this.content,
            score:this.score,
            time: date,
            userId: this.userId
          }
        ).then(res => {
          let code = res.data.code
          if(code == 200) {
            this.$message({
              type: "success",
              message: "Success"
            })
          }
          this.getMsg()
        })
      }
      this.title = ""
      this.content = ""
      this.getMsg()
    },
    replay(messageId) {
      this.$prompt('Reply to a message', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
        inputErrorMessage: 'Reply can not be empty'
      }).then(({ value }) => {
        let date = new Date()
        console.log(messageId)
        this.request.post( '/replay',
          {
            replay: value,
            replayTime: date,
            messageId: messageId
          }
        ).then(res => {
          this.getMsg()
        })
        this.$message({
          type: 'success',
          message: 'Success'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Cancel input'
        });
      });
    },
    enter(index) {
      this.flag = true
      this.current = index
    },
    leave(index) {
      this.flag = false;
      this.current = index;
    }
  },

  created() {
    this.goodId = this.$route.params.goodId;
    this.getMsg()

    this.request.get("/api/good/" + this.goodId).then((res) => {
      if (res.code === "200") {
        this.good = res.data;
        let discount = this.good.discount;
        if (discount < 1) {
          this.isDiscount = true;
          this.discount = discount * 10 + "off";
        }
      } else {
        this.$router.go(0);
      }
    });
    this.request.get("/api/good/standard/" + this.goodId).then((res) => {
      if (res.code === "200") {
        let standards = JSON.parse(res.data);
        this.standards = standards;
        this.price = this.getPriceRange(standards);
      } else {
        this.price = this.good.price;
        this.store = this.good.store;
        this.showStore = true;
      }
    });
  },
  mounted() {},
  computed: {
    realPrice: function () {
      if (this.good.discount < 1) {
        if (isNaN(this.price)) {
          let down =
            this.price.substring(0, this.price.indexOf("元")) *
            this.good.discount;
          let up =
            this.price.substring(this.price.lastIndexOf(" ")) *
            this.good.discount;
          return down.toFixed(2) + "元 ~ " + up.toFixed(2);
        } else {
          return (this.price * this.good.discount).toFixed(2);
        }
      }
      return this.price;
    },
  },
};
</script>

<style scoped>
.main-box {
  width: 1060px;
  margin: 20px auto;
  padding: 30px;
  background-color: #ffffff;
  overflow: hidden;
}
.image-box {
  height: 420px;
  width: 420px;
  border: #f2f2f2 1px solid;
  text-align: center;
  margin-left: 80px;
  margin-top: 30px;
  display: inline-block;
  overflow: hidden;
}
.image {
  height: 100%;
  width: 350px;
}
.detail-box {
  width: 420px;
  display: inline-block;
  margin-left: 50px;
  overflow: hidden;
}
.price-box {
  background-color: #e9e9e9;
  border-radius: 5px;
  font: 12px/1.5 "Microsoft Yahei", tahoma, arial;
  padding-bottom: 1px;
  padding-top: 1px;
  margin-right: 20px;
  margin-top: 30px;
}
.price-box div {
  line-height: 20px;
  margin-left: 8px;
  margin-bottom: 5px;
}
.price-box dl dt {
  float: left;
  font-size: 14px;
  line-height: 20px;
}
.price-box dl dd {
  font-size: 18px;
  line-height: 20px;
}
.button {
  width: 130px;
  height: 45px;
  background-color: #96e2e0;
  color: #710a0a;
}
.standard {
  height: 30px;
  margin-right: 10px;
}

.pagination {
  display: flex;
  justify-content: center;
}
#message {
  width: 980px;
  margin: 0 auto;
}
.title {
  margin: 20px;
}
.content {
  padding: 20px 0px;
}
#message  {
  .btn {
    padding-bottom: 20px;
  }
  .all {
    .date {
      color: rgb(80, 157, 202);
      line-height: 45px;
      font-size: 13px;
    }
    .list {
      background-color: #eee;
      padding:10px;
      border-radius: 4px;
      margin: 10px 0px;
      position: relative;
      transition: all .3s ease;
      .title {
        color: #5f5f5f;
        margin: 0px;
        font-size: 13px;
        line-height: 30px;
      }
      .content {
        padding: 0px;
      }
      .icon-untitled33 {
        font-size: 13px;
        margin-right: 4px;
      }
      .icon-date {
        font-size: 13px;
        margin-right: 4px;
        color: rgb(80, 157, 202);
      }
      .replay {
        position: absolute;
        right: 30px;
        bottom: 15px;
        color: tomato;
        cursor: pointer;
        transition: all .3s ease;
      }
      .comment {
        margin:-7px 0px;
        padding-bottom: 12px;
        font-size: 13px;
        color: #28b2b4;
        i {
          margin-right: 4px;
        }
      }
    }
  }
}
#message .wrapper {
  background-color: #fff;
  padding: 20px;

}
</style>
