<template>
  <a-layout-content style="padding: 0 50px">
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-sider width="200" style="background: #fff">
        <a-menu
            mode="inline"
            v-model:selectedKeys="selectedKeys2"
            v-model:openKeys="openKeys"
            style="height: 100%"
        >
          <a-sub-menu key="sub1">
            <template #title>
                <span>
                  <user-outlined />
                  subnav 1
                </span>
            </template>
            <a-menu-item key="1">option1</a-menu-item>
            <a-menu-item key="2">option2</a-menu-item>
            <a-menu-item key="3">option3</a-menu-item>
            <a-menu-item key="4">option4</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub2">
            <template #title>
                <span>
                  <laptop-outlined />
                  subnav 2
                </span>
            </template>
            <a-menu-item key="5">option5</a-menu-item>
            <a-menu-item key="6">option6</a-menu-item>
            <a-menu-item key="7">option7</a-menu-item>
            <a-menu-item key="8">option8</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub3">
            <template #title>
                <span>
                  <notification-outlined />
                  subnav 3
                </span>
            </template>
            <a-menu-item key="9">option9</a-menu-item>
            <a-menu-item key="10">option10</a-menu-item>
            <a-menu-item key="11">option11</a-menu-item>
            <a-menu-item key="12">option12</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"  :data-source="listData">
          <template #renderItem="{ item }">
            <a-list-item key="item.title">
              <template #actions>
                <span v-for="{ type, text } in actions" :key="type">
                  <component v-bind:is="type" style="margin-right: 8px" />
                  <span v-if="type === 'StarOutlined'">
                    {{ item.docCount }}
                  </span>
                  <span v-else-if="type === 'LikeOutlined'">
                    {{ item.voteCount }}
                  </span>
                  <span v-else-if="type === 'MessageOutlined'">
                    {{ item.viewCount }}
                  </span>
                </span>
              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <a :href="item.href">{{ item.title }}</a>
                </template>
                <template #avatar><a-avatar :src="item.cover" /></template>
              </a-list-item-meta>
              {{ item.content }}
            </a-list-item>
          </template>
        </a-list>
      </a-layout-content>
    </a-layout>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, toRef} from 'vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import axios from 'axios'
const listData: Record<string, string>[] = reactive([]);


export default defineComponent({
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  name: 'Home',
  setup(){
    onMounted(() => {
      listData.length = 0
      axios.get('/ebook/list').then((resp) => {
        listData.push(...resp.data.content)
      })
    })

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 2,
    };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '151' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];
    return {
      listData,
      pagination,
      actions,
    };
  }
});
</script>
