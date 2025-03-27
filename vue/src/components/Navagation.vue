<template>
  <div class="navagation">
    <el-row>

      <el-col :span="3">
        <div style="font-size: 20px; font-weight: bold; text-align: center">
          <a href="/"> <img src="../resource/img/logo.png" style="width: 40px;position: relative; top: 13px;right: 6px">RetailerIMS</a>
        </div>
      </el-col>
      <el-col :span="17">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal"router>
          <el-menu-item index="/" class="menu-item">Home</el-menu-item>
          <el-menu-item index="/goodList" class="menu-item">Commodities</el-menu-item>
          <el-menu-item index="/cart" class="menu-item">Shopping cart</el-menu-item>
          <el-menu-item index="/orderlist" class="menu-item">My order</el-menu-item>
          <el-menu-item index="/manage" class="menu-item"v-if="role === 'admin'">Backend</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="4">
        <el-dropdown style="cursor: pointer; float: right; margin-right: 60px">
          <span class="el-dropdown-link">
            <div style="display: inline-block">
              <img
                  v-if="user.avatarUrl != null"
                  :src="baseApi + user.avatarUrl"
                  class="avatar"
              />
              {{ user.nickname }}
              <i
                  class="el-icon-arrow-down el-icon--right"
                  style="margin-right: 5px"
              ></i>
            </div>
          </span>
          <el-dropdown-menu slot="dropdown" style="text-align: center">
            <el-dropdown-item>

              <div @click="$router.push({ path: '/login', query: { to: '/' } })"v-show="!loginStatus">
                Login
              </div>
            </el-dropdown-item>

            <el-dropdown-item v-show="loginStatus">
              <div @click="$router.push('/person')">Personal info</div>
            </el-dropdown-item>

            <el-dropdown-item v-show="loginStatus">
              <div @click="logout">Quit</div>
            </el-dropdown-item>

          </el-dropdown-menu>

        </el-dropdown>

      </el-col>
    </el-row>
  </div>
</template>


<script>
export default {
  name: "Navagation",
  props: {
    user: Object,
    loginStatus: Boolean,
    role: String,
  },
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      baseApi: this.$store.state.baseApi,
    };
  },
  methods: {
    logout() {
      localStorage.removeItem("user");
      this.$router.go(0);
      this.$message.success("Successful exit");
    },
  },
};
</script>
<style>
a {
  text-decoration: none;
}
.navagation {
  width: 100%;
  height: 60px;
  line-height: 60px;
  background-color: #ffffff;
  overflow: hidden;
}
.avatar {
  width: 30px;
  border-radius: 50%;
  position: relative;
  top: 10px;
  right: 5px;
}
.menu-item {
  padding-left: 50px;
  padding-right: 50px;
}
</style>
