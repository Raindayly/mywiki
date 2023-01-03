<template>
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            mode="inline"
            v-model:openKeys="openKeys"
            style="height: 100%"
            @click="handleMenuClick"
        >
          <a-menu-item key="welcome">
            欢迎
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template #title>
              <span>
                {{ item.name }}
              </span>
            </template>
            <a-menu-item v-for="itemChild in item.children" :key="itemChild.id">
              {{ itemChild.name }}
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <div class="welcome" v-show="isWelcome">
          <the-welcome></the-welcome>
        </div>
        <a-list v-show="!isWelcome" :loading="loading" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"  :data-source="books">
        <template #renderItem="{ item }">
          <a-list-item :key="item.title">
            <template #actions>
                <span>
                  <component v-bind:is="'FileOutlined'" style="margin-right: 8px" />
                  {{ item.docCount }}
                </span>
                <span>
                  <component v-bind:is="'UserOutlined'" style="margin-right: 8px" />
                  {{ item.viewCount }}
                </span>
                <span>
                  <component v-bind:is="'LikeOutlined'" style="margin-right: 8px" />
                  {{ item.voteCount }}
                </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
            {{ item.content }}
          </a-list-item>
        </template>
      </a-list>
      </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import axios from 'axios'
import {message} from "ant-design-vue";
import { Tool } from '@/util/tool';
import  TheWelcome from '@/components/TheWelcome.vue'


export default defineComponent({
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    TheWelcome
  },
  name: 'Home',
  setup(){

    const books = ref([]);
    const loading = ref(false)
    const pagination = ref({
      current: 1,
      pageSize: 1000
    });



    const openKeys =  ref();
    const level1 =  ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          // 加载完分类后，将侧边栏全部展开
          openKeys.value = [];
          for (let i = 0; i < categorys.length; i++) {
            openKeys.value.push(categorys[i].id)
          }

          level1.value = [];
          level1.value = Tool.arrayToTree(categorys, 0);
          console.log("树形结构：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };

    const isWelcome = ref(true)

    let category2Id = ''
    const queryBooks = () => {
      loading.value = true
      axios.get("/ebook/list",{
        params: {
          page: 1,
          size: 1000,
          category2Id: category2Id
        }
      }).then((res) => {
        loading.value = false
        const data = res.data
        if(data.success){
          books.value = data.content.list
        }
      })
    }

    /**
     * 点击菜单
     */
    const handleMenuClick = (value: any) => {
      if(value.key === 'welcome') {
        isWelcome.value = true
      }else {
        isWelcome.value = false
        category2Id = value.key
        queryBooks()
      }
    }


    onMounted(() => {
      handleQueryCategory()
    })

    return {
      books,
      pagination,
      loading,
      isWelcome,

      handleMenuClick,

      openKeys,
      level1
    };


  }
});
</script>

<style scoped>
  .ant-avatar {
    border-radius: 10%;
    width: 100px;
    height: 100px;
    line-height: 100px;
  }
</style>
