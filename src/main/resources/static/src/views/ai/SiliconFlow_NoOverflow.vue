<script lang="ts" setup>
import {
  Edit,ArrowLeft
} from '@element-plus/icons-vue'
import {ref, reactive, onMounted, computed,provide, inject} from 'vue'
import axios from '@/network'
import {msg} from '@/utils/Utils'
import {ElMessage, ElMessageBox} from 'element-plus'
import type {FormInstance, FormRules} from 'element-plus'
import router from "@/router";
import ChatBox from "@/components/chat/ChatBox.vue";

onMounted(() => {
});

const headerCellStyle = () => {
  // 添加表头颜色
  return {backgroundColor: '#f5f5f5', color: '#333', fontWeight: 'bold'};
}

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
    <ChatBox/>
  </div>
</template>

<style scoped>
.container {
  flex-grow: 1;
  padding: 0;
  overflow: hidden;
  width: 100%;
}
</style>