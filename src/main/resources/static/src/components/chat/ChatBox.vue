<script lang="ts" setup>
import {Setting} from '@element-plus/icons-vue'
import {ref, reactive, onMounted, computed, provide, inject, nextTick} from 'vue'
import axios from '@/network'
import {msg} from '@/utils/Utils'
import {ElMessage, ElMessageBox} from 'element-plus'
import type {} from 'element-plus'
import router from "@/router";
import chatAI from '@/components/icon/AI2.vue'
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css'; // 引入 highlight.js 样式
import mk from 'markdown-it-katex';
import settings from '@/components/chat/settings.vue'

const props = defineProps({
  chatTitle: {
    type: String,
    default: 'Chat'
  },
  placeholder: {
    type: String,
    default: '请输入提示词？'
  },
  sendBtnText: {
    type: String,
    default: '发送'
  }
})

const showContent = ref('')
const md = new MarkdownIt({
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre><code class="hljs">${hljs.highlight(str, {language: lang}).value}</code></pre>`;
      } catch (__) {
      }
    }
    // console.log('输出内容: ',md.utils.escapeHtml(str))
    return `<pre><code class="hljs">${md.utils.escapeHtml(str)}</code></pre>`;
  },
}).use(mk);
const renderedHTML = computed(() => {
  return md.render(showContent.value);
});

const dialog = ref(false)
const form = reactive({
  apiUrl: 'https://api.siliconflow.cn/v1/chat/completions',
  apiSecret: 'sk-liedctubagwhaufblaqbesquyyhgmwfjeocirhxjyhkrvfqo',
  model: 'deepseek-ai/DeepSeek-R1',
  role: 'user',
  max_tokens: 4096,
  temperature: 0.7,
  top_p: 0.7,
  top_k: 50,
  frequency_penalty: 0.5,
  n: 1,
  msg: '你是谁？',
})

const showSettingDialog = () => {
  dialog.value = true
}

const handleClose = (done) => {
  done()
}

// 模拟请求的 API 配置
const fetchStreamData = async () => {
  try {
    let options = {
      url: form.apiUrl,
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + form.apiSecret,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        model: form.model,
        messages: [
          {
            role: form.role,
            content: form.msg
          }
        ],
        stream: true,
        max_tokens: form.max_tokens,
        stop: ["null"],
        temperature: form.temperature,
        top_p: form.top_p,
        top_k: form.top_k,
        frequency_penalty: form.frequency_penalty,
        n: form.n,
        response_format: {type: "text"}
      })
    };
    let userIcon = "development" == process.env.NODE_ENV ? '/icon/user.svg' : './icon/user.svg'
    let aiIcon = "development" == process.env.NODE_ENV ? '/icon/ai.svg' : './icon/ai.svg'
    showContent.value += '  \n  \n### ![User Icon](' + userIcon + ')\n' + form.msg
    showContent.value += '  \n  \n### ![AI Icon](' + aiIcon + ')\n'
    form.msg=''
    scrollToBottom()
    // 第一步：发送 POST 请求获取流配置和 SSE URL
    const response = await fetch(form.apiUrl, {
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
  showContent.value += text
  scrollToBottom();
};

// 滚动到聊天的最底部
const scrollToBottom = () => {
  nextTick(() => {
    const chatContainer = document.querySelector('.chat-body');
    if (chatContainer) {
      chatContainer.scrollTop = chatContainer.scrollHeight;
    }
  });
};

const debounce = (callback: (...args: any[]) => void, delay: number) => {
  let tid: any;
  return function (...args: any[]) {
    const ctx = self;
    tid && clearTimeout(tid);
    tid = setTimeout(() => {
      callback.apply(ctx, args);
    }, delay);
  };
};

const _ = (window as any).ResizeObserver;
(window as any).ResizeObserver = class ResizeObserver extends _ {
  constructor(callback: (...args: any[]) => void) {
    callback = debounce(callback, 20);
    super(callback);
  }
};

</script>

<template>
  <div class="container">
    <div class="chat-card">
      <div class="chat-header">
        <chatAI style="width: 20px;"/>
        <settings style="width: 20px;cursor: pointer" @click="showSettingDialog" class="settings-icon"/>
      </div>
      <div class="chat-body">
        <div v-html="renderedHTML"></div>
      </div>
      <div class="chat-footer">
        <input v-model="form.msg" :placeholder="props.placeholder" type="text">
        <button @click="fetchStreamData">{{ props.sendBtnText }}</button>
      </div>
    </div>


    <el-drawer
        v-model="dialog"
        title="模型设置"
        :before-close="handleClose"
        direction="rtl"
        class="demo-drawer"
        size="600"
    >
      <div class="demo-drawer__content">
        <el-form :model="form" label-width="25%">
          <el-form-item label="API地址">
            <el-input v-model="form.apiUrl" clearable/>
          </el-form-item>
          <el-form-item label="API秘钥">
            <el-input v-model="form.apiSecret" type="password"/>
          </el-form-item>
          <el-form-item label="模型名称" prop="model">
            <el-select v-model="form.model" placeholder="请选择" size="small" clearable filterable style="width:250px">
              <el-option key="deepseek-ai/DeepSeek-R1" label="deepseek-ai/DeepSeek-R1" value="deepseek-ai/DeepSeek-R1"/>
              <el-option key="Pro/deepseek-ai/DeepSeek-R1" label="Pro/deepseek-ai/DeepSeek-R1"
                         value="Pro/deepseek-ai/DeepSeek-R1"/>
              <el-option key="deepseek-ai/DeepSeek-V3" label="deepseek-ai/DeepSeek-V3" value="deepseek-ai/DeepSeek-V3"/>
              <el-option key="Pro/deepseek-ai/DeepSeek-V3" label="Pro/deepseek-ai/DeepSeek-V3"
                         value="Pro/deepseek-ai/DeepSeek-V3"/>
              <el-option key="Qwen/Qwen2.5-72B-Instruct-128K" label="Qwen/Qwen2.5-72B-Instruct-128K"
                         value="Qwen/Qwen2.5-72B-Instruct-128K"/>
              <el-option key="TeleAI/TeleChat2" label="TeleAI/TeleChat2" value="TeleAI/TeleChat2"/>
              <el-option key="meta-llama/Meta-Llama-3.1-405B-Instruct" label="meta-llama/Meta-Llama-3.1-405B-Instruct"
                         value="meta-llama/Meta-Llama-3.1-405B-Instruct"/>
              <el-option key="THUDM/glm-4-9b-chat" label="THUDM/glm-4-9b-chat" value="THUDM/glm-4-9b-chat"/>
              <el-option key="Vendor-A/Qwen/Qwen2.5-72B-Instruct" label="Vendor-A/Qwen/Qwen2.5-72B-Instruct" value="Vendor-A/Qwen/Qwen2.5-72B-Instruct"/>
              <el-option key="internlm/internlm2_5-20b-chat" label="internlm/internlm2_5-20b-chat" value="internlm/internlm2_5-20b-chat"/>
            </el-select>
          </el-form-item>
          <el-form-item label="角色名称" prop="role">
            <el-select v-model="form.role" placeholder="请选择" size="small" clearable filterable style="width:120px">
              <el-option key="user" label="user" value="user"/>
              <el-option key="ai" label="ai" value="ai"/>
            </el-select>
          </el-form-item>
          <el-form-item label="最大token" prop="max_tokens">
            <el-input v-model="form.max_tokens" type="number"/>
          </el-form-item>
          <el-form-item label="temperature" prop="temperature">
            <el-input v-model="form.temperature" type="number"/>
          </el-form-item>
          <el-form-item label="top_p" prop="top_p">
            <el-input v-model="form.top_p" type="number"/>
          </el-form-item>
          <el-form-item label="top_k" prop="top_k">
            <el-input v-model="form.top_k" type="number"/>
          </el-form-item>
          <el-form-item label="frequency_penalty" prop="frequency_penalty">
            <el-input v-model="form.frequency_penalty" type="number"/>
          </el-form-item>
          <el-form-item label="n" prop="n">
            <el-input v-model="form.n" type="number"/>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding: 0;
}

/* From Uiverse.io by ahmed150up */
.chat-card {
  width: 100%;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  height: calc(100vh - 160px);
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 10px;
  background-color: #f2f2f2;
  display: flex;
  align-items: center;
  height: 40px; /* 提高 header 高度 */
  justify-content: space-between;
  font-size: 16px;
  color: #333;
}

.chat-header .settings-icon {
  margin-left: 10px; /* 保证图标与标题之间有一定的间距 */
}


.chat-body {
  padding: 20px;
  overflow-y: auto;
  height: calc(100vh - 160px - 20px - 40px);
  flex-grow: 1;
  font-size: 12px;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
}

.incoming {
  background-color: #e1e1e1;
}

.outgoing {
  background-color: #f2f2f2;
  text-align: right;
}

.message p {
  font-size: 14px;
  color: #333;
  margin: 0;
}

.chat-footer {
  padding: 10px;
  background-color: #f2f2f2;
  display: flex;
  height: 40px;
  flex-shrink: 0;
}

.chat-footer input[type="text"] {
  flex-grow: 1;
  padding: 5px;
  border: none;
  border-radius: 3px;
}

.chat-footer button {
  padding: 5px 10px;
  border: none;
  background-color: #4285f4;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-footer button:hover {
  background-color: #0f9d58;
}

@keyframes chatAnimation {
  0% {
    opacity: 0;
    transform: translateY(10px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.chat-card .message {
  animation: chatAnimation 0.3s ease-in-out;
  animation-fill-mode: both;
  animation-delay: 0.1s;
}

.chat-card .message:nth-child(even) {
  animation-delay: 0.2s;
}

.chat-card .message:nth-child(odd) {
  animation-delay: 0.3s;
}

.demo-drawer {
  width: 300px;
}
</style>