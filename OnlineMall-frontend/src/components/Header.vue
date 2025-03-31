
<template xmlns:v-on="http://www.w3.org/1999/xhtml">
  <div style="display: flex">

    <div style="flex: 1;">

    <span style="font-size: 20px;cursor: pointer" :class="collapseIcon" v-on:click="$emit('collapse')" :title="collapseTitle"></span>


    <span style="font-size: 20px;cursor: pointer;margin-left: 10px" class='el-icon-back' v-on:click="back" title="return"></span>


    <el-breadcrumb style="display: inline-block; margin-left: 30px;font-size: 17px">
      <el-breadcrumb-item :to="{ path: '/manage/home' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item>{{routePath}}</el-breadcrumb-item>
    </el-breadcrumb>
    </div>

    <el-dropdown style="margin-right: 40px;cursor: pointer">
    <span class="el-dropdown-link">
      <div style="display: inline-block">
        <img :src="baseApi + user.avatarUrl" class="avatar">
             {{user.nickname }}
      <i class="el-icon-arrow-down el-icon--right" style="margin-right: 15px"></i>
      </div>
    </span>

      <el-dropdown-menu slot="dropdown" style="text-align: center">
        <el-dropdown-item>
          <div @click="$router.push('/manage/person')">PersonalInfo</div>
        </el-dropdown-item>
        <el-dropdown-item>
          <div @click="logout">Quit</div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </div>

</template>

<script>

export default {
  name: "Header",
  props: {
    collapseIcon: String,
    collapseTitle: String,
    user: Object
  },
  methods:{
    logout() {
      localStorage.removeItem("user");
      this.$router.push('/login');
      this.$message.success("quit success");
    },
    back(){
      this.$router.go(-1)
    }

  },
  data(){
    return{
      routePath: '',
      baseApi: this.$store.state.baseApi,
    }
  },
  watch: {
    '$route': function (){
      this.routePath=this.$route.meta.path
    },

  },
  created() {
    this.routePath=this.$route.meta.path;
  }
}
</script>
<style scoped>
.avatar{
  width: 30px;
  border-radius: 50%;
  position: relative;
  top: 10px;
  right: 5px;
}
</style>
