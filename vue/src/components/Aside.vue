
<template>
  <el-menu :default-openeds="['2', 'good']" style="height: 100%;"
           background-color="rgb(28,28,28)"
           text-color="#fff"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >

    <div style="height: 60px;margin-left: 30px; line-height: 60px">
      <router-link to="/manage/home">
        <img src="../resource/img/logo.png" style="width: 40px;position: relative; top: 13px;right: 6px">
      </router-link>
      <span slot="title" style="color: aliceblue;font-size: 20px" v-show="!isCollapse">RetailerIMS</span>
    </div>

    <el-menu-item index="/manage/home">
      <i class="el-icon-house"></i><span slot="title">Home</span>
    </el-menu-item>

    <el-menu-item index="/">
      <i class="el-icon-house"></i><span slot="title">Front</span>
    </el-menu-item>

    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i><span slot="title">System Manage</span></template>
      <el-submenu v-show="userGroup" index="user">

        <template slot="title">Account related</template>
        <el-menu-item index="/manage/user" v-if="menuFlags.userMenu">User management</el-menu-item>
      </el-submenu>

      <el-submenu v-if="fileGroup" index="file">
        <template slot="title">File dependent</template>
        <el-menu-item index="/manage/file" v-if="menuFlags.fileMenu">File management</el-menu-item>
        <el-menu-item index="/manage/avatar" v-if="menuFlags.avatarMenu">Avatar management</el-menu-item>
      </el-submenu>

      <el-submenu v-if="GoodGroup" index="good">
        <template slot="title">Retailer Manage</template>
        <el-menu-item index="/manage/category" v-if="menuFlags.categoryMenu">Commodity classification</el-menu-item>
        <el-menu-item index="/manage/carousel" v-if="menuFlags.carouselMenu">Carousel graph</el-menu-item>
        <el-menu-item index="/manage/good" v-if="menuFlags.goodMenu">Commodity management</el-menu-item>
        <el-menu-item index="/manage/order" v-if="menuFlags.orderMenu">Order management</el-menu-item>
      </el-submenu>

      <el-submenu v-if="incomeGroup" index="income">
        <template slot="title">Sales statistics</template>
        <el-menu-item index="/manage/incomeChart" v-if="menuFlags.incomeChartMenu">Chart analysis</el-menu-item>
        <el-menu-item index="/manage/incomeRank" v-if="menuFlags.incomeRankMenu">Top Income ranking</el-menu-item>
      </el-submenu>

    </el-submenu>
  </el-menu>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
  },

  data() {
    return {
      role: 'user',
      menuFlags: {
        userMenu: false,
        fileMenu: false,
        avatarMenu: false,
        goodMenu: false,
        carouselMenu: false,
        orderMenu: false,
        categoryMenu: false,
        incomeChartMenu: false,
        incomeRankMenu: false,
      }
    }
  },

  computed: {
    userGroup: function () {
      return this.menuFlags.userMenu
    },
    fileGroup: function () {
      return this.menuFlags.fileMenu || this.menuFlags.avatarMenu
    },
    GoodGroup: function () {
      return this.menuFlags.goodMenu || this.menuFlags.orderMenu || this.menuFlags.categoryMenu || this.menuFlags.carouselMenu
    },
    incomeGroup: function () {
      return this.menuFlags.incomeChartMenu || this.menuFlags.incomeRankMenu
    }
  },
  created() {

    request.post("http://localhost:8888/role").then(res => {
      if (res.code === '200') {
        this.role = res.data;
        if (this.role === 'admin') {
          this.menuFlags.userMenu = true
          this.menuFlags.fileMenu = true
          this.menuFlags.avatarMenu = true
          this.menuFlags.categoryMenu = true
          this.menuFlags.goodMenu = true
          this.menuFlags.carouselMenu = true
          this.menuFlags.orderMenu = true
          this.menuFlags.incomeChartMenu = true
          this.menuFlags.incomeRankMenu = true
        } else if (this.role === 'user') {

        }
      }
    })
  }
}
</script>

