<template>
  <div class="w-full p-4">
    <div class="flex gap-4 mb-3 p-2 bg-[#1e1e1e] rounded-lg">
      <label class="flex items-center gap-2 text-gray-300 text-sm">
        字体:
        <select v-model="fontFamily"
          class="px-2 py-1 bg-[#2d2d2d] text-white border border-[#444] rounded cursor-pointer hover:border-[#666]">
          <option value="Consolas, Monaco, 'Courier New', monospace">Consolas</option>
          <option value="'Fira Code', monospace">Fira Code</option>
          <option value="'JetBrains Mono', monospace">JetBrains Mono</option>
          <option value="'Source Code Pro', monospace">Source Code Pro</option>
          <option value="Monaco, Consolas, monospace">Monaco</option>
        </select>
      </label>
      <label class="flex items-center gap-2 text-gray-300 text-sm">
        字号:
        <select v-model="fontSize"
          class="px-2 py-1 bg-[#2d2d2d] text-white border border-[#444] rounded cursor-pointer hover:border-[#666]">
          <option v-for="size in [12, 13, 14, 15, 16, 18, 20]" :key="size" :value="size">{{ size }}px</option>
        </select>
      </label>
    </div>

    <CodeEditor v-model="code" :font-family="fontFamily" :font-size="fontSize" class="h-[350px]" />

    <div class="flex gap-3 mt-4">
      <button @click="handleCompile" :disabled="loading"
        class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
        {{ loading ? '编译中...' : '编译' }}
      </button>
      <button @click="handleRun" :disabled="loading || !taskId"
        class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
        {{ loading ? '运行中...' : '运行' }}
      </button>
      <button @click="handleCompileAndRun" :disabled="loading"
        class="px-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors">
        {{ loading ? '执行中...' : '编译并运行' }}
      </button>
    </div>

    <div v-if="taskId" class="mt-3 p-2 bg-[#2d2d2d] rounded text-gray-400 text-sm">
      Task ID: <span class="text-green-400 font-mono">{{ taskId }}</span>
    </div>

    <div v-if="output" class="mt-4">
      <h3 class="text-gray-300 text-sm mb-2">输出结果:</h3>
      <pre
        class="p-4 bg-[#1e1e1e] rounded-lg text-gray-200 text-sm overflow-auto max-h-[300px] whitespace-pre-wrap">{{ output }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import CodeEditor from './CodeEditor.vue'
import { compileApi } from '../api/compile'

const code = ref(`#include <stdio.h>

int main() {
    printf("Hello, World!\\n");
    return 0;
}
`)

const fontFamily = ref("Consolas, Monaco, 'Courier New', monospace")
const fontSize = ref(18)

const loading = ref(false)
const taskId = ref('')
const output = ref('')

const handleCompile = async () => {
  loading.value = true
  output.value = ''
  try {
    const result = await compileApi.compile(code.value)
    if (result.startsWith('编译错误')) {
      output.value = result
      taskId.value = ''
    } else {
      taskId.value = result
      output.value = `编译成功! Task ID: ${result}`
    }
  } catch (error) {
    output.value = `编译失败: ${error.message}`
  } finally {
    loading.value = false
  }
}

const handleRun = async () => {
  if (!taskId.value) {
    output.value = '请先编译代码'
    return
  }
  loading.value = true
  output.value = ''
  try {
    const result = await compileApi.run(taskId.value, [])
    output.value = result
  } catch (error) {
    output.value = `运行失败: ${error.message}`
  } finally {
    loading.value = false
  }
}

const handleCompileAndRun = async () => {
  loading.value = true
  output.value = ''
  taskId.value = ''
  try {
    const result = await compileApi.compileAndRun(code.value, [])
    output.value = result
  } catch (error) {
    output.value = `执行失败: ${error.message}`
  } finally {
    loading.value = false
  }
}
</script>
