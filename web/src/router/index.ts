import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/home.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import About from '@/views/about.vue'
import Category from '@/views/admin/admin-category.vue'
import Doc from '@/views/doc.vue'
import AdminDoc from "@/views/admin/admin-doc.vue";
import adminUser from "@/views/admin/admin-user.vue";
import store from "@/store";
import {Tool} from "@/util/tool";
import AdminRole from "@/views/admin/admin-role.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/doc',
    name: 'Doc',
    component: Doc
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/about.vue')
    component: About
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'Category',
    component: Category,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: adminUser,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: AdminDoc,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/role',
    name: 'AdminRole',
    component: AdminRole,
    meta: {
      loginRequire: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})


// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  console.log(to.matched)
  //这段代码的意思是访问的路由中有loginRequire这个属性的返回true
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
