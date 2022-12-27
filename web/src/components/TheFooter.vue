<template>
  <a-layout-footer style="text-align: center">
    YBook ©2022 Created by yaoliyan
  </a-layout-footer>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'TheFooter',
  props: {
    msg: String,
  },
});
</script>
<style scoped>

</style>