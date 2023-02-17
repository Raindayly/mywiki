<template>
  <div>
    <a-space direction="vertical" style="width: 100%">
      <a-row :gutter="[12,0]">
        <a-col :span="12">
          <div class="dashboard">
            <a-spin size="large" v-if="!spin1" :delay="500"/>
            <template v-else>
              <a-space style="min-width: 300px" :size="size1">
                <a-statistic title="总文章数" :value="docSum" >
                  <template #prefix>
                    <LayoutOutlined />
                  </template>
                </a-statistic>
                <a-statistic title="总点赞数" :value="voteSum">
                  <template #prefix>
                    <like-outlined />
                  </template>
                </a-statistic>
                <a-statistic title="总阅读数" :value="viewSum">
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
              <template v-if="!todayData">
                暂无数据
              </template>
              <template v-else>
                <a-space :size="size2">
                  <a-statistic title="今日阅读数" :value="todayData.viewIncrease">
                    <template #prefix>
                      <LayoutOutlined />
                    </template>
                  </a-statistic>
                  <a-statistic  title="今日点赞数" :value="todayData.voteIncrease">
                    <template #prefix>
                      <like-outlined />
                    </template>
                  </a-statistic>
                  <a-statistic  title="预计今日阅读数" :value="expectViewCount">
                    <template #prefix>
                      <like-outlined />
                    </template>
                  </a-statistic>
                  <a-statistic  title="今日点赞率" suffix="%" :value-style="{ color: voteRate >= 100 ?'#3f8600':'#cf1322' }" :value="voteRate">
                    <template #prefix>
                      <arrow-up-outlined v-if="voteRate >= 100" />
                      <arrow-down-outlined v-else />
                    </template>
                  </a-statistic>
                </a-space>
              </template>
            </template>
          </div>
        </a-col>
      </a-row >
      <a-row>
        <Chart></Chart>
      </a-row>
    </a-space>
  </div>
</template>

<script setup lang="ts">
  import  Chart  from "@/components/TheWelcome/components/chart.vue"
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
  const size1 = 20
  //间距2
  const size2 = 20

  const todayData = ref<homeData>()
  const yesterdayData = ref<homeData>()

  //点赞率
  const voteRate = computed(()=>{
    let result = (todayData.value!.voteIncrease / todayData.value!.viewIncrease) * 100
    if(Number.isNaN(result) || !Number.isFinite(result)){
      return 0
    } else {
      return  Math.floor(result)
    }
  })

  //预计增长数
  const expectViewCount = computed(() => {
    let nowDate = new Date()
    let nowDateMS = nowDate.getHours() * 3600 * 1000 + nowDate.getMinutes() * 1000 + nowDate.getMilliseconds()
    let oneDayMS = 3600 * 24 * 1000
    return Math.floor((nowDateMS / oneDayMS) * yesterdayData.value!.viewIncrease)
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
          if(data.content.length === 1){
            [todayData.value] =  data.content
          } else if (data.content.length === 2){
            [yesterdayData.value ,todayData.value] =  data.content
          }
        }
        console.log(todayData.value,yesterdayData.value)
      })
    },1000)

  })
</script>

<style scoped>
  .dashboard {
    background-color: #d0e5f2;
    height: 220px;
    display: flex;
    justify-content: center;
    align-items: center;
    min-width: 300px;
  }
  .dashboard .ant-space {
    justify-content: space-around
  }
  /*
  css样式在scoped中属性选择器默认加载末尾，加上>>>会加在>>>之前或者使用:deep()
  */
  .ant-statistic:deep(.ant-statistic-title),.ant-statistic:deep(.ant-statistic-content) {
    display: flex !important;
    justify-content: center !important;
  }
</style>
