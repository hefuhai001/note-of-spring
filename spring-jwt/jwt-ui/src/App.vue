<template>
  <div class="main">
    <button @click="handleLogin">登录</button>
    <button @click="handleTest">测试</button>
  </div>
</template>

<script setup lang="ts">
import {login, test} from "@/api";

function handleLogin() {
  login("1", "1").then((res: any) => {
    alert(JSON.stringify(res.data))
    localStorage.setItem("token", res.data);
    setTimeout(() => {
      location.replace(location.href);
    }, 500);
  })
}

function handleTest() {
  test().then((res: any) => {
    alert(JSON.stringify(res));
  })
}

</script>

<style scoped>

</style>
