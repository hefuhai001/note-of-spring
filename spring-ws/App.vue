<template>
  <div></div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {}
  },
  mounted() {
    let ws = new WebSocket("ws://localhost:9090/myWs")
    ws.onopen = function () {
      console.log('open')
      ws.send("hello")
    }
    ws.onmessage = function (msg) {
      console.log(msg.data)
    }
    ws.onclose = function () {
      console.log('close')
    }
  },
}
</script>

<style scoped>

</style>