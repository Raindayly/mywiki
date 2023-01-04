<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        v-model:selectedKeys="selectedKeys1"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">
          首页
        </router-link>
      </a-menu-item>
      <a-menu-item key="/adminbook">
        <router-link to="/adminbook">
          电子书管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/category">
        <router-link to="/category">
          类别管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          关于
        </router-link>
      </a-menu-item>

    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'TheHeader',
  props: {
    msg: String,
  },
});
</script>