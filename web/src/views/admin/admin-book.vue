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
    <template #action="{ text, record }">
      <a-space>
        <a-button type="primary" @click="edit(record)">编辑</a-button>
        <a-button type="primary" danger>删除</a-button>
      </a-space>
    </template>
  </a-table>

  <a-modal
      title="电子书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue';
import {Tool} from "@/util/tool";


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

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();
    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };

    const level1 =  ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

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
      handleQueryCategory()
    })
    return {
      loading,
      listData,
      columns,
      pagination,
      handleTableChange,

      edit,

      ebook,
      modalVisible,
      modalLoading,
      handleModalOk,
      categoryIds,
      level1,
    };
  },
  components: {
  },
});
</script>

<style scoped>

</style>