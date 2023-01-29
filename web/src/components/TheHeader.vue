<template>
  <a-layout-header class="header">
    <a-row>
      <a-col :span="2">
        <div class="logo" />
      </a-col>
      <a-col :span="8">
        <a-menu
            theme="dark"
            mode="horizontal"
            :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="/">
            <router-link to="/">
              首页
            </router-link>
          </a-menu-item>
          <a-menu-item key="/admin/ebook">
            <router-link to="/admin/ebook">
              电子书管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/admin/category">
            <router-link to="/admin/category">
              类别管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/admin/user">
            <router-link to="/admin/user">
              用户管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/about">
            <router-link to="/about">
              关于
            </router-link>
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="14">
        <a-popconfirm
            title="确认退出登录?"
            ok-text="是"
            cancel-text="否"
        >
          <a class="login-menu" >
            <span>退出登录</span>
          </a>
        </a-popconfirm>
        <a class="login-menu" >
          <span>您好：</span>
        </a>
        <a class="login-menu" @click="showLoginModal">
          <span>登录</span>
        </a>
      </a-col>
    </a-row>
    <a-modal
        title="登录"
        v-model:visible="isLoginModal"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import axios from "axios";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'TheHeader',
  props: {
    msg: String,
  },
  setup(){
    const isLoginModal = ref(false)
    const loginModalLoading = ref(false)
    const loginUser = ref()
    loginUser.value = {
      loginName: '',
      password: ''
    }

    const showLoginModal = () => {
      isLoginModal.value = true
    }

    const login = () => {
      let hexMd5Password = hexMd5(loginUser.value.password + KEY)
      axios.post('/user/login',{
        loginName: loginUser.value.loginName,
        password: hexMd5Password
      }).then(res => {
        console.log(res)
      })
    }

    return {
      isLoginModal,
      showLoginModal,
      loginModalLoading,
      login,
      loginUser

    }
  }
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>