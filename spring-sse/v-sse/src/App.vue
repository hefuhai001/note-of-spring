<template>
  <div id="app">
    <ChatView/>
    <!--<TestView/>-->
    <!--<FlexView/>-->
    <!--<SseView/>-->
  </div>
</template>

<script>
import ChatView from './components/ChatView.vue'
import TestView from './components/TestView.vue'
import FlexView from './components/FlexView.vue'
import SseView from './components/SseView.vue'

export default {
  name: 'App',
  components: {
    ChatView,
    TestView,
    FlexView,
    SseView
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
