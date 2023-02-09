<template>
  <div >
    <a-row :gutter="[24,48]">
      <a-col :span="12">
        <div class="dashboard">
          <a-spin size="large" v-if="!spin1" :delay="500"/>
          <template v-else>
            <a-space :size="size1">
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
              <a-statistic title="总阅读数" :value="viewSum" style="margin-right: 50px">
                <template #prefix>
                  <MehOutlined />
                </template>
              </a-statistic>
            </a-space>
          </template>

        </div>
      </a-col>
      <a-col :span="12">
        <div  class="dashboard">
          <a-spin size="large" v-if="!spin2" :delay="500"/>
          <template v-else >
            <a-space :size="size1">
              <a-statistic title="今日阅读数" :value="todayData.viewCount" style="margin-right: 50px">
                <template #prefix>
                  <LayoutOutlined />
                </template>
              </a-statistic>
              <a-statistic  title="今日点赞数" :value="todayData.voteCount" style="margin-right: 50px">
                <template #prefix>
                  <like-outlined />
                </template>
              </a-statistic>
              <a-statistic  title="点赞率" suffix="%" :value-style="{ color: voteRate >= 100 ?'#3f8600':'#cf1322' }" :value="voteRate" style="margin-right: 50px">
                <template #prefix>
                  <arrow-up-outlined v-if="voteRate >= 100" />
                  <arrow-down-outlined v-else />
                </template>
              </a-statistic>
            </a-space>
          </template>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
  import axios from "axios";
  import {homeData} from "@/components/TheWelcome/entity/homeData";

  const voteSum = ref(0)
  const viewSum = ref(0)
  const docSum = ref(0)

  //加载
  const spin1 = ref(false)
  const spin2 = ref(false)
  const spin3 = ref(false)

  //间距1
  const size1 = 50

  const todayData = ref(new homeData())
  const yesterdayData = ref(new homeData())

  //点赞率
  const voteRate = computed(()=>{
    console.log(1/0)
    let result = (todayData.value.voteCount / todayData.value.viewCount) * 100
    if(Number.isNaN(result) || !Number.isFinite(result)){
      return 0
    } else {
      return  result
    }
  })

  onMounted(() => {
    setTimeout(()=>{
      axios.get("/statistics/sum").then((res) => {
        const data = res.data
        if(data.success) {
          spin1.value = true;
          //#赋值型对象的解构赋值，行首出现的{要在之前加上(防止js将其解释成为代码块
          ({ viewSum:viewSum.value,voteSum:voteSum.value,docSum:docSum.value } = data.content);
        }
      })

      axios.get("/statistics/homeStatistics").then(res => {
        const { data } = res
        if(data.success) {
          spin2.value = true;
          [yesterdayData.value ,todayData.value] =  data.content
        }
        console.log(todayData.value,yesterdayData.value)
      })
    },2000)

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
