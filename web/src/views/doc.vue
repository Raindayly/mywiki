<template>
  <a-layout style="padding: 24px 0; background: #fff">
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-space direction="vertical" style="width: 100%">
        <a-row :gutter="16" v-if="level1.length === 0">
          没有相关文档
        </a-row>
        <a-row :gutter="16" v-else>
          <a-col :span="4">
            <a-tree
                class="draggable-tree"
                v-model:expandedKeys="expandedKeys"
                draggable
                @select="onSelect"
                :tree-data="level1"
                :replaceFields="{title:'name',key:'id',value: 'id'}"
            />
          </a-col>
          <a-col :span="20">
            <div>文章标题:{{doc.name}}</div>
            <span>点赞数：{{doc.voteCount}}</span><span>阅读数：{{doc.viewCount}}</span>
            <a-divider />
            <div class="wangeditor" :innerHTML="html"></div>
            <div class="center mgt">
              <a-button style="margin: auto" type="primary" shape="round" size="large" @click="handleVote">点赞</a-button>
            </div>
          </a-col>
        </a-row>
      </a-space>
    </a-layout-content>
  </a-layout>
</template>
<script lang="ts">
import {defineComponent, onBeforeUnmount, onMounted, reactive, ref, shallowRef} from 'vue';
import axios from "axios";
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import { Editor, Toolbar, } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import '../assets/css/common.css'
import { TreeDataItem, TreeDragEvent, DropEvent } from 'ant-design-vue/es/tree/Tree';
import "prismjs";
// 引用css
import "prismjs/themes/prism-okaidia.css";
//prismjs在window对象下有Prism属性
// 使用any类型骗过ts检测
const Prism = (window as any).Prism;

export default defineComponent({
  name: "admin-book",
  components: { Editor, Toolbar },
  setup() {
    const route = useRoute();
    const loading = ref(false)
    const listData = ref()
    /**
     * 富文本
     */
    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()
    // 内容 HTML
    const valueHtml = ref('')
    // 显示html
    const html = ref('')
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

    const doc = ref()
    doc.value = {}

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
          if (Tool.isNotEmpty(level1)) {
            defaultSelectedKeys.value = [level1.value[0].id];
            handleContent(level1.value[0].id);
            // 初始显示文档信息
            doc.value = level1.value[0];
          }
        } else {
          message.error(data.message)
        }
      })
    }
    const handleContent = (docId:any) => {
      axios.get('/doc/content/'+ docId).then((resp) => {
        const data = resp.data
        if (data.success) {
          html.value = data.content.content
          setTimeout(()=>{
            Prism.highlightAll()
          })
        }
      })
    }
    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)) {
        // 选中某一节点时，加载该节点的文档信息
        doc.value = info.selectedNodes[0].props;
        // 加载内容
        handleContent(selectedKeys[0]);
      }
    };

    /**
     * 点赞
     */
    const handleVote = () => {
      axios.get('/doc/vote/'+ doc.value.id).then((resp) => {
        const data = resp.data
        if (data.success) {
          doc.value.voteCount++;
        } else {
          message.error(data.message);
        }
      })
    }


    /**
     * 树形控件
     */
    const expandedKeys = ref<string[]>([]);
    const gData = ref<TreeDataItem[]>([]);
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

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
      loading,
      level1,
      doc,

      //富文本
      editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,
      html,


      //树节点
      expandedKeys,
      gData,
      onSelect,

      //点赞
      handleVote
    }
  }
});
</script>

<style>
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  /*background-color: #f1f1f1;*/
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor blockquote p {
  font-family:"YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight:600;
}

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.wangeditor img {
  max-width: 100%;
  height: auto;
}

/* 视频自适应 */
.wangeditor iframe {
  width: 100%;
  height: 400px;
}
.mgt {
  margin-top: 80px;
}
</style>