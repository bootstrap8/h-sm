<script lang="ts" setup>
import {ref, reactive, onMounted} from 'vue';
import axios from '@/network';
import {msg, deobfuscate, encryptAES} from '@/utils/Utils';
import CryptoJS from "crypto-js";
import router from "@/router";

const obfs = "969";
const key = CryptoJS.enc.Utf8.parse(deobfuscate("΍ΊϻΌΌϱϰϺϸΌϽϺϽΈϽϽ", obfs));
const iv = key;

const data = reactive({
  actions: []
});

onMounted(() => {
  console.log('页面加载后');
});

const username = ref('');
const password = ref('');
const showModal = ref(false);
const question = ref('');
const correctAnswer = ref<number | null>(null);
const userAnswer = ref<number | null>(null);

function generateQuestion() {
  const num1 = Math.floor(Math.random() * 10);
  const num2 = Math.floor(Math.random() * 10);
  correctAnswer.value = num1 + num2;
  question.value = `请计算: ${num1} + ${num2} = ?`;
}

function validateLogin() {
  showModal.value = true;
  generateQuestion();
  return false;
}

function confirmAnswer() {
  if (userAnswer.value === correctAnswer.value) {
    showModal.value = false;
    const form = {
      username: username.value,
      password: password.value
    };
    const body = encryptAES(JSON.stringify(form), key, iv);
    axios({
      url: '/system/login',
      method: 'post',
      headers: {
        'Content-Type': 'application/json;charset=utf8'
      },
      data: body
    }).then((res: any) => {
      if (res.data.state == 'OK') {
        router.push({path: '/main_left'});
      } else {
        msg(res.data.errorMessage, 'warning');
      }
    }).catch((err: Error) => {
      msg('请求异常', 'error');
    });
  } else {
    alert("答案错误，请重试！");
  }
}

function cancel() {
  showModal.value = false;
}
</script>

<template>
  <div class="container">
    <!-- 左侧背景 -->
    <div class="left-panel"></div>

    <!-- 右侧登录框 -->
    <div class="right-panel">
      <div class="form_container">
        <div class="title_container">
          <p class="title">登录你的账号</p>
          <span class="subtitle">开始使用我们的应用维护平台，只需创建一个帐户并享受体验。</span>
        </div>
        <br>
        <div class="input_container">
          <svg fill="none" viewBox="0 0 24 24" height="24" width="24" xmlns="http://www.w3.org/2000/svg" class="icon">
            <path stroke-linejoin="round" stroke-linecap="round" stroke-width="1.5" stroke="#141B34"
                  d="M12 4C9.79086 4 8 5.79086 8 8C8 10.2091 9.79086 12 12 12C14.2091 12 16 10.2091 16 8C16 5.79086 14.2091 4 12 4Z"></path>
            <path stroke-linejoin="round" stroke-width="1.5" stroke="#141B34"
                  d="M20 19C20 16.7909 18.2091 15 16 15H8C5.79086 15 4 16.7909 4 19V20H20V19Z"></path>
          </svg>
          <input placeholder="请输入账号" title="Input title" name="input-name" type="text" class="input_field"
                 id="email_field" v-model="username">
        </div>
        <div class="input_container">
          <svg fill="none" viewBox="0 0 24 24" height="24" width="24" xmlns="http://www.w3.org/2000/svg" class="icon">
            <path stroke-linecap="round" stroke-width="1.5" stroke="#141B34"
                  d="M18 11.0041C17.4166 9.91704 16.273 9.15775 14.9519 9.0993C13.477 9.03404 11.9788 9 10.329 9C8.67911 9 7.18091 9.03404 5.70604 9.0993C3.95328 9.17685 2.51295 10.4881 2.27882 12.1618C2.12602 13.2541 2 14.3734 2 15.5134C2 16.6534 2.12602 17.7727 2.27882 18.865C2.51295 20.5387 3.95328 21.8499 5.70604 21.9275C6.42013 21.9591 7.26041 21.9834 8 22"></path>
            <path stroke-linejoin="round" stroke-linecap="round" stroke-width="1.5" stroke="#141B34"
                  d="M6 9V6.5C6 4.01472 8.01472 2 10.5 2C12.9853 2 15 4.01472 15 6.5V9"></path>
            <path fill="#141B34"
                  d="M21.2046 15.1045L20.6242 15.6956V15.6956L21.2046 15.1045ZM21.4196 16.4767C21.7461 16.7972 22.2706 16.7924 22.5911 16.466C22.9116 16.1395 22.9068 15.615 22.5804 15.2945L21.4196 16.4767ZM18.0228 15.1045L17.4424 14.5134V14.5134L18.0228 15.1045ZM18.2379 18.0387C18.5643 18.3593 19.0888 18.3545 19.4094 18.028C19.7299 17.7016 19.7251 17.1771 19.3987 16.8565L18.2379 18.0387ZM14.2603 20.7619C13.7039 21.3082 12.7957 21.3082 12.2394 20.7619L11.0786 21.9441C12.2794 23.1232 14.2202 23.1232 15.4211 21.9441L14.2603 20.7619ZM12.2394 20.7619C11.6914 20.2239 11.6914 19.358 12.2394 18.82L11.0786 17.6378C9.86927 18.8252 9.86927 20.7567 11.0786 21.9441L12.2394 20.7619ZM12.2394 18.82C12.7957 18.2737 13.7039 18.2737 14.2603 18.82L15.4211 17.6378C14.2202 16.4587 12.2794 16.4587 11.0786 17.6378L12.2394 18.82ZM14.2603 18.82C14.8082 19.358 14.8082 20.2239 14.2603 20.7619L15.4211 21.9441C16.6304 20.7567 16.6304 18.8252 15.4211 17.6378L14.2603 18.82ZM20.6242 15.6956L21.4196 16.4767L22.5804 15.2945L21.785 14.5134L20.6242 15.6956ZM15.4211 18.82L17.8078 16.4767L16.647 15.2944L14.2603 17.6377L15.4211 18.82ZM17.8078 16.4767L18.6032 15.6956L17.4424 14.5134L16.647 15.2945L17.8078 16.4767ZM16.647 16.4767L18.2379 18.0387L19.3987 16.8565L17.8078 15.2945L16.647 16.4767ZM21.785 14.5134C21.4266 14.1616 21.0998 13.8383 20.7993 13.6131C20.4791 13.3732 20.096 13.1716 19.6137 13.1716V14.8284C19.6145 14.8284 19.619 14.8273 19.6395 14.8357C19.6663 14.8466 19.7183 14.8735 19.806 14.9391C19.9969 15.0822 20.2326 15.3112 20.6242 15.6956L21.785 14.5134ZM18.6032 15.6956C18.9948 15.3112 19.2305 15.0822 19.4215 14.9391C19.5091 14.8735 19.5611 14.8466 19.5879 14.8357C19.6084 14.8273 19.6129 14.8284 19.6137 14.8284V13.1716C19.1314 13.1716 18.7483 13.3732 18.4281 13.6131C18.1276 13.8383 17.8008 14.1616 17.4424 14.5134L18.6032 15.6956Z"></path>
          </svg>
          <input placeholder="请输入密码" title="Input title" name="input-name" type="password" class="input_field"
                 id="password_field" v-model="password">
        </div>
        <button title="登录" type="button" class="sign-in_btn" @click="validateLogin">
          <span>登   录</span>
        </button>
      </div>
    </div>
  </div>

  <!-- 自定义模态框 -->
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <h3>安全保护</h3>
      <p>检测到本次操作需输入验证码</p>
      <span @click="generateQuestion">{{ question }}</span>
      <input type="number" v-model="userAnswer" placeholder="请输入验证码">
      <div class="modal-buttons">
        <button @click="confirmAnswer">确定</button>
        <button @click="cancel">取消</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 容器布局 */
.container {
  display: flex;
  height: 100vh; /* 使容器占满整个视口高度 */
}

/* 左侧背景 */
.left-panel {
  flex: 1; /* 左侧占满剩余空间 */
  background: url('@/assets/login.png') no-repeat center center;
  background-size: 65%; /* 背景图片覆盖整个区域 */
}

/* 右侧登录框 */
.right-panel {
  width: 45%; /* 右侧宽度 */
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  background-color: white; /* 背景颜色 */
}

/* 登录框样式 */
.form_container {
  width: fit-content;
  height: fit-content;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 15px;
  padding: 50px 40px 20px 40px;
  background-color: #ffffff;
  box-shadow: 0px 106px 42px rgba(0, 0, 0, 0.01),
  0px 59px 36px rgba(0, 0, 0, 0.05), 0px 26px 26px rgba(0, 0, 0, 0.09),
  0px 7px 15px rgba(0, 0, 0, 0.1), 0px 0px 0px rgba(0, 0, 0, 0.1);
  border-radius: 11px;
  font-family: "Inter", sans-serif;
}

.title_container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 700;
  color: #212121;
}

.subtitle {
  font-size: 0.725rem;
  max-width: 80%;
  text-align: center;
  line-height: 1.1rem;
  color: #8B8E98;
}

.input_container {
  width: 100%;
  height: fit-content;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.icon {
  width: 20px;
  position: absolute;
  z-index: 99;
  left: 12px;
  bottom: 9px;
}

.input_label {
  font-size: 0.75rem;
  color: #8B8E98;
  font-weight: 600;
}

.input_field {
  width: auto;
  height: 40px;
  padding: 0 0 0 40px;
  border-radius: 7px;
  outline: none;
  border: 1px solid #e5e5e5;
  filter: drop-shadow(0px 1px 0px #efefef) drop-shadow(0px 1px 0.5px rgba(239, 239, 239, 0.5));
  transition: all 0.3s cubic-bezier(0.15, 0.83, 0.66, 1);
}

.input_field:focus {
  border: 1px solid transparent;
  box-shadow: 0px 0px 0px 2px #242424;
  background-color: transparent;
}

.sign-in_btn {
  width: 100%;
  height: 40px;
  border: 0;
  background: #115DFC;
  border-radius: 7px;
  outline: none;
  color: #ffffff;
  cursor: pointer;
}

.modal {
  display: flex;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border: 1px solid #888;
  width: 300px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.modal-content h3 {
  margin-top: 0;
  color: #ff5722;
}

.modal-content p {
  margin: 10px 0;
}

.modal-content input {
  width: 80%;
  padding: 8px;
  margin-top: 10px;
  margin-bottom: 10px;
}

.modal-buttons {
  display: flex;
  justify-content: space-around;
}

.modal-buttons button {
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 45%;
}

.modal-buttons button:hover {
  background-color: #0056b3;
}
</style>