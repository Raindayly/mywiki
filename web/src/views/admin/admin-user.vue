<template>
  <a-layout style="padding: 24px 0; background: #fff">
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-space direction="vertical" style="width: 100%">
        <a-form
            layout="inline"
            :model="searchForm"
        >
          <a-form-item>
            <a-input v-model:value="searchForm.loginName" placeholder="登陆名搜索"></a-input>
          </a-form-item>
          <a-form-item>
            <a-input v-model:value="searchForm.nickName" placeholder="昵称搜索"></a-input>
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
          <template #roles="{ text, record }">
            <a-space direction="vertical">
              <a-tag color="#55acee" v-for="item in record.roles" :key="item.roleId">
                <template #icon>
                  <twitter-outlined />
                </template>
                {{ item.roleName }}
              </a-tag>
            </a-space>
          </template>
          <template #action="{ text, record }">
            <a-space>
              <a-button type="primary" @click="editRoles(record)">角色配置</a-button>
              <a-button type="primary" @click="resetPassword(record)">重置密码</a-button>
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
      title="新增/编辑"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 4 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登陆名">
        <a-input v-model:value="user.loginName" placeholder="请输入登陆名" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.nickName" placeholder="请输入昵称" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password" type="textarea" placeholder="请输入密码" />
      </a-form-item>
        <a-form-item label="选择角色">
          <a-select
              v-model:value="curRoles"
              mode="multiple"
              style="width: 100%"
              placeholder="Please select"
              option-label-prop="label"
              :options="allRoles"
          >
            <template #option="{value,label}">
              &nbsp;&nbsp;{{ label }}
            </template>
          </a-select>
        </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      title="重置密码"
      v-model:visible="resetPasswordModalVisible"
      :confirm-loading="resetModalLoading"
      @ok="handleResetModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password"/>
      </a-form-item>
    </a-form>
  </a-modal>
  <a-modal
      title="角色配置"
      v-model:visible="editRoleModal"
      :confirm-loading="editRoleModalLoading"
      @ok="roleSettingOk"
  >
    <a-form :model="curRoles" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="选择角色">
        <a-select
            v-model:value="curRoles"
            mode="multiple"
            style="width: 100%"
            placeholder="Please select"
            option-label-prop="label"
            :options="allRoles"
        >
          <template #option="{value,label}">
            &nbsp;&nbsp;{{ label }}
          </template>
        </a-select>
      </a-form-item>
    </a-form>
 </a-modal>

</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";

declare let hexMd5: any;
declare let KEY: any;

const columns = [
  {
    title: '登陆名',
    dataIndex: 'loginName',
  },
  {
    title: '昵称',
    dataIndex: 'nickName',
    slots: {customRender: 'nickName'}
  },
  {
    title: '密码',
    dataIndex: 'password',
    slots: {customRender: 'password'},
  },
  {
    title: '角色',
    slots: {customRender: 'roles'},
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
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    })

    const searchForm = ref({
      loginName: '',
      nickName: ''
    })

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      let params = Tool.copy(user.value)
      params.roles = curRoles.value.join(",")
      axios.post("/user/save", params).then((response) => {
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
     * 重置密码
     */
    const resetPasswordModalVisible = ref(false)
    const resetModalLoading = ref(false);
    const handleResetModalOk = () => {
      resetModalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      axios.post("/user/reset-password", user.value).then((response) => {
        resetModalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          resetPasswordModalVisible.value = false;

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
     * 重置密码
     */
    const resetPassword = (record: any) => {
      resetPasswordModalVisible.value = true;
      user.value = Tool.copy(record);
      user.value.password = null;
    };
    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
      console.log(record)
      curRoles.value = record.roles.map((item: any) => item.roleId)
    };

    /**
     * 搜索
     */
    const search = () => {
      handleQuery({
        nickName: searchForm.value.nickName,
        loginName: searchForm.value.loginName,
        page: 1,
        size: pagination.value.pageSize
      })
    }

    /**
     * 重置
     */
    const reset = () => {
      searchForm.value.nickName = ''
      searchForm.value.loginName = ''
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
      user.value = {}

    }

    /**
     * 删除
     */
    const handleDel = (id: string) => {
      axios.delete(`/user/delete/${id}`).then(res => {
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
    /**
     *
     * 角色配置
     */
    const editRoleModal = ref(false)
    const editRoleModalLoading = ref(false)
    const allRoles = ref()
    const curRoles = ref()
    const editRoles = (userinfo: any)=> {
      user.value = userinfo
      console.log(user.value)
      editRoleModal.value = true
      curRoles.value = userinfo.roles.map((item: any) => item.roleId)
    }

    const level1 = ref();
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
      axios.get('/user/list', {
        params: {
          nickName: searchForm.value.nickName,
          loginName: searchForm.value.loginName,
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
    const roleSettingOk = () => {
      // user.value.roles = curRoles.value.join(",")
      let params = Tool.copy(user.value)
      params.roles = curRoles.value.join(",")
     axios.post('/user/save',params).then(res => {
      let data = res.data
       if(data.success) {
         message.success('保存成功')
         editRoleModal.value = false
         curRoles.value = []
         handleQueryCategory()
       }else {
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
     const handleQueryAllRoles = ()=> {
       axios.get(`/user/allRoles`).then(res => {
         let data = res.data
         if(data.success) {
           allRoles.value = data.content.map((_:any) => {
             return {
               value:_.roleId,
               label:_.roleName
             }
           })
         }
       })
     }

    onMounted(() => {
      handleQueryCategory()
      handleQueryAllRoles()
    })
    return {
      loading,
      listData,
      columns,
      pagination,
      handleTableChange,
      handleQueryCategory,
      getCategoryName,

      searchForm,
      search,
      reset,

      edit,
      add,
      handleDel,

      user,
      modalVisible,
      modalLoading,
      handleModalOk,
      level1,

      //重置密码
      resetModalLoading,
      resetPasswordModalVisible,
      handleResetModalOk,
      resetPassword,

      //角色配置
      editRoles,
      allRoles,
      curRoles,
      editRoleModal,
      editRoleModalLoading,


      roleSettingOk
    };
  },
  components: {},
});
</script>

<style scoped>

</style>