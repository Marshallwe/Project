
<template>
  <div class="login-index" :style="backgroundDiv">

    <div class="login-window-index">

      <div class="title">
        <b><img src="../resource/logo.png" style="width: 40px;position: relative; top: 13px;right: 6px">
          <span style="color: #e75c09">Inventory Management System</span>
        </b>
      </div>

      <div style="margin-top: 30px">

        <el-form label-width="70px">

          <el-form-item label="username">
            <el-input v-model.trim="user.username" aria-required="true"></el-input>
          </el-form-item>

          <el-form-item label="password" style="margin-top: 25px">
            <el-input v-model.trim="user.password" show-password aria-required="true"></el-input>
          </el-form-item>

          <el-form-item style="margin: 30px 80px">
            <el-button type="success" @click="onSubmit">Login</el-button>
            <el-button @click="$router.push('/register')">Register</el-button>
          </el-form-item>

        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import md5 from 'js-md5'
export default {
  name: "Login",
  data(){
    return {
      to: '/',
      user: {},
      backgroundDiv: {
        backgroundImage:
            "url(" + require("@/resource/img/login_back.png") + ")",
        backgroundRepeat: "no-repeat",
        backgroundSize: "100% 100%",
      },
    }
  },
  created() {
    this.to = this.$route.query.to ? this.$route.query.to : "/"
  },
  methods: {

    onSubmit() {
      if(this.user.username==='' || this.user.password===''){
        this.$message.error("The account number or password cannot be empty")
        return false;
      }
      this.user.password = md5(this.user.password);
      this.request.post("/login",this.user).then(res=>{
        if(res.code==='200'){
          this.$message.success({message: "Successful login",showClose: true})
          this.$router.push(this.to)
          localStorage.setItem("user",JSON.stringify(res.data))
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.login-index {
  background: #ffffff;
  height: 100%;
  position: relative;
}

.login-window-index {
  padding: 20px;
  width: 450px;
  height: 280px;
  background: #ffffff;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.title {
  text-align: center;
  margin: 30px auto;
  font-size: 25px;
}
</style>
