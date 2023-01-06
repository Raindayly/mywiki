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
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="selectTreeData"
            placeholder="Please select"
            tree-default-expand-all
            :replaceFields="{title:'name',key:'id',value:'id'}"
        >
        </a-tree-select>
<!--        <a-select-->
<!--            ref="select"-->
<!--            v-model:value="doc.parent"-->
<!--        >-->
<!--          <a-select-option-->
<!--              value="000"-->
<!--          >-->
<!--            无-->
<!--          </a-select-option>-->
<!--          <a-select-option-->
<!--            v-for="item in level1"-->
<!--            :key="item.id"-->
<!--            :value="item.id"-->
<!--            :disabled="doc.id === item.id"-->
<!--          >-->
<!--            {{ item.name }}-->
<!--          </a-select-option>-->
<!--        </a-select>-->
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";


const columns = [
  {
    title: '名称',
    dataIndex: 'name',
  },
  {
    title: '父文档',
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
    const route = useRoute();
    const loading = ref(false)
    const listData = ref()
    const searchForm = ref({
      name: ''
    })

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const doc = ref();
    const selectTreeData = ref()
    selectTreeData.value = []
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      // doc.value.doc1Id = docIds.value[0];
      // doc.value.doc2Id = docIds.value[1];
      axios.post("/doc/save", doc.value).then((response) => {
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
      doc.value = Tool.copy(record);
      selectTreeData.value = Tool.copy(level1.value)

      banTreeNode(selectTreeData.value , record.id)

      selectTreeData.value.unshift({id: 0, name: '无'});
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
      doc.value = {
        ebookId: route.query.ebookId
      }
      selectTreeData.value = Tool.copy(level1.value)

      selectTreeData.value.unshift({id: 0, name: '无'});
    }

    /**
     * 删除
     */
    const handleDel = (id: string) => {
      const delIds =  delTreeNode(level1.value , id)
      console.log(delIds)
      axios.delete('/doc/delete/'+ delIds.join(',')).then(res => {
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
      axios.get('/doc/all').then((resp) => {
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

    /**
     * 给树形选择添加disable，不能选自身级自身的子层级为父级
     */
    const banTreeNode = (source: any, id: string ) => {
      for (let i = 0; i < source.length; i++) {
        let node = source[i]
        if(node.id === id){
          //如果当前节点是要找的节点
          node.disabled = true

          const children = node.children
          if(Tool.isNotEmpty(children)){
            for (let j = 0; j < children.length; j++) {
              banTreeNode(children, children[j].id)
            }
          }
        }else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            banTreeNode(children, id)
          }
        }

      }
    }

    /**
     * 删除树节点本身及子节点，将逻辑放在前端，减少服务器压力
     * @param source 树节点数据
     * @param id 需要删除的树节点
     * @return delIds 删除的树节点id数组
     */
    const delTreeNode = (source: any, id: string ):string[] => {
      const delIds: string[] = []
      const recursion = (source: any,id: string) => {
        for (let i = 0; i < source.length; i++) {
          const node = source[i]
          const children = node.children
          if(node.id === id){
            //如果当前节点是要找的节点
            delIds.push(id)

            console.log(delIds)
            if(Tool.isNotEmpty(children)){
              for (let j = 0; j < children.length; j++) {
                recursion(children, children[j].id)
              }
            }
          }else {
            if(Tool.isNotEmpty(children)){
              recursion(children, id)
            }
          }
        }
        return delIds
      }
      return recursion(source,id)
    }

    onMounted(() => {
      handleQuery();
    })
    return {
      loading,
      // listData,
      selectTreeData,
      level1,

      columns,
      handleTableChange,

      searchForm,
      search,
      reset,

      edit,
      add,
      handleDel,

      doc,
      modalVisible,
      modalLoading,
      handleModalOk,
    };
  },
  components: {},
});
</script>

<style scoped>

</style>