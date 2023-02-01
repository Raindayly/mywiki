<template>
  <div >
    <a-row :gutter="[24,48]">
      <a-col :span="12">
        <div class="dashboard">
          <a-statistic title="总文章数" :value="docSum" style="margin-right: 50px">
            <template #prefix>
              <LayoutOutlined />
            </template>
          </a-statistic>
          <a-statistic title="总点赞数" :value="voteSum" style="margin-right: 50px">
            <template #prefix>
              <like-outlined />
            </template>
          </a-statistic>
          <a-statistic title="总用户数" :value="viewSum" style="margin-right: 50px">
            <template #prefix>
              <MehOutlined />
            </template>
          </a-statistic>
        </div>
      </a-col>
      <a-col :span="12">
        <div class="dashboard">
          123
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
  import {onMounted, ref} from "vue";
  import axios from "axios";

  const voteSum = ref(0)
  const viewSum = ref(0)
  const docSum = ref(0)

  onMounted(() => {
    axios.get("/statistics/sum").then((res) => {
      const data = res.data
      if(data.success) {
        ({ viewSum:viewSum.value,voteSum:voteSum.value,docSum:docSum.value } = data.content)
      }
    })
  })
</script>

<style scoped>
  .dashboard {
    background-color: #d0e5f2;
    height: 220px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
