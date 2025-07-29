<template>
  <div>
    <h1>Chat 消息列表</h1>
    <input type="text" v-model="userId">
    <input type="text" v-model="text">
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
      text: '',
      messages: [],
      eventSource: null,
    };
  },
  methods: {
    startSse() {
      if (window.EventSource) {
        this.eventSource = new EventSource("http://localhost:8000/chat/connect/" + this.userId);
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
      // 构造完整的 URL，包含查询参数
      const url = new URL(`http://localhost:8000/chat/send/${this.userId}`);
      url.searchParams.append("message", this.text);
      url.searchParams.append("provider", "wen");

      fetch(url.href, {
        method: "GET",
      })
          .then(response => response.text())
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