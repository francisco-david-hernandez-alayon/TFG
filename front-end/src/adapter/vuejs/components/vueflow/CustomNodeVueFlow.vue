<script setup lang="ts">
import type { NodeProps } from '@vue-flow/core'
import { Handle, Position } from '@vue-flow/core'
import { computed } from 'vue'
import { NodeContentType } from '@/domain/enumerate/node-content-type'
import type { NodeContent } from '@/domain/value-object/node-content'
import { NodeIcon } from '@/domain/enumerate/node-icon'

const props = defineProps<NodeProps<{
  name: string
  type: NodeContentType
  color?: string
  icon?: string
  isInitial?: boolean
  nodeContent: NodeContent
}>>()

//------------------------------MAPS-------------------------------/
const colorMap: Record<string, string> = {
  RED: '#FFA3A3',
  GREEN: '#A5D6A7',
  BLUE: '#90CAF9',
  YELLOW: '#FFF59D',
  ORANGE: '#FFCC80',
  PURPLE: '#CE93D8',
  CYAN: '#80DEEA',
  GRAY: '#BDBDBD',
}

const iconMap: Record<NodeIcon, string> = {
  [NodeIcon.ROBOT]: 'mdi-robot',
  [NodeIcon.FILE]: 'mdi-file',
  [NodeIcon.WARNING]: 'mdi-alert-circle',
  [NodeIcon.GEAR]: 'mdi-cog',
  [NodeIcon.FOLDER]: 'mdi-folder',
  [NodeIcon.START]: 'mdi-flag-checkered',
}


const cardColor = computed(() => {
  const colorKey = props.data.color?.toUpperCase?.()
  return colorKey && colorMap[colorKey] ? colorMap[colorKey] : undefined
})

const shapeClass = computed(() => {
  switch (props.data.type) {
    case NodeContentType.FILE:
      return 'shape-file'
    case NodeContentType.CONDITIONAL:
      return 'shape-ellipse'
    case NodeContentType.OPERATION:
    default:
      return 'shape-rectangle'
  }
})

const iconClass = computed(() => {
  const iconKey = props.data.icon?.toUpperCase?.() as NodeIcon | undefined
  return iconKey && iconMap[iconKey] ? iconMap[iconKey] : undefined
})


</script>


<template>
  <div class="node-wrapper">
    <v-icon v-if="props.data.isInitial" color="amber darken-2" class="initial-icon">mdi-flag</v-icon>

    <div class="operation-node"
      :class="[shapeClass, { 'initial-node': props.data.isInitial, selected: props.selected }]"
      :style="cardColor ? { backgroundColor: cardColor, '--border-color': cardColor } : {}">


      <Handle type="target" :position="Position.Top" class="custom-handle" />
      <span class="node-title
      text-body-1 text-black">
        <v-icon v-if="iconClass" class="mr-2">{{ iconClass }}</v-icon>
        {{ props.data.name }}
      </span>
      <Handle type="source" :position="Position.Bottom" class="custom-handle" />
    </div>
  </div>
</template>


<style scoped>
/* NODE */
.selected {
  border: 2px solid #1976d2 !important;
  box-shadow: 0 0 10px rgba(25, 118, 210, 0.6);
}

.node-title {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.node-wrapper {
  position: relative;
  display: inline-block;
}

.operation-node {
  display: inline-block;
  min-width: 100px;
  max-width: 240px;
  font-family: 'Comic Sans MS', 'Cursive', sans-serif;
  transition: transform 0.2s, border-color 0.2s, box-shadow 0.2s;
  word-break: break-word;
  position: relative;
  padding: 0.75rem;
  text-align: center;
  background-color: transparent;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.15);
}

.operation-node:hover {
  transform: scale(1.05);
  cursor: pointer;
}

/* INITIAL */
.initial-icon {
  position: absolute;
  top: -40px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 34px;
  z-index: 2;
}

.initial-node {
  border: 5px solid #FFECB3;
  box-shadow: 0 0 12px rgba(255, 236, 179, 0.5);
}

/* RECTANGLE */
.shape-rectangle {
  border: 1px solid #555;
  border-radius: 16px;
}

/* FILE */
.shape-file {
  position: relative;
  border: 2px solid var(--border-color, #555);
  border-radius: 8px;
  width: 120px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.shape-file::before,
.shape-file::after {
  content: '';
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-style: solid;
  pointer-events: none;
}

.shape-file::before {
  left: -16px;
  border-width: 16px 16px 16px 0;
  border-color: transparent var(--border-color, #555) transparent transparent;
}

.shape-file::after {
  right: -16px;
  border-width: 16px 0 16px 16px;
  border-color: transparent transparent transparent var(--border-color, #555);
}

/* ELLIPSE */
.shape-ellipse {
  border: 1px solid #555;
  border-radius: 50% / 60%;
  min-width: 140px;
  max-width: 260px;
  padding: 1rem 1.5rem;
  box-sizing: border-box;
  font-family: 'Comic Sans MS', 'Cursive', sans-serif;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.15);
  transition: border-color 0.2s, box-shadow 0.2s;
  text-align: center;
  display: inline-block;
}



/* HANDLE */
.custom-handle {
  width: 10px;
  height: 10px;
  background: #333;
  border-radius: 50%;
  border: 2px solid white;
  position: absolute;
  z-index: 2;
}

.custom-handle:first-of-type {
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
}

.custom-handle:last-of-type {
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
}
</style>
