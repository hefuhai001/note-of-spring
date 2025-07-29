<template>
  <div>
    <h1>Flex 消息列表</h1>
    <input type="text" v-model="userId">
    <ul>
      <li v-for="(message, index) in messages" :key="index">{{ message }}</li>
    </ul>
    <button @click="startSse">开始接收消息</button>
    <button @click="sendMessage">发送消息</button>
    <button @click="logMessage">打印消息</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: '',
      messages: [],
      eventSource: null,
    };
  },
  methods: {
    startSse() {
      if (window.EventSource) {
        this.eventSource = new EventSource("http://localhost:8000/flex/connect/" + this.userId);
        this.eventSource.onmessage = (event) => {
          this.messages.push(event.data);
        };
        this.eventSource.onerror = (error) => {
          console.error("SSE 连接错误：", error);
          this.eventSource.close();
        };
      } else {
        alert("你的浏览器不支持 SSE");
      }
    },
    sendMessage() {
      fetch("http://localhost:8000/flex/send/" + this.userId, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      })
          .then(response => response.json())
          .then(data => {
            console.log("消息发送成功：", data);
          })
          .catch(error => {
            console.error("消息发送失败：", error);
          });
    },
    logMessage() {
      console.log(this.messages);
    }
  },
  beforeDestroy() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  },
};
</script>