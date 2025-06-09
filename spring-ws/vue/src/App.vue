<!--<template>-->
<!--  <div id="app">-->
<!--    <nav>-->
<!--      <router-link to="/">Home</router-link> |-->
<!--      <router-link to="/about">About</router-link>-->
<!--    </nav>-->
<!--    <router-view/>-->
<!--  </div>-->
<!--</template>-->

<!--<style>-->
<!--#app {-->
<!--  font-family: Avenir, Helvetica, Arial, sans-serif;-->
<!--  -webkit-font-smoothing: antialiased;-->
<!--  -moz-osx-font-smoothing: grayscale;-->
<!--  text-align: center;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--nav {-->
<!--  padding: 30px;-->
<!--}-->

<!--nav a {-->
<!--  font-weight: bold;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--nav a.router-link-exact-active {-->
<!--  color: #42b983;-->
<!--}-->
<!--</style>-->

<template>
  <div></div>
</template>

<script>
export default {
  name: 'HelloWorld',
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