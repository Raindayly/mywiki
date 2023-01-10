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
        <a-row :gutter="16">
          <a-col :span="8">
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
          </a-col>
          <a-col :span="16">
            <a-form :model="doc">
              <a-form-item>
                <a-input placeholder="请输入名称" v-model:value="doc.name"/>
              </a-form-item>
              <a-form-item>
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="selectTreeData"
                    placeholder="请选择父文档"
                    tree-default-expand-all
                    :replaceFields="{title:'name',key:'id',value:'id'}"
                >
                </a-tree-select>
              </a-form-item>
              <a-form-item>
                <a-input placeholder="请输入排序" v-model:value="doc.sort"/>
              </a-form-item>
              <a-form-item>
                <div style="border: 1px solid #ccc">
                  <Toolbar
                      style="border-bottom: 1px solid #ccc"
                      :editor="editorRef"
                      :defaultConfig="toolbarConfig"
                      :mode="mode"
                  />
                  <Editor
                      style="height: 500px; overflow-y: hidden;"
                      v-model="valueHtml"
                      :defaultConfig="editorConfig"
                      :mode="mode"
                      @onCreated="handleCreated"
                  />
                </div>
              </a-form-item>
              <a-row>
                <a-col :span="8"/>
                <a-col :span="8">
                  <a-affix :offset-bottom="top">
                    <a-button block type="primary" @click="handleDocSave">保存</a-button>
                  </a-affix>
                </a-col>
                <a-col :span="8"/>
              </a-row>
            </a-form>


          </a-col>
        </a-row>
      </a-space>
    </a-layout-content>
  </a-layout>
<!--  <a-modal-->
<!--      title="类别表单"-->
<!--      v-model:visible="modalVisible"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleDocSave"-->
<!--  >-->
<!--  </a-modal>-->
</template>

<script lang="ts">
import {defineComponent, onBeforeUnmount, onMounted, reactive, ref, shallowRef} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import { Editor, Toolbar, } from '@wangeditor/editor-for-vue'
import { createEditor } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css' // 引入 css

const columns = [
  {
    title: '名称',
    dataIndex: 'name',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    slots: {customRender: 'action'},
  },
];

export default defineComponent({
  name: "admin-book",
  components: { Editor, Toolbar },
  setup() {
    const top = ref<number>(10);
    const route = useRoute();
    const loading = ref(false)
    const listData = ref()
    const searchForm = ref({
      name: ''
    })

    /**
     * 富文本
     */
    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()
    // 内容 HTML
    const valueHtml = ref('')

    const toolbarConfig = {}
    const editorConfig = { placeholder: '请输入内容...' }

    const handleCreated = (editor: any) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }

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
    const doc = ref()
    doc.value = {}
    const selectTreeData = ref()
    selectTreeData.value = []
    const modalLoading = ref(false);
    const handleDocSave = () => {
      modalLoading.value = true;
      doc.value.content = editorRef.value.getHtml()
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {

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
      doc.value = Tool.copy(record);
      findContent()
      generateFatherTree(record.id)
    };

    /**
     * 通过id查询富文本content
     */
    const findContent = () => {
      //处理富文本bug
      axios.get("/doc/content/"+doc.value.id).then((res) => {
        const data = res.data
        if(data.success) {
          // editor = createEditor({
          //   // content 或 html
          //   // 其他属性
          // })
          const editor = editorRef.value
          if (editor == null) return
          editorRef.value.setHtml(data.content.content)
        }
      })
    }
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
      doc.value = {
        ebookId: route.query.ebookId
      }
      valueHtml.value = ''
      generateFatherTree()
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
      axios.get('/doc/ebook-docs/'+ route.query.ebookId).then((resp) => {
        loading.value = false
        const data = resp.data
        if (data.success) {
          listData.value = data.content
          level1.value = []
          level1.value = Tool.arrayToTree(data.content,'0')
          /**
           * 生成选择父文档选项
           */
          generateFatherTree()
        } else {
          message.error(data.message)
        }
      })
    }
    const handleTableChange = (pagination: any) => {
      handleQuery()
    }

    /**
     * 生成树结构
     */
    const generateFatherTree = (banId?:string) => {
      selectTreeData.value = Tool.copy(level1.value)?Tool.copy(level1.value):[]
      if(banId){
        banTreeNode(selectTreeData.value , banId)
      }
      selectTreeData.value.unshift({id: 0, name: '无'});
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

      /**
       * 电子书id默认值
       */
      doc.value.ebookId = route.query.ebookId
    })
    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })
    return {
      top,
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
      modalLoading,
      handleDocSave,

      //富文本
      editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,
    };
  },
});
</script>

<style scoped>

</style>