<template>
  <v-navigation-drawer app permanent color="primary">
    <v-list-item to="/" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-home</v-icon>
      <span class="text-white">{{ t('sidebar.home') }}</span>
    </v-list-item>

    <v-divider :thickness="1" class="border-opacity-50 my-2" style="width: 90%; margin: 0 auto;" />

    <!-- OPERATIONS -->
    <v-list-item to="/operations" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-list-box</v-icon>
      <span class="text-white">{{ t('sidebar.operations') }}</span>
    </v-list-item>

    <v-list-item to="/operations/create" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-plus-box</v-icon>
      <span class="text-white">{{ t('sidebar.createOperation') }}</span>
    </v-list-item>

    <v-divider :thickness="1" class="border-opacity-50 my-2" style="width: 90%; margin: 0 auto;" />

    <!-- FLOWS -->
    <v-list-item to="/workflows" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-list-box-outline</v-icon>
      <span class="text-white">{{ t('sidebar.workflows') }}</span>
    </v-list-item>

    <v-list-item to="/workflows/design" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-pencil</v-icon>
      <span class="text-white">{{ t('sidebar.designWorkflow') }}</span>
    </v-list-item>

    <v-divider :thickness="1" class="border-opacity-50 my-2" style="width: 90%; margin: 0 auto;" />

    <!-- IMPORT/EXECUTE -->
    <v-list-item to="/workflows/import" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-import</v-icon>
      <span class="text-white">{{ t('sidebar.importWorkflow') }}</span>
    </v-list-item>

    <v-list-item to="/executions" class="list-item-custom">
      <v-icon class="text-white me-3">mdi-robot</v-icon>
      <span class="text-white">{{ t('sidebar.executions') }}</span>
    </v-list-item>
    
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
</script>

<style scoped>
.list-item-custom {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-left: 16px;
  cursor: pointer;
}
</style>
