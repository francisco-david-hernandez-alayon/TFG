<template>
  <div>
    <!-- DIALOG BUTTON -->
    <v-tooltip :text="t('files.addFile')" location="bottom">
      <template #activator="{ props }">
        <v-btn v-bind="props" icon color="primary" variant="tonal" @click="dialog = true">
          <v-icon>mdi-file-plus</v-icon>
        </v-btn>
      </template>
    </v-tooltip>


    <!-- DIALOG -->
    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title class="text-h6 text-center text-secondary font-weight-bold mt-4">
          {{ t('files.addFile') }}
        </v-card-title>

        <v-card-text>
          <v-text-field v-model="fileName" :label="t('files.fileName')" outlined dense clearable />
          <v-text-field v-model="fileUri" :label="t('files.fileUri')" outlined dense clearable />

          <div class="text-caption text-grey mt-n3 mb-3">
            {{ t('files.optionalUriHint') }}
          </div>

          <v-text-field v-model="nodeName" :label="t('files.nodeName')" outlined dense clearable />
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn text @click="dialog = false">{{ t('close') }}</v-btn>
          <v-btn color="primary" :disabled="!fileName.trim() || !nodeName.trim()" @click="submitFile">
            {{ t('files.addFile') }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { File } from '@/domain/value-object/file';
import { FileName } from '@/domain/value-object/file-name';
import { NodeName } from '@/domain/value-object/node-name';

const { t } = useI18n();

const dialog = ref(false);
const fileName = ref('');
const fileUri = ref('');
const nodeName = ref('');

const emit = defineEmits<{
  (e: 'file-added', payload: { file: File; name: NodeName }): void;
}>();

const submitFile = () => {
  try {
    const name = new FileName(fileName.value.trim());
    const uri = fileUri.value.trim();
    const node = new NodeName(nodeName.value.trim());

    const file = uri ? new File(name, uri) : new File(name);

    emit('file-added', { file, name: node });
    resetDialog();
  } catch (error) {
    console.error(t('files.errorAddingFile') + ": " + error);
  }
};

const resetDialog = () => {
  fileName.value = '';
  fileUri.value = '';
  nodeName.value = '';
  dialog.value = false;
};
</script>
