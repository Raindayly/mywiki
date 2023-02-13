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
                 :pagination="pagination"
                 @change="handleTableChange"
        >
          <template #role="{ record }">
            <span>
              {{ record.roleName }}
            </span>
          </template>
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
      title="角色"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="role" :label-col="{ span: 4 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="角色名">
        <a-input v-model:value="role.roleName" placeholder="请输入名称"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";


const columns = [
  {
    title: '角色',
    dataIndex: 'role',
    slots: {customRender: 'role'}
  },
  {
    title: '操作',
    key: 'action',
    slots: {customRender: 'action'},
  },
];

export default defineComponent({
  name: "admin-role",
  setup() {
    const loading = ref(false)
    const listData = ref()
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    })

    const searchForm = ref({
      name: ''
    })

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();
    const role = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      role.value.category1Id = categoryIds.value[0];
      role.value.category2Id = categoryIds.value[1];
      axios.post("/role/save", role.value).then((response) => {
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
      }).catch(() => {
        modalLoading.value = false;
      })
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      role.value = Tool.copy(record);
      categoryIds.value = [role.value.category1Id, role.value.category2Id]
    };

    /**
     * 搜索
     */
    const search = () => {
      handleQuery({
        name: searchForm.value.name,
        page: 1,
        size: pagination.value.pageSize
      })
    }

    /**
     * 重置
     */
    const reset = () => {
      searchForm.value.name = ''
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      })
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      role.value = {}
      categoryIds.value = []

    }

    /**
     * 删除
     */
    const handleDel = (id: string) => {
      axios.delete(`/role/delete/${id}`).then(res => {
        const data = res.data
        if (data.success) {
          message.success("删除成功!")

          /**
           * 刷新页面
           */
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          })
        } else {
          message.error("删除失败!")
        }
      }).catch(err => {
        message.error(err)
      })
    }

    let categorys: any;


    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    const handleQuery = (params: any) => {
      loading.value = true
      axios.get('/role/list', {
        params: {
          roleName: searchForm.value.name,
          page: params.page,
          size: params.size
        }

      }).then((resp) => {
        loading.value = false
        const data = resp.data
        if (data.success) {
          listData.value = data.content.list

          //重置分页按钮
          pagination.value.current = params.page
          pagination.value.total = data.content.total
        } else {
          message.error(data.message)
        }
      })
    }
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
      // handleQueryCategory()
    })
    return {
      loading,
      listData,
      columns,
      pagination,
      handleTableChange,
      searchForm,
      search,
      reset,

      edit,
      add,
      handleDel,

      role,
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