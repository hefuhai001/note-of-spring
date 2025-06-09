<template>
  <div>
<!--    {{ useCounterStore }}-->

    {{ t }}
  </div>
</template>

<script setup lang="ts">
import {useCounterStore} from "@/stores/counter";
import {computed, onMounted, ref} from "vue"

computed(() => {

})

import request from "@/request";

const t = ref();
onMounted(() => {
  request.get("/test").then((res) => {
    t.value = res;
  })
})
</script>

<style scoped>

</style>