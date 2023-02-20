<template>
  <div id="main" style="width:100%;height:400px;background-color: #d0e5f2;padding: 20px"></div>
</template>

<script setup lang="ts">
import {CanvasRenderer} from "echarts/renderers";
import * as echarts from 'echarts/core';
import { BarChart } from 'echarts/charts';
import { LineChart } from 'echarts/charts'
import { LabelLayout, UniversalTransition } from 'echarts/features';
import dayjs from 'dayjs'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  VisualMapComponent,

} from 'echarts/components';
import {onMounted, ref} from "vue";
import {homeData} from "@/components/TheWelcome/entity/homeData";
import axios from "axios";
import store from "@/store";

// 注册必须的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  BarChart,
  LabelLayout,
  UniversalTransition,
  CanvasRenderer,
  VisualMapComponent,
  LineChart
]);

const chartData = ref<homeData[]>()
const xAxisData = ref<string[]>()
const seriesData = ref<number[]>()

const genSeriesData = (chartData:homeData[]):(string|number)[][] => {
  let arr: (string|number)[][] = []
  chartData.forEach(item => {
    arr.push([dayjs(item.date).format("YYYY-MM-DD") ,item.viewIncrease])
  })
  return arr
}



type ApiContent<T> = {
  success: boolean,
  content: T,
  message: string
}

onMounted(() => {
  let el: any = document.getElementById('main')
  let myChart = echarts.init(el);

  axios.get<ApiContent<homeData[]>>(`/statistics/chart/${store.state.user.id}`).then(res => {
    let data = res.data
    if(data.success){
      chartData.value = res.data.content
      let seriesData = genSeriesData(chartData.value)
      myChart.setOption({
        title: {
          text:'今日阅读数统计'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '30%']
        },
        // visualMap: {
        //   type: 'piecewise',
        //   show: false,
        //   dimension: 0,
        //   seriesIndex: 0,
        //   pieces: [
        //     {
        //       gt: 1,
        //       lt: 3,
        //       color: 'rgba(0, 0, 180, 0.4)'
        //     },
        //     {
        //       gt: 5,
        //       lt: 7,
        //       color: 'rgba(0, 0, 180, 0.4)'
        //     }
        //   ]
        // },
        series: [
          {
            type: 'line',
            smooth: 0.6,
            symbol: 'none',
            lineStyle: {
              color: '#5470C6',
              width: 5
            },
            markLine: {
              symbol: ['none', 'none'],
              label: { show: false },
              data: [{ xAxis: 1 }, { xAxis: 3 }, { xAxis: 5 }, { xAxis: 7 }]
            },
            areaStyle: {},
            data: seriesData
          }
        ]
      });

    }
  })

// 绘制图表
})

</script>

<style scoped>

</style>