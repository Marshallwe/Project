<template>
  <div class="login-container">
    <div class="login-card">
      <div class="card-content">
        <!-- 标题部分 -->
        <div class="header-section">
          <h1 class="main-title">Inventory Management System</h1>
          <p class="sub-title">Sign in to your account</p>
        </div>

        <!-- 表单部分 -->
        <el-form
            ref="loginForm"
            :model="loginForm"
            :rules="rules"
            label-position="top"
            class="login-form"
            @submit.native.prevent="confirm"
        >
          <!-- 账号输入 -->
          <el-form-item label="Account" prop="no">
            <el-input
                v-model="loginForm.no"
                placeholder="Enter your account"
                prefix-icon="el-icon-user"
                clearable
                tabindex="1"
            ></el-input>
          </el-form-item>

          <!-- 密码输入 -->
          <el-form-item label="Password" prop="password">
            <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="Enter your password"
                prefix-icon="el-icon-lock"
                show-password
                tabindex="2"
                @keyup.enter.native="confirm"
            ></el-input>
          </el-form-item>

          <!-- 提交按钮 -->
          <el-form-item class="action-item">
            <el-button
                type="primary"
                native-type="submit"
                class="submit-btn"
                :loading="confirm_disabled"
            >
              {{ confirm_disabled ? 'Signing in...' : 'Sign In' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 底部信息 -->
        <div class="footer-note">
          <span>© 2025 IM System</span>
          <span>v1.0.0</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      confirm_disabled: false,
      loginForm: {
        no: '',
        password: ''
      },
      rules: {
        no: [
          {
            required: true,
            message: 'Please input account',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 20,
            message: 'Length should be 1 to 20 characters',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: 'Please input password',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 20,
            message: 'Length should be 1 to 20 characters',
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    confirm() {
      this.$refs.loginForm.validate(valid => {
        if (!valid) return;

        this.confirm_disabled = true;

        this.$axios.post(this.$httpUrl + '/user/login', this.loginForm)
            .then(res => {
              const { code, data } = res.data;
              if (code === 200) {
                this.handleLoginSuccess(data);
              } else {
                this.handleLoginError();
              }
            })
            .catch(error => {
              console.error('Login error:', error);
              this.handleLoginError();
            });
      });
    },
    handleLoginSuccess(data) {
      sessionStorage.setItem("CurUser", JSON.stringify(data.user));
      this.$store.commit("setMenu", data.menu);
      this.$router.replace('/Index');
      this.$message.success('Login successful!');
    },
    handleLoginError() {
      this.confirm_disabled = false;
      this.$message.error('Invalid credentials. Please try again.');
      this.loginForm.password = '';
      this.$refs.loginForm.clearValidate('password');
    }
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 2rem;
}

.login-card {
  background: rgba(255, 255, 255, 0.98);
  border-radius: 1.5rem;
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 440px;
  overflow: hidden;
  transform: translateY(0);
  animation: cardEntrance 0.6s cubic-bezier(0.23, 1, 0.32, 1);

  .card-content {
    padding: 3rem 2.5rem;
  }
}

.header-section {
  text-align: center;
  margin-bottom: 2.5rem;

  .main-title {
    font-size: 2rem;
    color: #2d3748;
    margin-bottom: 0.75rem;
    font-weight: 700;
    letter-spacing: -0.025em;
  }

  .sub-title {
    color: #718096;
    font-size: 0.95rem;
  }
}

.login-form {
  ::v-deep {
    .el-form-item {
      margin-bottom: 1.75rem;

      &__label {
        color: #4a5568;
        font-weight: 500;
        padding-bottom: 0.5rem;
        font-size: 0.9rem;
      }

      .el-input__inner {
        height: 48px;
        border-radius: 0.75rem;
        border: 1px solid #e2e8f0;
        transition: all 0.3s ease;
        font-size: 1rem;
        padding-left: 2.75rem;

        &:hover {
          border-color: #c3dafe;
        }

        &:focus {
          border-color: #667eea;
          box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
        }
      }

      .el-input__prefix {
        left: 12px;
        font-size: 1.1rem;
        color: #a0aec0;
      }
    }
  }
}

.action-item {
  margin-top: 2rem;
}

.submit-btn {
  width: 100%;
  height: 50px;
  font-weight: 600;
  font-size: 1rem;
  border-radius: 0.75rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  letter-spacing: 0.5px;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  }

  &:active {
    transform: translateY(0);
  }
}

.footer-note {
  margin-top: 2.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid #edf2f7;
  text-align: center;
  font-size: 0.85rem;
  color: #718096;
  display: flex;
  justify-content: space-between;
}

@keyframes cardEntrance {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.98);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 1rem;
    background: #667eea;
  }

  .login-card {
    border-radius: 1rem;

    .card-content {
      padding: 2rem 1.5rem;
    }
  }

  .header-section {
    .main-title {
      font-size: 1.75rem;
    }
  }

  .submit-btn {
    height: 48px;
  }
}
</style>