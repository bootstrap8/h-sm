<script setup lang="ts">

import { defineProps,defineEmits,ref  } from 'vue';
import router from "@/router";

interface Radio {
  value: string;
  label: string;
}

const selectValue=ref('L-R')

const emit = defineEmits(['layout-change']);

function handleChangeInSetup(newValue: string) {
  selectValue.value=newValue
  emit('layout-change', newValue);
  if(newValue=='L-R'){
    router.push({path:'/main_left'})
  }else{
    router.push({path:'/main_top'})
  }
}

const props = defineProps<{
  radioName: string
}>();
</script>

<template>
  <div class="radio-inputs">
    <label class="radio">
      <input type="radio" :name="props.radioName" value="L-R" @change="handleChangeInSetup('L-R')" :checked="selectValue=='L-R'">
      <span class="name">L-R</span>
    </label>
    <label class="radio">
      <input type="radio" :name="props.radioName" value="U-D" @change="handleChangeInSetup('U-D')" :checked="selectValue=='U-D'">
      <span class="name">U-D</span>
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
  padding: 0.2rem;
  width: 120px;
  height: 2.7em;
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
  padding: .2rem 0;
  color: rgba(51, 65, 85, 1);
  transition: all .15s ease-in-out;
}

.radio-inputs .radio input:checked + .name {
  background-color: #fff;
  font-weight: 600;
}
</style>