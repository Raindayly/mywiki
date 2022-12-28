import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//引入ant design
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import axios from 'axios';

console.log(process.env.NODE_ENV)
console.log(process.env.VUE_APP_SERVER)
axios.defaults.baseURL = process.env.VUE_APP_SERVER

createApp(App).use(store).use(router).use(Antd).mount('#app')
