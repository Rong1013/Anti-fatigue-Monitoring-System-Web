<template>
  <div class="dashboard-container">
    <div class="dashboard-text">欢迎使用抗疲劳制造数字化监测平台</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 0;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-image: url('~@/assets/dashboard-monitor.png');
    background-size: cover;
    background-position: center;
  }
  &-text {
    font-size: 64px;
    font-family: 'Impact', '微软雅黑', sans-serif; /* 自定义字体 */
    font-weight: bold;
    letter-spacing: 12px;
    transform: translateY(-100px);
    background-image: linear-gradient(45deg, #f0f0f0 10%, #ffffff 90%); /* 更鲜艳的渐变颜色 */
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent; /* 使文字颜色透明，显示背景渐变 */
    text-shadow: 0 6px 12px rgba(0,0,0,0.6), 0 12px 24px rgba(0,0,0,0.5); /* 增强文字阴影 */
  }
}
</style>
