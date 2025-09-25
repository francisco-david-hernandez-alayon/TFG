<template>
  <v-container class="fill-height d-flex align-center justify-center">
    <div class="import-wrapper">
      <v-row justify="center" align="stretch" class="mt-6">

        <!-- LEFT CARD - IMPORT -->
        <v-col cols="12" md="6">
          <v-card class="pa-6 fill-height" elevation="4">
            <v-card-title class="text-center mb-6">
              <v-icon class="mr-2">mdi-database-import</v-icon>
              {{ t('import.title') }}
            </v-card-title>

            <v-card-text class="text-center">
              <!-- DROPZONE -->
              <div class="drop-zone mb-6" @dragover.prevent @drop.prevent="handleDrop">
                <v-icon size="50" color="primary">mdi-file-upload</v-icon>
                <p class="mt-2 text-subtitle-2">{{ t('import.dropzone.text') }}</p>
              </div>

              <div class="my-6">{{ t('import.or') }}</div>

              <!-- BROWSE FILE -->
              <v-btn @click="triggerFileInput" color="primary" variant="outlined" size="large">
                <v-icon left>mdi-folder-open</v-icon>
                {{ t('import.browse.button') }}
              </v-btn>

              <input type="file" accept=".json" ref="fileInput" @change="handleFileChange" class="hidden" />
            </v-card-text>
          </v-card>
        </v-col>

        <!-- RIGHT CARD - FLOW DATA -->
        <v-col cols="12" md="6">
          <v-card class="pa-6 fill-height" elevation="4">
            <v-card-title class="text-center mb-6">
              <v-icon class="mr-2">mdi-chart-box-outline</v-icon>
              {{ t('import.preview.title') }}
            </v-card-title>

            <v-card-text>
              <div v-if="workFlow">
                <div class="flow-preview-line"><strong>ID:</strong> {{ workFlow.getId().getValue() }}</div>
                <div class="flow-preview-line"><strong>{{ t('import.preview.name') }}:</strong> {{ workFlow.getName().getName() }}</div>
                <div class="flow-preview-line"><strong>{{ t('import.preview.nodes') }}:</strong> {{ workFlow.getNodes().length }}</div>
                <div class="flow-preview-line"><strong>{{ t('import.preview.created') }}:</strong> {{ formatDate(workFlow.getCreationDate()) }}</div>
              </div>
              <div v-else class="text-center text-grey">
                {{ t('import.preview.empty') }}
              </div>
            </v-card-text>

            <v-card-actions class="justify-center mt-4" v-if="workFlow">
              <v-btn color="secondary" variant="elevated" size="large" @click="confirmImport">
                {{ t('import.confirm.button') }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </div>
  </v-container>
</template>



<script setup lang="ts">
import { ref, inject } from 'vue'
import { useI18n } from 'vue-i18n'
import { WorkFlow } from '@/domain/entity/workflow'
import { UserImportFileJsonResponse } from '@/adapter/http/response/user-import-file-json-response';
import { ExportFile } from '@/domain/value-object/export-file';
import { WorkFlowHttpRepository } from '@/adapter/http/repository/workflow-http-repository';

const { t } = useI18n()

const fileInput = ref<HTMLInputElement | null>(null);
const workFlow = ref<WorkFlow | null>(null);
const repoWorkFlows = new WorkFlowHttpRepository();
const exportFile = ref<ExportFile | null>(null);

function triggerFileInput() {
  fileInput.value?.click()
}

function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) readFile(file)
}

function handleDrop(e: DragEvent) {
  const file = e.dataTransfer?.files?.[0]
  if (file) readFile(file)
}

async function readFile(file: File) {
  try {
    const text = await file.text();
    const json = JSON.parse(text);

    const parsedFile = UserImportFileJsonResponse.toExportFile(json);
    exportFile.value = parsedFile;
    workFlow.value = parsedFile.getWorkFlow();

    showNotif("success", t('import.readFile.success') + ": " + file.name);
  } catch (error) {
    showNotif("error", t('import.readFile.error') + ": " + file.name + " - " + (error as Error).message);
  }
}

async function confirmImport() {
  if (!exportFile.value) return;

  try {
    const result = await repoWorkFlows.importWorkFlow(exportFile.value as ExportFile);

    if (result.isRight()) {
      showNotif('success', t('import.confirm.success') + ": " + workFlow.value?.getName().getName());

      // Clear state after successful import
      workFlow.value = null;
      exportFile.value = null;
      if (fileInput.value) fileInput.value.value = '';
    } else {
      showNotif('error', t('import.confirm.error') + ": " + workFlow.value?.getName().getName());
    }

  } catch (error) {
    showNotif("error", t('import.confirm.error') + ": " + (error as Error).message);
  }
}

const showNotif = inject('showNotif') as (color: string, message: string) => void

function formatDate(date: Date) {
  return new Date(date).toLocaleString()
}
</script>



<style scoped>
.import-wrapper {
  padding: 2rem;
  width: 100%;
  max-width: 1200px;
}

.drop-zone {
  border: 2px dashed #aaa;
  border-radius: 12px;
  padding: 2.5rem;
  text-align: center;
  color: #666;
  transition: border-color 0.3s, color 0.3s;
}

.drop-zone:hover {
  border-color: #1976d2;
  color: #1976d2;
  cursor: pointer;
}

.hidden {
  display: none;
}

.flow-preview-line {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  font-size: 1rem;
}
</style>
