<template>
  <el-container class="main-layout">
    <!-- 侧边栏 -->
    <el-aside
        :width="asideWidth"
        class="app-aside"
        :class="{ 'is-collapsed': isCollapse }"
    >
      <Aside :isCollapse="isCollapse" />
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="app-header">
        <Header
            @toggle-collapse="toggleCollapse"
            :collapse-icon="collapseIcon"
        />
      </el-header>

      <!-- 页面内容 -->
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { defineComponent } from 'vue'
import Aside from './Aside.vue'
import Header from './Header.vue'

export default defineComponent({
  name: 'LayoutIndex',
  data() {
    return {
      isCollapse: false,
      asideWidth: '240px',
      collapseIcon: 'el-icon-s-fold'
    }
  },
  methods: {
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '240px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'

      setTimeout(() => {
        window.dispatchEvent(new Event('resize'))
      }, 500)
    }
  }
})
</script>

<style lang="scss" scoped>
.main-layout {
  height: 100vh;
  background: #f5f6fa;
}

.app-aside {
  background: #ffffff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  &.is-collapsed {
    ::v-deep .el-menu--collapse {
      width: 64px;
    }
  }
}

.app-header {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  z-index: 9;
}

.app-main {
  padding: 20px;
  background-color: #f5f6fa;
  min-height: calc(100vh - 64px);

  @media (max-width: 768px) {
    padding: 16px;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>