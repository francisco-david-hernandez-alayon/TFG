<template>
  <g>
    <!-- Define arrow marker once -->
    <defs>
      <marker id="arrow" viewBox="0 0 10 10" refX="10" refY="5" markerWidth="6" markerHeight="6"
        orient="auto-start-reverse">
        <path d="M 0 0 L 10 5 L 0 10 z" fill="#666" />
      </marker>
    </defs>

    <!-- Main edge path with arrow -->
    <path class="vue-flow__connection animated" :class="{ selected: props.selected }" fill="none" :stroke="labelColor"
      stroke-width="2" :marker-end="'url(#arrow)'"
      :d="`M${props.sourceX},${props.sourceY} L${props.targetX},${props.targetY}`" />

    <!-- Invisible interaction path -->
    <path class="vue-flow__connection-interaction" :class="{ selected: props.selected }" fill="none"
      stroke="transparent" stroke-width="20"
      :d="`M${props.sourceX},${props.sourceY} L${props.targetX},${props.targetY}`" />

    <!-- Label background -->
    <rect :class="{ selected: props.selected }"
      :x="labelX - textWidth / 2 - rectPadding - (hasIcon ? iconExtraSpace : 0)" :y="labelY - rectPadding"
      :width="textWidth + rectPadding * 2 + (hasIcon ? iconExtraSpace : 0)" :height="rectPadding * 2" :fill="labelColor"
      rx="4" ry="4" />


    <!-- CONDITIONAL ICON -->
    <foreignObject v-if="hasIcon" :x="labelX - textWidth / 2 - iconOffsetX - iconSize / 2" :y="labelY - iconSize / 2"
      :width="iconSize" :height="iconSize" style="pointer-events: none;">
      <v-icon :color="'white'" :size="iconSize" class="mdi-icon">
        {{ props.data?.condition === 'true' ? 'mdi-check-circle-outline' : 'mdi-close' }}
      </v-icon>
    </foreignObject>


    <!-- Label text -->
    <text ref="labelRef" :x="labelX" :y="labelY" text-anchor="middle" alignment-baseline="middle"
      class="select-none text-sm text-body-2 text-white" style="pointer-events: none; fill: white;">
      {{ props.label || 'link' }}
    </text>
  </g>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import type { EdgeProps } from '@vue-flow/core'

const props = defineProps<EdgeProps>()

const positiveColor = '#8BC34A'; 
const negativeColor = '#F28B82'; 

// LABEL TEXT
const labelRef = ref<SVGTextElement | null>(null)
const textWidth = ref(0)
const labelX = computed(() => (props.sourceX + props.targetX) / 2)
const labelY = computed(() => (props.sourceY + props.targetY) / 2)

const labelColor = computed(() => (props.data?.enabled ? positiveColor : negativeColor))

const rectPadding = 12

// ICON SETTINGS
const iconSize = 24
const iconOffsetX = 14
const iconExtraSpace = 18

const hasIcon = computed(() => props.data?.condition === 'true' || props.data?.condition === 'false')

const updateTextWidth = () => {
  if (labelRef.value) {
    textWidth.value = labelRef.value.getBBox().width
  }
}

watch(() => props.label, updateTextWidth)
onMounted(updateTextWidth)
</script>

<style scoped>
.selected {
  stroke: #1976d2;
  stroke-width: 2;
  opacity: 1;
}
</style>
