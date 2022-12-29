<template>
  <a-table :columns="columns"
           :data-source="listData"
           :rowKey="record => record.id"
           :loading="loading"
           :pagination="pagination"
           @change="handleTableChange"
  >
    <template #cover="{ text }">
      <a>{{ text }}</a>
    </template>
    <template #customTitle>
      <span>
        封面
      </span>
    </template>
    <template #docCount="{ text }">
      <span>
        {{ text }}
      </span>
    </template>
    <template #viewCount="{ text }">
      <span>
        {{ text }}
      </span>
    </template>
    <template #voteCount="{ text }">
      <span>
        {{ text }}
      </span>
    </template>
    <template #tags="{ text: tags }">
      <span>
        <a-tag
            v-for="tag in tags"
            :key="tag"
            :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
        >
          {{ tag.toUpperCase() }}
        </a-tag>
      </span>
    </template>
    <template #action>
      <a-space>
        <a-button type="primary">编辑</a-button>
        <a-button type="primary" danger>删除</a-button>
      </a-space>
    </template>
  </a-table>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";


const columns = [
  {
    dataIndex: 'cover',
    slots: { title: 'customTitle', customRender: 'cover' },
  },
  {
    title: '名称',
    dataIndex: 'name',
  },
  {
    title: '分类1',
    dataIndex: 'category1Id',
  },
  {
    title: '分类2',
    dataIndex: 'category2Id',
  },
  {
    title: '文档数',
    dataIndex: 'docCount',
    slots: { customRender: 'docCount' },
  },
  {
    title: '点赞数',
    dataIndex: 'voteCount',
    slots: { customRender: 'voteCount' },
  },
  {
    title: '阅读数',
    dataIndex: 'viewCount',
    slots: { customRender: 'viewCount' },
  },
  {
    title: '操作',
    key: 'action',
    slots: { customRender: 'action' },
  },
];

export default defineComponent({
  name: "admin-book",
  setup() {
    const loading = ref(false)
    const listData = ref()
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    })

    const handleQuery = (params: any) => {
      loading.value = true
      axios.get('/ebook/list', {
        params
      }).then((resp) => {
        const data = resp.data
        loading.value = false
        listData.value = data.content.list

        //重置分页按钮
        pagination.value.current = params.page
        pagination.value.total = data.content.total
      })
    }
    const handleTableChange = (pagination :any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      })
    })
    return {
      loading,
      listData,
      columns,
      pagination,
      handleTableChange
    };
  },
  components: {
  },
});
</script>

<style scoped>

</style>