<template>
    <v-snackbar v-model="visible" :timeout="timeout" :color="color" location="top right" elevation="4" multi-line
        class="ma-4">
        {{ message }}
    </v-snackbar>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

// Props
defineProps<{
    color: string;
    message: string;
    timeout?: number;
}>();


const visible = ref(false);

const emit = defineEmits(['shown', 'hidden']);

// Event when Show
const show = () => {
    visible.value = true;
    emit('shown');
};

// Event when Hide
watch(visible, (newVal) => {
    if (!newVal) {
        emit('hidden');
    }
});

defineExpose({
    show
});
</script>