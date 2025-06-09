<template>
  <div>
    {{ t }}
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue"
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