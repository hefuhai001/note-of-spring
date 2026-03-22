<template>
  <div class="code-editor">
    <div ref="editorRef" class="editor-container"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted, computed } from 'vue'
import { EditorView, basicSetup } from 'codemirror'
import { EditorState } from '@codemirror/state'
import { cpp } from '@codemirror/lang-cpp'
import { oneDark } from '@codemirror/theme-one-dark'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: '// 在这里编写 C 代码...'
  },
  fontFamily: {
    type: String,
    default: 'Consolas, Monaco, "Courier New", monospace'
  },
  fontSize: {
    type: Number,
    default: 18
  }
})

const emit = defineEmits(['update:modelValue'])

const editorRef = ref(null)
let editorView = null

const editorTheme = computed(() => {
  return EditorView.theme({
    '&': { height: '100%' },
    '.cm-scroller': { overflow: 'auto' },
    '.cm-content': {
      minHeight: '300px',
      fontFamily: props.fontFamily,
      fontSize: `${props.fontSize}px`
    },
    '.cm-gutters': {
      fontFamily: props.fontFamily,
      fontSize: `${props.fontSize}px`
    }
  })
})

const createEditor = () => {
  if (editorView) {
    editorView.destroy()
  }

  const updateListener = EditorView.updateListener.of((update) => {
    if (update.docChanged) {
      const doc = update.state.doc.toString()
      emit('update:modelValue', doc)
    }
  })

  const state = EditorState.create({
    doc: props.modelValue || props.placeholder,
    extensions: [
      basicSetup,
      cpp(),
      oneDark,
      updateListener,
      EditorView.lineWrapping,
      editorTheme.value
    ]
  })

  editorView = new EditorView({
    state,
    parent: editorRef.value
  })
}

onMounted(() => {
  createEditor()
})

watch(() => props.modelValue, (newValue) => {
  if (editorView && newValue !== editorView.state.doc.toString()) {
    editorView.dispatch({
      changes: {
        from: 0,
        to: editorView.state.doc.length,
        insert: newValue
      }
    })
  }
})

watch([() => props.fontFamily, () => props.fontSize], () => {
  createEditor()
})

onUnmounted(() => {
  if (editorView) {
    editorView.destroy()
  }
})
</script>

<style scoped>
.code-editor {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #303030;
}

.editor-container {
  width: 100%;
  height: 100%;
  text-align: left;
}
</style>
