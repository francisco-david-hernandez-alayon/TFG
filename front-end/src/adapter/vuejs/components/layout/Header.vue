<template>
    <v-app-bar app color="primary" class="header-container">
        <v-btn icon @click="$emit('toggle-sidebar')">
            <v-icon>mdi-menu</v-icon>
        </v-btn>

        <v-toolbar-title>Workflow-Maker</v-toolbar-title>

        <v-spacer />

        <!-- CHANGE THEME -->
        <v-btn class="ml-2" variant="outlined" @click="toggleTheme">
            {{ currentThemeName }}
        </v-btn>

        <!-- CHANGE LANGUAGE -->
        <v-select :items="[
            { title: 'English', value: 'en' },
            { title: 'Español', value: 'es' }
        ]" v-model="selectedLanguage" @update:modelValue="changeLanguage" label="Language" variant="underlined"
            density="comfortable" style="max-width: 150px" hide-details class="ml-4" />


    </v-app-bar>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useTheme } from 'vuetify'

const { t, locale } = useI18n()
const selectedLanguage = ref(locale.value)
const changeLanguage = (lang: string) => {
    locale.value = lang
}

// Vuetify theme control
const theme = useTheme()

// Custom themes
const availableThemes = ['MainTheme', 'SunsetTheme', 'ForestTheme', 'GrapeTheme']

function toggleTheme() {
    const current = theme.global.name.value
    const currentIndex = availableThemes.indexOf(current)
    const nextIndex = (currentIndex + 1) % availableThemes.length
    theme.global.name.value = availableThemes[nextIndex]
}

const currentThemeName = computed(() => theme.global.name.value)

defineEmits(['toggle-sidebar'])
</script>

<style>
.header-container {
    display: flex;
    flex-direction: row;
    gap: 50px;
}
</style>
