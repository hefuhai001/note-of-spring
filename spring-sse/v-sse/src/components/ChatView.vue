<template>
  <div class="chat-container">
    <div class="sidebar">
      <h2>会话名称</h2>
      <ul>
        <li v-for="session in sessions" :key="session.id">
          <div @click="selectSession(session.id)" :class="{'active': currentSessionId === session.id}">
            {{ session.name }}
          </div>
        </li>
      </ul>
    </div>
    <div class="chat-box" v-if="currentSession">
      <div class="chat-header">
        <h3>{{ currentSession.name }}</h3>
      </div>
      <div class="messages">
        <div v-for="(message, index) in messages" :key="index" class="message"
             :class="{'self': message.self, 'other': !message.self}">
          <div class="message-content">{{ message.text }}</div>
        </div>
      </div>
      <div class="input-area">
        <input type="text" v-model="text" placeholder="输入消息..." @keyup.enter="sendMessage"/>
        <button @click="sendMessage">发送消息</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: 'user1',
      sessions: [
        {id: '1', name: '会话1'},
        {id: '2', name: '会话2'}
      ],
      currentSessionId: '',
      currentSession: null,
      text: '',
      messages: [],
      eventSource: null,
    };
  },
  methods: {
    selectSession(sessionId) {
      this.currentSessionId = sessionId;
      this.currentSession = this.sessions.find(s => s.id === sessionId);
      this.messages = [];
      this.startSse();
    },
    startSse() {
      if (window.EventSource && this.currentSessionId) {
        const url = `http://localhost:8000/chat/connect/${this.currentSessionId}`;
        this.eventSource = new EventSource(url);
        this.eventSource.onmessage = (event) => {
          const message = {text: event.data, self: false};
          this.messages.push(message);
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
      if (this.text.trim() === '') return;
      const url = new URL(`http://localhost:8000/chat/send/${this.currentSessionId}`);
      url.searchParams.append("message", this.text);
      url.searchParams.append("provider", "wen");

      fetch(url.href, {
        method: "GET",
      })
          .then(response => response.text())
          .then(data => {
            console.log("消息发送成功：", data);
            const message = {text: this.text, self: true};
            this.messages.push(message);
            this.text = ''; // 清空输入框
          })
          .catch(error => {
            console.error("消息发送失败：", error);
          });
    },
  },
  beforeDestroy() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  },
  mounted() {
    this.selectSession('1'); // 默认选择第一个会话
  }
};
</script>

<style>
.chat-container {
  display: flex;
  height: 100vh;
  font-family: 'Arial', sans-serif;
  background-color: #e1f5fe;
}

.sidebar {
  width: 250px;
  background-color: #ffffff;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  border-right: 1px solid #ccc;
  padding: 10px;
}

.sidebar h2 {
  font-size: 18px;
  margin-bottom: 20px;
  color: #2c3e50;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  margin-bottom: 10px;
}

.sidebar div {
  cursor: pointer;
  padding: 5px;
  border-radius: 5px;
  color: #2c3e50;
}

.sidebar .active {
  background-color: #ecf0f1;
  font-weight: bold;
}

.chat-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #e1f5fe;
}

.chat-header {
  background-color: #ffffff;
  padding: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chat-header h3 {
  margin: 0;
  font-size: 20px;
  color: #2c3e50;
}

.messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background-color: #ffffff;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  display: flex;
  align-items: center;
}

.self {
  align-self: flex-end;
  background-color: #81d4fa;
  color: #000;
}

.other {
  align-self: flex-start;
  background-color: #b2ebf2;
  color: #000;
}

.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 5px;
  word-wrap: break-word;
}

.input-area {
  display: flex;
  padding: 10px;
  background-color: #ffffff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.input-area input {
  flex: 1;
  padding: 10px;
  margin-right: 10px;
  border: none;
  border-radius: 5px;
  background-color: #ecf0f1;
  color: #2c3e50;
}

.input-area button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #4fc3f7;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s;
}

.input-area button:hover {
  background-color: #29b6f6;
}
</style>