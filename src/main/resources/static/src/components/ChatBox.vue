<template>
  <div v-html="renderedHTML" class="markdown-content"></div>
</template>

<script lang="ts" setup>
import {ref, computed, nextTick} from 'vue';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css'; // 引入 highlight.js 样式
import mk from 'markdown-it-katex';

// 定义消息数据结构
interface Message {
  type: 'user' | 'ai';
  text: string;
}

// 创建响应式数据
const showContent = ref('')
const md = new MarkdownIt({
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre><code class="hljs">${hljs.highlight(str, {language: lang}).value}</code></pre>`;
      } catch (__) {
      }
    }
    return `<pre><code class="hljs">${md.utils.escapeHtml(str)}</code></pre>`;
  },
}).use(mk);
const renderedHTML = computed(() => {
  return md.render(showContent.value);
});

// 模拟请求的 API 配置
const fetchStreamUrl = async (options) => {
  try {
    // 第一步：发送 POST 请求获取流配置和 SSE URL
    const response = await fetch(options.url, {
      method: options.method,
      headers: options.headers,
      body: options.body,
    });

    if (!response.ok) {
      throw new Error('请求失败');
    }

    const reader = response.body?.getReader();
    const decoder = new TextDecoder();
    let dataBuffer = '';

    // 持续读取数据
    while (true) {
      const {done, value} = await reader?.read() ?? {done: true, value: new Uint8Array()};
      if (done) break;

      // 解码二进制数据
      dataBuffer += decoder.decode(value, {stream: true});

      // 处理完整的事件流数据
      let index;
      while ((index = dataBuffer.indexOf('\n\n')) > -1) {
        const chunk = dataBuffer.slice(0, index).trim();
        dataBuffer = dataBuffer.slice(index + 2);

        if (chunk.startsWith('data: ')) {
          const message = chunk.slice(6); // 获取data后面的内容
          if (message == '[DONE]') {
            break;
          }
          handleEventMessage(message);
        }
      }
    }

    // 第二步：使用返回的 URL 启动事件流
    // startEventStream();
  } catch (error) {
    console.error('获取流数据失败', error);
  }
};

const handleEventMessage = (message: string) => {
  // 这里处理每个事件流消息
  const data = JSON.parse(message);
  if (data.choices && data.choices[0]?.delta) {
    const content = data.choices[0].delta.content;
    if (content) {
      appendMessage('ai', content);
    }
  }
};

// 向消息数组添加新消息
const appendMessage = (type: 'user' | 'ai', text: string) => {
  showContent.value = showContent.value + text
  scrollToBottom();
};

// 滚动到聊天的最底部
const scrollToBottom = () => {
  nextTick(() => {
    const chatContainer = document.querySelector('.chat-container');
    if (chatContainer) {
      chatContainer.scrollTop = chatContainer.scrollHeight;
    }
  });
};

defineExpose({
  fetchStreamUrl,
  appendMessage
});
</script>

<style scoped>
.markdown-content {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f9f9f9;
  font-family: "Times New Roman", Times, serif;
  line-height: 1.5;
}
</style>