<script setup lang="ts">

import { defineProps,defineEmits,ref  } from 'vue';

interface Radio {
  value: string;
  label: string;
}

const selectValue=ref(sessionStorage.getItem('h-sm-lang') || 'zh-CN')

const emit = defineEmits(['locale-change']);

function handleChangeInSetup(newValue: string) {
  selectValue.value=newValue
  emit('locale-change', newValue);
}

const props = defineProps<{
  radioName: string
}>();
</script>

<template>
  <div class="radio-inputs">
    <label class="radio">
      <input type="radio" :name="props.radioName" value="zh-CN" @change="handleChangeInSetup('zh-CN')" :checked="selectValue=='zh-CN'">
      <span class="name">中</span>
    </label>
    <label class="radio">
      <input type="radio" :name="props.radioName" value="en-US" @change="handleChangeInSetup('en-US')" :checked="selectValue=='en-US'">
      <span class="name">EN</span>
    </label>

    <label class="radio">
      <input type="radio" :name="props.radioName" value="ja-JP" @change="handleChangeInSetup('ja-JP')" :checked="selectValue=='ja-JP'">
      <span class="name">日本語</span>
    </label>
  </div>
</template>

<style scoped>
.radio-inputs {
  position: relative;
  display: flex;
  flex-wrap: wrap;
  border-radius: 0.5rem;
  background-color: #EEE;
  box-sizing: border-box;
  box-shadow: 0 0 0px 1px rgba(0, 0, 0, 0.06);
  padding: 0rem;
  width: 100px;
  height: 2.3em;
  font-size: 10px;
}

.radio-inputs .radio {
  flex: 1 1 auto;
  text-align: center;
}

.radio-inputs .radio input {
  display: none;
}

.radio-inputs .radio .name {
  display: flex;
  cursor: pointer;
  align-items: center;
  justify-content: center;
  border-radius: 0.5rem;
  border: none;
  padding: 0rem 0;
  color: rgba(51, 65, 85, 1);
  transition: all .15s ease-in-out;
  line-height: 2.3em; /* 根据容器高度调整 */
}

.radio-inputs .radio input:checked + .name {
  background-color: rgb(209.4, 236.7, 195.9);
  font-weight: 600;
}
</style>