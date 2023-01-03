<template>
  <a-layout style="padding: 24px 0; background: #fff">
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-space direction="vertical" style="width: 100%">
        <a-form
            layout="inline"
            :model="searchForm"
        >
          <a-form-item>
            <a-button type="primary" @click="add">新增</a-button>
          </a-form-item>
        </a-form>
        <a-table :columns="columns"
                 :data-source="level1"
                 :rowKey="record => record.id"
                 :loading="loading"
                 :pagination="false"
                 @change="handleTableChange"
        >
          <template #action="{ text, record }">
            <a-space>
              <a-button type="primary" @click="edit(record)">编辑</a-button>
              <a-popconfirm
                  title="请确定是否需要删除"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDel(record.id)"
              >
                <a-button type="primary" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="类别表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value="category.parent"
        >
          <a-select-option
              value="000"
          >
            无
          </a-select-option>
          <a-select-option
            v-for="item in level1"
            :key="item.id"
            :value="item.id"
            :disabled="category.id === item.id"
          >
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";


const columns = [
  {
    title: '名称',
    dataIndex: 'name',
  },
  {
    title: '父分类',
    dataIndex: 'parent',
    width: '10%',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
    width: '10%',
  },
  {
    title: '操作',
    key: 'action',
    width: '20%',
    slots: {customRender: 'action'},
  },
];

export default defineComponent({
  name: "admin-book",
  setup() {
    const loading = ref(false)
    const listData = ref()
    const searchForm = ref({
      name: ''
    })

    /**
     * 一级分类树，children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级分类树，children属性就是二级分类
    level1.value = [];

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();
    const category = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      // category.value.category1Id = categoryIds.value[0];
      // category.value.category2Id = categoryIds.value[1];
      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      }).catch(() => {
        modalLoading.value = false;
      })
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
      categoryIds.value = [category.value.category1Id, category.value.category2Id]
    };

    /**
     * 搜索
     */
    const search = () => {
      handleQuery()
    }

    /**
     * 重置
     */
    const reset = () => {
      searchForm.value.name = ''
      handleQuery()
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {}
    }

    /**
     * 删除
     */
    const handleDel = (id: string) => {
      axios.delete(`/category/delete/${id}`).then(res => {
        const data = res.data
        if (data.success) {
          message.success("删除成功!")

          /**
           * 刷新页面
           */
          handleQuery()
        } else {
          message.error("删除失败!")
        }
      }).catch(err => {
        message.error(err)
      })
    }


    const handleQuery = () => {
      loading.value = true
      axios.get('/category/all').then((resp) => {
        loading.value = false
        const data = resp.data
        if (data.success) {
          listData.value = data.content
          level1.value = []
          level1.value = Tool.arrayToTree(data.content,'0')
        } else {
          message.error(data.message)
        }
      })
    }
    const handleTableChange = (pagination: any) => {
      handleQuery()
    }
    onMounted(() => {
      handleQuery();
    })
    return {
      loading,
      // listData,
      level1,

      columns,
      handleTableChange,

      searchForm,
      search,
      reset,

      edit,
      add,
      handleDel,

      category,
      modalVisible,
      modalLoading,
      handleModalOk,
      categoryIds,
    };
  },
  components: {},
});
</script>

<style scoped>

</style>