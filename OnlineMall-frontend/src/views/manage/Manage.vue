
<template>
  <div style="height: 100%">

    <el-container style="height: 100%">

      <el-aside width="sideWidth + 'px'">
        <Aside :is-collapse="isCollapse"></Aside>
      </el-aside>

      <el-container>
        <el-header style="border-bottom: 1px solid;">
          <Header
              :collapse-icon="collapseIcon"
              :collapse-title="collapseTitle"
              @collapse="handleCollapse"
              :user="user"
          ></Header>
        </el-header>
        <el-main :class="{bk: $route.path=='/manage/home'}">
          <router-view @refresh="getUser"/>
        </el-main>

      </el-container>

    </el-container>
  </div>
</template>

<style>
.el-header {
  background-color: #ffffff;
  color: #000000;
  line-height: 60px;
}

.el-aside {
  background-color: #101010;
  color: #000000;
}

.bk {
  width: 100%;
  background: url("@/resource/img/back.jpg") center center no-repeat;
  background-size: 100% 100%;
}

</style>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  data() {
    return {
      user: {},
      isCollapse: false,
      sideWidth: 250,
      collapseIcon: "el-icon-s-fold",
      collapseTitle: "shrink",
    };
  },
  components: {
    Aside,
    Header,
  },

  methods: {
    handleCollapse() {
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        this.sideWidth = 64;
        this.collapseIcon = "el-icon-s-unfold";
        this.collapseTitle = "unfold";
      } else {
        this.sideWidth = 250;
        this.collapseIcon = "el-icon-s-fold";
        this.collapseTitle = "shrink";
      }
    },
    getUser() {
      let username = localStorage.getItem("user")
          ? JSON.parse(localStorage.getItem("user")).username
          : "";
      if (username) {
        this.request.get("/userinfo/" + username).then((res) => {
          this.user = res.data;
        });
      }
    },
  },
  created() {
    this.getUser();
  },
};
</script>
