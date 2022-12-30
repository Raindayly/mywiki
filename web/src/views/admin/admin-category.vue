<template>
  <a-layout style="padding: 24px 0; background: #fff">
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-space direction="vertical" style="width: 100%">
        <a-form
            layout="inline"
            :model="searchForm"
        >
          <a-form-item>
            <a-input v-model:value="searchForm.name" placeholder="搜索名称"></a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="search">搜索</a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="reset">重置</a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add">新增</a-button>
          </a-form-item>
        </a-form>
        <a-table :columns="columns"
                 :data-source="listData"
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
      title="电子书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
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
    dataIndex: 'cover',
    slots: {title: 'customTitle', customRender: 'cover'},
  },
  {
    title: '名称',
    dataIndex: 'name',
  },
  {
    title: '父分类',
    dataIndex: 'parent',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
  },
  {
    title: '操作',
    key: 'action',
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

    // const level1 =  ref();
    // let categorys: any;
    // /**
    //  * 查询所有分类
    //  **/
    // const handleQueryCategory = () => {
    //   loading.value = true;
    //   axios.get("/category/all").then((response) => {
    //     loading.value = false;
    //     const data = response.data;
    //     if (data.success) {
    //       categorys = data.content;
    //       console.log("原始数组：", categorys);
    //
    //       level1.value = [];
    //       level1.value = Tool.array2Tree(categorys, 0);
    //       console.log("树形结构：", level1.value);
    //
    //       // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
    //       handleQuery({
    //         page: 1,
    //         size: pagination.value.pageSize,
    //       });
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };

    const handleQuery = () => {
      loading.value = true
      axios.get('/category/list', {
        params: {
          name: searchForm.value.name
        }

      }).then((resp) => {
        loading.value = false
        const data = resp.data
        if (data.success) {
          listData.value = data.content
        } else {
          message.error(data.message)
        }
      })
    }
    const handleTableChange = (pagination: any) => {
      handleQuery()
    }
    onMounted(() => {
      // handleQueryCategory()
      handleQuery();
    })
    return {
      loading,
      listData,
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